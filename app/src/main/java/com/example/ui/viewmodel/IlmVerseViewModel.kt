package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.PlayerState
import com.example.audio.QuranAudioPlayer
import com.example.data.db.AppDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.DailyChallengeEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserQuestionHistoryEntity
import com.example.data.repository.IlmVerseRepository
import com.example.data.seed.QuestionBankSeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object QuizSession : Screen("quiz_session")
    object QuranAudio : Screen("quran_audio")
    object Hadith : Screen("hadith")
    object Learning : Screen("learning")
    object History : Screen("history")
    object Leaderboard : Screen("leaderboard")
    object Achievements : Screen("achievements")
    object Profile : Screen("profile")
    object Admin : Screen("admin")
}

data class QuizSessionState(
    val category: String = "QURAN",
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val consecutiveStreak: Int = 0,
    val sessionCoinsEarned: Int = 0,
    val sessionXpEarned: Int = 0,
    val isCompleted: Boolean = false,
    val isBankExhausted: Boolean = false,
    val timeRemainingSeconds: Int = 30
)

class IlmVerseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: IlmVerseRepository
    val audioPlayer: QuranAudioPlayer

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _quizSessionState = MutableStateFlow(QuizSessionState())
    val quizSessionState: StateFlow<QuizSessionState> = _quizSessionState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Screen backstack tracking for custom state navigation & BackHandler
    private val screenStack = mutableListOf<Screen>()

    private var timerJob: Job? = null

    init {
        val db = AppDatabase.getInstance(application)
        repository = IlmVerseRepository(db.ilmVerseDao())
        audioPlayer = QuranAudioPlayer(application)

        viewModelScope.launch {
            repository.ensureInitialized()
            repository.ensureTodayChallenge()
        }
    }

    val user: StateFlow<UserEntity?> = repository.user
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allQuestions: StateFlow<List<QuestionEntity>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminQuestions: StateFlow<List<QuestionEntity>> = repository.adminQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userHistory: StateFlow<List<UserQuestionHistoryEntity>> = repository.userHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coinTransactions: StateFlow<List<CoinTransactionEntity>> = repository.coinTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<AchievementEntity>> = repository.achievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayChallenge: StateFlow<DailyChallengeEntity?> = repository.getTodayChallenge()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val audioPlayerState: StateFlow<PlayerState> = audioPlayer.playerState

    // Navigation
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.lastIndex)
            return true
        } else if (_currentScreen.value != Screen.Home) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    // Language Toggle
    fun toggleLanguage() {
        viewModelScope.launch {
            val current = user.value ?: return@launch
            val newLang = if (current.language == "bn") "en" else "bn"
            repository.updateUser(current.copy(language = newLang))
        }
    }

    // Avatar Update
    fun updateAvatar(newAvatar: String) {
        viewModelScope.launch {
            val current = user.value ?: return@launch
            repository.updateUser(current.copy(avatar = newAvatar))
        }
    }

    // Start Quiz Session with Never-Repeat filtering
    fun startQuiz(category: String) {
        viewModelScope.launch {
            // Fetch unseen questions prioritized
            val unseen = repository.getUnseenQuestions(category).firstOrNull() ?: emptyList()

            if (unseen.isEmpty()) {
                // If unseen questions are exhausted, check total in this category
                val allInCat = repository.getQuestionsByCategory(category).firstOrNull() ?: emptyList()
                if (allInCat.isEmpty()) {
                    _quizSessionState.value = QuizSessionState(
                        category = category,
                        questions = emptyList(),
                        isBankExhausted = true
                    )
                } else {
                    // Let user know all questions have been completed!
                    _quizSessionState.value = QuizSessionState(
                        category = category,
                        questions = allInCat.shuffled(),
                        isBankExhausted = true
                    )
                }
            } else {
                _quizSessionState.value = QuizSessionState(
                    category = category,
                    questions = unseen.shuffled(),
                    currentIndex = 0,
                    selectedOptionIndex = null,
                    isSubmitted = false,
                    consecutiveStreak = 0,
                    sessionCoinsEarned = 0,
                    sessionXpEarned = 0,
                    isCompleted = false,
                    isBankExhausted = false,
                    timeRemainingSeconds = 30
                )
            }
            navigateTo(Screen.QuizSession)
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val curr = _quizSessionState.value
                if (curr.isSubmitted || curr.isCompleted) break
                if (curr.timeRemainingSeconds > 0) {
                    _quizSessionState.value = curr.copy(timeRemainingSeconds = curr.timeRemainingSeconds - 1)
                } else {
                    // Time up! Auto submit as skipped/incorrect
                    submitAnswer(-1)
                    break
                }
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        if (_quizSessionState.value.isSubmitted) return
        _quizSessionState.value = _quizSessionState.value.copy(selectedOptionIndex = optionIndex)
    }

    fun submitAnswer(forcedOption: Int? = null) {
        val curr = _quizSessionState.value
        if (curr.isSubmitted || curr.questions.isEmpty()) return

        timerJob?.cancel()
        val q = curr.questions.getOrNull(curr.currentIndex) ?: return
        val selected = forcedOption ?: curr.selectedOptionIndex ?: -1
        val isCorrect = selected == q.correctIndex
        val timeSpent = 30 - curr.timeRemainingSeconds

        val newConsecutive = if (isCorrect) curr.consecutiveStreak + 1 else 0

        viewModelScope.launch {
            val (coins, xp) = repository.recordAnswer(
                question = q,
                selectedOptionIndex = selected,
                isCorrect = isCorrect,
                timeTakenSec = timeSpent,
                consecutiveStreak = newConsecutive
            )

            repository.incrementDailyChallengeProgress()

            _quizSessionState.value = curr.copy(
                selectedOptionIndex = if (selected >= 0) selected else null,
                isSubmitted = true,
                isCorrect = isCorrect,
                consecutiveStreak = newConsecutive,
                sessionCoinsEarned = curr.sessionCoinsEarned + coins,
                sessionXpEarned = curr.sessionXpEarned + xp
            )
        }
    }

    fun nextQuestion() {
        val curr = _quizSessionState.value
        if (curr.currentIndex + 1 < curr.questions.size) {
            _quizSessionState.value = curr.copy(
                currentIndex = curr.currentIndex + 1,
                selectedOptionIndex = null,
                isSubmitted = false,
                isCorrect = false,
                timeRemainingSeconds = 30
            )
            startTimer()
        } else {
            // Session complete
            _quizSessionState.value = curr.copy(isCompleted = true)
        }
    }

    // Bookmarks
    fun toggleBookmark(type: String, title: String, subtitle: String, arabic: String?, reference: String) {
        viewModelScope.launch {
            repository.toggleBookmark(type, title, subtitle, arabic, reference)
        }
    }

    fun removeBookmark(id: Long) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    // Reset History
    fun resetUserHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // Search in Learning Mode
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Admin Panel Methods
    fun addAdminQuestion(
        category: String,
        questionBn: String,
        questionEn: String,
        optABn: String,
        optBBn: String,
        optCBn: String,
        optDBn: String,
        correctIdx: Int,
        explanationBn: String,
        explanationEn: String,
        arabic: String?,
        ref: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val fp = QuestionBankSeed.generateFingerprint(category, questionBn)
            val isDup = repository.checkDuplicateFingerprint(category, questionBn)
            if (isDup) {
                onResult(false, "এই প্রশ্নটি ইতিপূর্বেই প্রশ্নব্যাংকে সংরক্ষিত রয়েছে (ডুপ্লিকেট সনাক্ত হয়েছে)!")
                return@launch
            }
            val id = "q_custom_${System.currentTimeMillis()}"
            val entity = QuestionEntity(
                id = id,
                category = category,
                questionBn = questionBn,
                questionEn = questionEn.ifBlank { questionBn },
                optionABn = optABn,
                optionBBn = optBBn,
                optionCBn = optCBn,
                optionDBn = optDBn,
                optionAEn = optABn,
                optionBEn = optBBn,
                optionCEn = optCBn,
                optionDEn = optDBn,
                correctIndex = correctIdx,
                explanationBn = explanationBn,
                explanationEn = explanationEn.ifBlank { explanationBn },
                arabicText = arabic?.ifBlank { null },
                referenceSource = ref,
                fingerprint = fp
            )
            val success = repository.addQuestionAdmin(entity)
            if (success) {
                onResult(true, "প্রশ্নটি সফলভাবে প্রশ্নব্যাংকে অনুমোদিত ও সংরক্ষিত হয়েছে!")
            } else {
                onResult(false, "সংরক্ষণ ব্যর্থ হয়েছে।")
            }
        }
    }

    fun archiveQuestion(id: String) {
        viewModelScope.launch {
            repository.archiveQuestionAdmin(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
        timerJob?.cancel()
    }
}
