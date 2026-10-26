package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.DailyChallengeEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserQuestionHistoryEntity
import com.example.data.seed.QuestionBankSeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        QuestionEntity::class,
        UserQuestionHistoryEntity::class,
        CoinTransactionEntity::class,
        AchievementEntity::class,
        BookmarkEntity::class,
        DailyChallengeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ilmVerseDao(): IlmVerseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ilmverse_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate initial verified dataset asynchronously
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context).populateInitialData()
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun populateInitialData() {
        val dao = ilmVerseDao()
        // Seed default user
        if (dao.getUserDirect(1) == null) {
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
        }
        // Seed initial questions
        dao.insertQuestions(QuestionBankSeed.initialQuestions)
        // Seed achievements
        dao.insertAchievements(QuestionBankSeed.initialAchievements)
    }
}
