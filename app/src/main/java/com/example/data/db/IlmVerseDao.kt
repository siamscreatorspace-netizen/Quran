package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AchievementEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.DailyChallengeEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserQuestionHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IlmVerseDao {

    // --- USER ---
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUser(id: Int = 1): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserDirect(id: Int = 1): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    // --- QUESTIONS & NEVER-REPEAT LOGIC ---
    @Query("SELECT * FROM questions WHERE isApproved = 1 AND isArchived = 0 ORDER BY createdAt DESC")
    fun getAllApprovedQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions ORDER BY createdAt DESC")
    fun getAllQuestionsAdmin(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE category = :category AND isApproved = 1 AND isArchived = 0")
    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE category = :category AND isApproved = 1 AND isArchived = 0 AND id NOT IN (SELECT questionId FROM user_question_history WHERE userId = :userId)")
    fun getUnseenQuestions(category: String, userId: Int = 1): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id NOT IN (SELECT questionId FROM user_question_history WHERE userId = :userId) AND isApproved = 1 AND isArchived = 0")
    fun getAllUnseenQuestions(userId: Int = 1): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: String): QuestionEntity?

    @Query("SELECT * FROM questions WHERE fingerprint = :fingerprint LIMIT 1")
    suspend fun getQuestionByFingerprint(fingerprint: String): QuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT COUNT(*) FROM questions")
    fun getTotalQuestionCount(): Flow<Int>

    // --- USER QUESTION HISTORY ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: UserQuestionHistoryEntity)

    @Query("SELECT * FROM user_question_history WHERE userId = :userId ORDER BY answeredAt DESC")
    fun getUserHistory(userId: Int = 1): Flow<List<UserQuestionHistoryEntity>>

    @Query("SELECT questionId FROM user_question_history WHERE userId = :userId")
    fun getAnsweredQuestionIds(userId: Int = 1): Flow<List<String>>

    @Query("DELETE FROM user_question_history WHERE userId = :userId")
    suspend fun clearUserHistory(userId: Int = 1)

    // --- COIN TRANSACTIONS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinTransaction(transaction: CoinTransactionEntity)

    @Query("SELECT * FROM coin_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getCoinTransactions(userId: Int = 1): Flow<List<CoinTransactionEntity>>

    // --- ACHIEVEMENTS ---
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    // --- BOOKMARKS ---
    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Long)

    @Query("DELETE FROM bookmarks WHERE reference = :reference")
    suspend fun deleteBookmarkByReference(reference: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE reference = :reference)")
    fun isBookmarked(reference: String): Flow<Boolean>

    // --- DAILY CHALLENGE ---
    @Query("SELECT * FROM daily_challenges WHERE date = :date LIMIT 1")
    fun getDailyChallenge(date: String): Flow<DailyChallengeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyChallenge(challenge: DailyChallengeEntity)

    @Update
    suspend fun updateDailyChallenge(challenge: DailyChallengeEntity)
}
