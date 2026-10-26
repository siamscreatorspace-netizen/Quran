package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PlayerState
import com.example.data.quran.Surah
import com.example.data.seed.QuranDataSeed
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.IlmVerseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranAudioScreen(
    viewModel: IlmVerseViewModel,
    playerState: PlayerState,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranDataSeed.allSurahs
        } else {
            QuranDataSeed.allSurahs.filter {
                it.nameBengali.contains(searchQuery, ignoreCase = true) ||
                        it.nameEnglish.contains(searchQuery, ignoreCase = true) ||
                        it.nameArabic.contains(searchQuery) ||
                        it.number.toString() == searchQuery.trim()
            }
        }
    }

    val currentSurah = remember(playerState.surahNumber) {
        QuranDataSeed.allSurahs.find { it.number == playerState.surahNumber }
            ?: QuranDataSeed.allSurahs.first()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("quran_audio_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("quran_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = if (isBn) "কুরআন তেলাওয়াত ও অডিও" else "Quran Recitation & Audio",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Active Player Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Surah Name and Qari Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${currentSurah.number}. ${if (isBn) currentSurah.nameBengali else currentSurah.nameEnglish} (${currentSurah.revelationType})",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark
                        )
                        Text(
                            text = "ক্বারী মিশারী রশিদ আল-আফাসী • আয়াত: ${playerState.ayahNumber}",
                            fontSize = 11.sp,
                            color = EmeraldLight
                        )
                    }

                    Text(
                        text = currentSurah.nameArabic,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldBright
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Arabic Text Box (RTL)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(EmeraldContainer.copy(alpha = 0.5f))
                        .padding(14.dp)
                ) {
                    Text(
                        text = playerState.currentAyah?.arabicText ?: "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EmeraldDark,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bengali Translation
                Text(
                    text = playerState.currentAyah?.bengaliTranslation ?: "অনুবাদ লোড হচ্ছে...",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Slider / Seekbar
                val maxDuration = if (playerState.durationMs > 0) playerState.durationMs.toFloat() else 100f
                val progress = playerState.currentPositionMs.toFloat().coerceIn(0f, maxDuration)

                Slider(
                    value = progress,
                    onValueChange = { viewModel.audioPlayer.seekTo(it.toInt()) },
                    valueRange = 0f..maxDuration,
                    colors = SliderDefaults.colors(
                        thumbColor = EmeraldPrimary,
                        activeTrackColor = EmeraldPrimary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Time labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(playerState.currentPositionMs),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatTime(playerState.durationMs),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Player Controls (Previous, Play/Pause, Next, Repeat)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Repeat Toggle
                    IconButton(
                        onClick = { viewModel.audioPlayer.toggleRepeat() },
                        modifier = Modifier.testTag("audio_repeat_btn")
                    ) {
                        Icon(
                            imageVector = if (playerState.isRepeatOne) Icons.Default.RepeatOne else Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint = if (playerState.isRepeatOne) EmeraldPrimary else Color.Gray
                        )
                    }

                    // Previous Ayah
                    IconButton(
                        onClick = { viewModel.audioPlayer.previousAyah() },
                        modifier = Modifier.testTag("audio_prev_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = EmeraldDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Play / Pause / Buffering Button
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary)
                            .clickable { viewModel.audioPlayer.togglePlayPause() }
                            .testTag("audio_play_pause_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (playerState.isBuffering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // Next Ayah
                    IconButton(
                        onClick = { viewModel.audioPlayer.nextAyah() },
                        modifier = Modifier.testTag("audio_next_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = EmeraldDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Bookmark Ayah
                    IconButton(
                        onClick = {
                            viewModel.toggleBookmark(
                                type = "QURAN_AYAH",
                                title = "${currentSurah.nameBengali} - আয়াত ${playerState.ayahNumber}",
                                subtitle = "কুরআন মাজীদ",
                                arabic = playerState.currentAyah?.arabicText,
                                reference = "সূরা ${currentSurah.nameBengali} (${currentSurah.number}:${playerState.ayahNumber})"
                            )
                        },
                        modifier = Modifier.testTag("audio_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark Ayah",
                            tint = GoldBright
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar for 114 Surahs
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (isBn) "সূরা খুঁজুন (নাম বা নম্বর)..." else "Search Surah...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = EmeraldPrimary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("surah_search_input"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // List of Surahs
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("surahs_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            items(filteredSurahs) { surah ->
                val isSelected = surah.number == playerState.surahNumber
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.audioPlayer.playAyah(surah.number, 1) }
                        .testTag("surah_item_${surah.number}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) EmeraldContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${surah.number}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isBn) surah.nameBengali else surah.nameEnglish,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${surah.revelationType} • ${surah.totalAyahs} ${if (isBn) "আয়াত" else "Ayahs"}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = surah.nameArabic,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) EmeraldDark else GoldAccent
                        )
                    }
                }
            }
        }
    }
}

fun formatTime(ms: Int): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return String.format("%02d:%02d", min, sec)
}
