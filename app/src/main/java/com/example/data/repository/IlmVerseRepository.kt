package com.example.data.repository

import com.example.data.db.IlmVerseDao
import com.example.data.model.AchievementEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.DailyChallengeEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserQuestionHistoryEntity
import com.example.data.seed.QuestionBankSeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IlmVerseRepository(private val dao: IlmVerseDao) {

    val user: Flow<UserEntity?> = dao.getUser(1)
    val allQuestions: Flow<List<QuestionEntity>> = dao.getAllApprovedQuestions()
    val adminQuestions: Flow<List<QuestionEntity>> = dao.getAllQuestionsAdmin()
    val userHistory: Flow<List<UserQuestionHistoryEntity>> = dao.getUserHistory(1)
    val coinTransactions: Flow<List<CoinTransactionEntity>> = dao.getCoinTransactions(1)
    val achievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()
    val bookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val totalQuestionsCount: Flow<Int> = dao.getTotalQuestionCount()

    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>> {
        return dao.getQuestionsByCategory(category)
    }

    // Never-Repeat System: returns questions not yet answered by the user
    fun getUnseenQuestions(category: String): Flow<List<QuestionEntity>> {
        return dao.getUnseenQuestions(category, 1)
    }

    fun getAllUnseenQuestions(): Flow<List<QuestionEntity>> {
        return dao.getAllUnseenQuestions(1)
    }

    suspend fun ensureInitialized() {
        val user = dao.getUserDirect(1)
        if (user == null) {
            dao.insertUser(
                UserEntity(
                    id = 1,
                    name = "মুসাফির (Knowledge Seeker)",
                    avatar = "scholar",
                    coins = 100,
                    xp = 50,
                    level = 1,
                    streak = 1,
                    language = "bn"
                )
            )
            dao.insertQuestions(QuestionBankSeed.initialQuestions)
            dao.insertAchievements(QuestionBankSeed.initialAchievements)
        }
    }

    suspend fun updateUser(user: UserEntity) {
        dao.updateUser(user)
    }

    suspend fun recordAnswer(
        question: QuestionEntity,
        selectedOptionIndex: Int,
        isCorrect: Boolean,
        timeTakenSec: Int,
        consecutiveStreak: Int
    ): Pair<Int, Int> { // Returns (coinsAwarded, xpAwarded)
        val user = dao.getUserDirect(1) ?: return Pair(0, 0)

        // 1. Record in History for Never-Repeat guarantees
        dao.insertHistory(
            UserQuestionHistoryEntity(
                userId = 1,
                questionId = question.id,
                fingerprint = question.fingerprint,
                category = question.category,
                answeredAt = System.currentTimeMillis(),
                isCorrect = isCorrect,
                selectedOptionIndex = selectedOptionIndex,
                timeTakenSeconds = timeTakenSec
            )
        )

        var coinsAwarded = 0
        var xpAwarded = 0

        if (isCorrect) {
            coinsAwarded += 10
            xpAwarded += 15

            // Consecutive streak bonus: 5 in a row awards +20 extra coins
            if (consecutiveStreak > 0 && consecutiveStreak % 5 == 0) {
                coinsAwarded += 20
                dao.insertCoinTransaction(
                    CoinTransactionEntity(
                        userId = 1,
                        amount = 20,
                        reasonBn = "টানা ৫টি সঠিক উত্তরের বোনাস!",
                        reasonEn = "5-in-a-row correct answers bonus!"
                    )
                )
            }

            dao.insertCoinTransaction(
                CoinTransactionEntity(
                    userId = 1,
                    amount = 10,
                    reasonBn = "সঠিক উত্তর পুরস্কার: ${question.category}",
                    reasonEn = "Correct answer reward: ${question.category}"
                )
            )
        } else {
            // Participation XP
            xpAwarded += 2
        }

        val newCoins = user.coins + coinsAwarded
        val newXp = user.xp + xpAwarded
        val newLevel = 1 + (newXp / 100)
        val newTotalAnswered = user.totalAnswered + 1
        val newTotalCorrect = if (isCorrect) user.totalCorrect + 1 else user.totalCorrect

        dao.updateUser(
            user.copy(
                coins = newCoins,
                xp = newXp,
                level = newLevel,
                totalAnswered = newTotalAnswered,
                totalCorrect = newTotalCorrect
            )
        )

        // Check achievements
        checkAchievements(newTotalAnswered, newTotalCorrect, consecutiveStreak, question.category)

        return Pair(coinsAwarded, xpAwarded)
    }

    private suspend fun checkAchievements(
        totalAnswered: Int,
        totalCorrect: Int,
        streak: Int,
        category: String
    ) {
        val list = dao.getAllAchievements().firstOrNull() ?: emptyList()
        for (ach in list) {
            if (!ach.isUnlocked) {
                val shouldUnlock = when (ach.requirementType) {
                    "TOTAL_QUIZZES" -> totalAnswered >= ach.requirementTarget
                    "STREAK" -> streak >= ach.requirementTarget
                    "QURAN_CORRECT" -> category == "QURAN" && totalCorrect >= ach.requirementTarget
                    "HADITH_CORRECT" -> category == "HADITH" && totalCorrect >= ach.requirementTarget
                    "BD_CORRECT" -> (category == "BANGLADESH_GK" || category == "BANGLADESH_HISTORY") && totalCorrect >= ach.requirementTarget
                    "TOTAL_QUESTIONS" -> totalAnswered >= ach.requirementTarget
                    else -> false
                }
                if (shouldUnlock) {
                    dao.updateAchievement(
                        ach.copy(isUnlocked = true, unlockedAt = System.currentTimeMillis())
                    )
                    // Bonus coins for achievement
                    awardCoins(30, "অর্জন আনলক: ${ach.titleBn}", "Achievement unlocked: ${ach.titleEn}")
                }
            }
        }
    }

    suspend fun awardCoins(amount: Int, reasonBn: String, reasonEn: String) {
        val user = dao.getUserDirect(1) ?: return
        dao.updateUser(user.copy(coins = user.coins + amount))
        dao.insertCoinTransaction(
            CoinTransactionEntity(
                userId = 1,
                amount = amount,
                reasonBn = reasonBn,
                reasonEn = reasonEn
            )
        )
    }

    suspend fun clearHistory() {
        dao.clearUserHistory(1)
        val user = dao.getUserDirect(1)
        if (user != null) {
            dao.updateUser(user.copy(totalAnswered = 0, totalCorrect = 0))
        }
    }

    // Bookmarks
    suspend fun toggleBookmark(
        type: String,
        title: String,
        subtitle: String,
        arabic: String?,
        reference: String
    ) {
        val exists = dao.isBookmarked(reference).firstOrNull() ?: false
        if (exists) {
            dao.deleteBookmarkByReference(reference)
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    itemType = type,
                    title = title,
                    subtitle = subtitle,
                    arabicText = arabic,
                    reference = reference
                )
            )
        }
    }

    suspend fun removeBookmark(id: Long) {
        dao.deleteBookmark(id)
    }

    // Daily Challenge
    fun getTodayChallenge(): Flow<DailyChallengeEntity?> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dao.getDailyChallenge(today)
    }

    suspend fun ensureTodayChallenge(): DailyChallengeEntity {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val existing = dao.getDailyChallenge(today).firstOrNull()
        if (existing != null) return existing

        val challenge = DailyChallengeEntity(
            date = today,
            titleBn = "দৈনিক কুরআন ও জ্ঞান চ্যালেঞ্জ",
            titleEn = "Daily Quran & Knowledge Quest",
            category = "QURAN",
            targetCount = 3,
            completedCount = 0,
            rewardCoins = 25
        )
        dao.insertDailyChallenge(challenge)
        return challenge
    }

    suspend fun incrementDailyChallengeProgress() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val existing = dao.getDailyChallenge(today).firstOrNull() ?: return
        if (existing.isClaimed) return

        val newCount = existing.completedCount + 1
        val isCompleted = newCount >= existing.targetCount
        dao.updateDailyChallenge(existing.copy(completedCount = newCount))

        if (isCompleted && !existing.isClaimed) {
            dao.updateDailyChallenge(existing.copy(completedCount = newCount, isClaimed = true))
            awardCoins(existing.rewardCoins, "দৈনিক চ্যালেঞ্জ পুরস্কার সম্পন্ন!", "Daily Challenge reward completed!")
        }
    }

    // Admin Operations
    suspend fun checkDuplicateFingerprint(category: String, text: String): Boolean {
        val fp = QuestionBankSeed.generateFingerprint(category, text)
        return dao.getQuestionByFingerprint(fp) != null
    }

    suspend fun addQuestionAdmin(question: QuestionEntity): Boolean {
        if (dao.getQuestionByFingerprint(question.fingerprint) != null) {
            return false // duplicate detected
        }
        dao.insertQuestion(question)
        return true
    }

    suspend fun archiveQuestionAdmin(questionId: String) {
        val q = dao.getQuestionById(questionId) ?: return
        dao.updateQuestion(q.copy(isArchived = true))
    }
}
