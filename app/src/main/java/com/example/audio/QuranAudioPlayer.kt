package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.data.quran.Ayah
import com.example.data.seed.QuranDataSeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
    val currentAyah: Ayah? = null,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val isRepeatOne: Boolean = false,
    val errorMessage: String? = null
)

class QuranAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        // Set initial Ayah to Surah Al-Fatihah, Ayah 1
        val initialAyah = QuranDataSeed.featuredAyahs.firstOrNull()
        _playerState.value = _playerState.value.copy(
            surahNumber = 1,
            ayahNumber = 1,
            currentAyah = initialAyah
        )
    }

    fun playAyah(surah: Int, ayah: Int) {
        val foundAyah = QuranDataSeed.featuredAyahs.find { it.surahNumber == surah && it.ayahNumber == ayah }
            ?: Ayah(
                surahNumber = surah,
                ayahNumber = ayah,
                arabicText = "آيَةٌ مِنَ الْقُرْآنِ الْكَرِيمِ",
                bengaliTranslation = "কুরআনুল কারীমের পবিত্র বাণী।",
                englishTranslation = "Verse from the Holy Quran.",
                audioUrl = QuranDataSeed.getAudioUrl(surah, ayah)
            )

        _playerState.value = _playerState.value.copy(
            surahNumber = surah,
            ayahNumber = ayah,
            currentAyah = foundAyah,
            isBuffering = true,
            errorMessage = null
        )

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(foundAyah.audioUrl)
                setOnPreparedListener { mp ->
                    _playerState.value = _playerState.value.copy(
                        isBuffering = false,
                        isPlaying = true,
                        durationMs = mp.duration
                    )
                    mp.start()
                    startProgressTracker()
                }
                setOnCompletionListener {
                    if (_playerState.value.isRepeatOne) {
                        it.seekTo(0)
                        it.start()
                    } else {
                        nextAyah()
                    }
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("QuranAudioPlayer", "Playback error: what=$what, extra=$extra")
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        isBuffering = false,
                        errorMessage = "অডিও লোড হতে সমস্যা হয়েছে। ইন্টারনেট সংযোগ পরীক্ষা করুন।"
                    )
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Failed to start media player", e)
            _playerState.value = _playerState.value.copy(
                isPlaying = false,
                isBuffering = false,
                errorMessage = e.localizedMessage
            )
        }
    }

    fun togglePlayPause() {
        val mp = mediaPlayer
        if (mp != null) {
            if (mp.isPlaying) {
                mp.pause()
                _playerState.value = _playerState.value.copy(isPlaying = false)
            } else {
                mp.start()
                _playerState.value = _playerState.value.copy(isPlaying = true)
                startProgressTracker()
            }
        } else {
            playAyah(_playerState.value.surahNumber, _playerState.value.ayahNumber)
        }
    }

    fun nextAyah() {
        val currentSurah = _playerState.value.surahNumber
        val currentAyah = _playerState.value.ayahNumber
        val surahInfo = QuranDataSeed.allSurahs.find { it.number == currentSurah }
        val maxAyahs = surahInfo?.totalAyahs ?: 7

        if (currentAyah < maxAyahs) {
            playAyah(currentSurah, currentAyah + 1)
        } else if (currentSurah < 114) {
            playAyah(currentSurah + 1, 1)
        } else {
            // Reached end of Quran
            _playerState.value = _playerState.value.copy(isPlaying = false)
        }
    }

    fun previousAyah() {
        val currentSurah = _playerState.value.surahNumber
        val currentAyah = _playerState.value.ayahNumber

        if (currentAyah > 1) {
            playAyah(currentSurah, currentAyah - 1)
        } else if (currentSurah > 1) {
            val prevSurah = QuranDataSeed.allSurahs.find { it.number == currentSurah - 1 }
            playAyah(currentSurah - 1, prevSurah?.totalAyahs ?: 1)
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.seekTo(positionMs)
        _playerState.value = _playerState.value.copy(currentPositionMs = positionMs)
    }

    fun toggleRepeat() {
        _playerState.value = _playerState.value.copy(isRepeatOne = !_playerState.value.isRepeatOne)
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (true) {
                val mp = mediaPlayer
                if (mp != null && mp.isPlaying) {
                    _playerState.value = _playerState.value.copy(
                        currentPositionMs = mp.currentPosition,
                        durationMs = mp.duration
                    )
                }
                delay(500)
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
