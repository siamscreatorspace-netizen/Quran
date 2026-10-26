package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "মুসাফির (Seeker)",
    val avatar: String = "scholar", // "scholar", "student", "thinker", "champion"
    val coins: Int = 100,
    val xp: Int = 0,
    val level: Int = 1,
    val streak: Int = 1,
    val lastActiveDate: String = "",
    val language: String = "bn", // "bn" (default) or "en"
    val totalAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val dailyChallengeDate: String = "",
    val dailyChallengeCompleted: Boolean = false
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String, // e.g. "q_qur_001"
    val category: String,       // e.g. "QURAN", "HADITH", "BANGLADESH_GK", etc.
    val subcategory: String = "",
    val difficulty: String = "MEDIUM", // EASY, MEDIUM, HARD
    val questionBn: String,
    val questionEn: String,
    val optionABn: String,
    val optionBBn: String,
    val optionCBn: String,
    val optionDBn: String,
    val optionAEn: String,
    val optionBEn: String,
    val optionCEn: String,
    val optionDEn: String,
    val correctIndex: Int,      // 0, 1, 2, or 3
    val explanationBn: String,
    val explanationEn: String,
    val arabicText: String? = null,
    val referenceSource: String, // Exact Surah:Ayah, Hadith book & number, or verified historical reference
    val fingerprint: String,     // Content hash for duplicate prevention
    val isApproved: Boolean = true,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_question_history")
data class UserQuestionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int = 1,
    val questionId: String,
    val fingerprint: String,
    val category: String,
    val answeredAt: Long = System.currentTimeMillis(),
    val isCorrect: Boolean,
    val selectedOptionIndex: Int,
    val timeTakenSeconds: Int = 0,
    val isSkipped: Boolean = false
)

@Entity(tableName = "coin_transactions")
data class CoinTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int = 1,
    val amount: Int,
    val reasonBn: String,
    val reasonEn: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val titleEn: String,
    val descBn: String,
    val descEn: String,
    val icon: String,
    val requirementType: String,
    val requirementTarget: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemType: String, // "QUESTION", "QURAN_AYAH", "HADITH"
    val title: String,
    val subtitle: String,
    val arabicText: String? = null,
    val reference: String,
    val notes: String = "",
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey val date: String, // "YYYY-MM-DD"
    val titleBn: String,
    val titleEn: String,
    val category: String,
    val targetCount: Int = 5,
    val completedCount: Int = 0,
    val rewardCoins: Int = 25,
    val isClaimed: Boolean = false
)

enum class QuizCategory(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val iconName: String,
    val colorHex: Long
) {
    QURAN("QURAN", "আল-কুরআন কুইজ", "Quran Quiz", "menu_book", 0xFF059669),
    HADITH("HADITH", "সহীহ হাদিস কুইজ", "Authentic Hadith", "auto_stories", 0xFF0D9488),
    BANGLADESH_GK("BANGLADESH_GK", "বাংলাদেশ সাধারণ জ্ঞান", "Bangladesh GK", "flag", 0xFF16A34A),
    BANGLADESH_HISTORY("BANGLADESH_HISTORY", "ইতিহাস ও মুক্তিযুদ্ধ", "History & 1971 Liberation", "military_tech", 0xFFDC2626),
    WORLD_GEO("WORLD_GEO", "বিশ্ব ভূগোল", "World Geography", "public", 0xFF0284C7),
    SCIENCE_TECH("SCIENCE_TECH", "বিজ্ঞান ও প্রযুক্তি", "Science & Technology", "memory", 0xFF7C3AED),
    SPACE_ASTRONOMY("SPACE_ASTRONOMY", "মহাকাশ ও জ্যোতির্বিজ্ঞান", "Space & Astronomy", "rocket_launch", 0xFF4F46E5),
    WORLD_HISTORY("WORLD_HISTORY", "বিশ্ব ইতিহাস ও সভ্যতা", "World History", "account_balance", 0xFFD97706),
    MATH_LOGIC("MATH_LOGIC", "গণিত ও যৌক্তিক যুক্তি", "Math & Logic", "calculate", 0xFFE11D48),
    SPORTS("SPORTS", "খেলাধুলা ও অ্যাথলেটিক্স", "Sports", "sports_soccer", 0xFFEA580C),
    LITERATURE_LANG("LITERATURE_LANG", "সাহিত্য ও ভাষা", "Literature & Languages", "translate", 0xFF0891B2),
    NATURE_ENV("NATURE_ENV", "প্রকৃতি ও পরিবেশ", "Nature & Environment", "forest", 0xFF65A30D),
    CURRENT_AFFAIRS("CURRENT_AFFAIRS", "যাচাইকৃত সাম্প্রতিক বিষয়", "Verified Current Affairs", "newspaper", 0xFF475569),
    RIDDLES("RIDDLES", "ধাঁধা ও মেধা পরীক্ষা", "Riddles & Brain Challenges", "psychology", 0xFF9333EA)
}
