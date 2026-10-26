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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AchievementEntity
import com.example.data.model.DailyChallengeEntity
import com.example.data.model.QuizCategory
import com.example.data.model.UserEntity
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.IlmVerseViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: IlmVerseViewModel,
    user: UserEntity?,
    challenge: DailyChallengeEntity?,
    achievements: List<AchievementEntity>,
    modifier: Modifier = Modifier
) {
    val isBn = user?.language != "en"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Hero Banner
        item {
            HomeTopHeader(
                user = user,
                isBn = isBn,
                onLanguageToggle = { viewModel.toggleLanguage() },
                onProfileClick = { viewModel.navigateTo(Screen.Profile) },
                onAdminClick = { viewModel.navigateTo(Screen.Admin) }
            )
        }

        // Daily Streak & Performance Stats Bar
        item {
            UserStatsRow(user = user, isBn = isBn)
        }

        // Daily Challenge Card
        item {
            DailyChallengeBanner(
                challenge = challenge,
                isBn = isBn,
                onStartChallenge = {
                    viewModel.startQuiz(challenge?.category ?: "QURAN")
                }
            )
        }

        // Quick Launch Floating Button: Continue Learning
        item {
            ContinueLearningCard(
                isBn = isBn,
                onContinue = { viewModel.startQuiz("QURAN") },
                onAudioOpen = { viewModel.navigateTo(Screen.QuranAudio) }
            )
        }

        // Featured Categories Section Header
        item {
            SectionHeader(
                title = if (isBn) "কুইজ বিভাগসমূহ" else "Quiz Categories",
                subtitle = if (isBn) "আপনার প্রিয় বিষয়ে জ্ঞান যাচাই করুন" else "Test your knowledge across subjects"
            )
        }

        // Categories Grid
        items(QuizCategory.values().toList().chunked(2)) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (cat in pair) {
                    CategoryCard(
                        category = cat,
                        isBn = isBn,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.startQuiz(cat.id) }
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Recent Achievements Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = if (isBn) "অর্জিত ব্যাজসমূহ" else "Recent Achievements",
                subtitle = if (isBn) "কুইজ খেলে নতুন ব্যাজ আনলক করুন" else "Unlock badges as you learn",
                onSeeAll = { viewModel.navigateTo(Screen.Achievements) }
            )
        }

        item {
            AchievementsHorizontalList(achievements = achievements, isBn = isBn)
        }
    }
}

@Composable
fun HomeTopHeader(
    user: UserEntity?,
    isBn: Boolean,
    onLanguageToggle: () -> Unit,
    onProfileClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(EmeraldDark, EmeraldPrimary)
                )
            )
            .padding(top = 20.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Avatar and Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onProfileClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(GoldBright)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EmeraldDark,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Avatar",
                                tint = GoldBright,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isBn) "আসসালামু আলাইকুম," else "Assalamu Alaikum,",
                            fontSize = 12.sp,
                            color = Color(0xFFA7F3D0)
                        )
                        Text(
                            text = user?.name ?: "মুসাফির",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBn) "স্তর ${user?.level ?: 1} • ইলমভার্স" else "Level ${user?.level ?: 1} • IlmVerse",
                            fontSize = 11.sp,
                            color = GoldLight
                        )
                    }
                }

                // Top Actions: Language Toggle & Admin Panel & Coins Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onLanguageToggle,
                        modifier = Modifier.testTag("language_toggle_btn")
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isBn) "EN" else "বাং",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier.testTag("admin_panel_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Admin Panel",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coins & XP Balance Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coins
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = GoldBright,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${user?.coins ?: 0}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBn) "মোট কয়েন" else "Total Coins",
                            fontSize = 11.sp,
                            color = Color(0xFFA7F3D0)
                        )
                    }
                }

                // XP Points
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "XP",
                        tint = Color(0xFF67E8F9),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${user?.xp ?: 0} XP",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBn) "অভিজ্ঞতা" else "Experience",
                            fontSize = 11.sp,
                            color = Color(0xFFA7F3D0)
                        )
                    }
                }

                // Daily Streak
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = Color(0xFFF97316),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${user?.streak ?: 1} ${if (isBn) "দিন" else "Days"}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBn) "দৈনিক ধারাবাহিকতা" else "Daily Streak",
                            fontSize = 11.sp,
                            color = Color(0xFFA7F3D0)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserStatsRow(user: UserEntity?, isBn: Boolean) {
    val total = user?.totalAnswered ?: 0
    val correct = user?.totalCorrect ?: 0
    val percentage = if (total > 0) (correct * 100) / total else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$total",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Text(
                    text = if (isBn) "মোট প্রশ্ন" else "Questions",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$correct",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldLight
                )
                Text(
                    text = if (isBn) "সঠিক উত্তর" else "Correct",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$percentage%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Text(
                    text = if (isBn) "সফলতার হার" else "Accuracy",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DailyChallengeBanner(
    challenge: DailyChallengeEntity?,
    isBn: Boolean,
    onStartChallenge: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GoldLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldBright),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Challenge",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isBn) (challenge?.titleBn ?: "দৈনিক চ্যালেঞ্জ") else (challenge?.titleEn ?: "Daily Challenge"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = if (isBn) "পুরস্কার: +২৫ কয়েন" else "Reward: +25 Coins",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }

                Button(
                    onClick = onStartChallenge,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("start_daily_challenge_btn")
                ) {
                    Text(
                        text = if (isBn) "শুরু করুন" else "Play",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            val progress = challenge?.let {
                if (it.targetCount > 0) it.completedCount.toFloat() / it.targetCount.toFloat() else 0f
            } ?: 0f

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EmeraldPrimary,
                trackColor = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ContinueLearningCard(
    isBn: Boolean,
    onContinue: () -> Unit,
    onAudioOpen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Quick Quiz Button
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onContinue() }
                .testTag("quick_quiz_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Quick Quiz",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isBn) "কুইজ খেলুন" else "Quick Quiz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = EmeraldDark
                    )
                    Text(
                        text = if (isBn) "১০টি প্রশ্ন" else "10 Questions",
                        fontSize = 11.sp,
                        color = EmeraldDark.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Quran Audio Button
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onAudioOpen() }
                .testTag("quran_audio_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0284C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Quran Audio",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isBn) "কুরআন অডিও" else "Quran Audio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0369A1)
                    )
                    Text(
                        text = if (isBn) "১১৪ সূরা ও অর্থ" else "114 Surahs",
                        fontSize = 11.sp,
                        color = Color(0xFF0369A1).copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAll: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (onSeeAll != null) {
            Text(
                text = "সব দেখুন",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                modifier = Modifier.clickable { onSeeAll() }
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: QuizCategory,
    isBn: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(96.dp)
            .clickable { onClick() }
            .testTag("cat_card_${category.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(category.colorHex).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category.iconName),
                    contentDescription = category.titleEn,
                    tint = Color(category.colorHex),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = if (isBn) category.titleBn else category.titleEn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (isBn) "অনুশীলন শুরু" else "Start Quiz",
                    fontSize = 11.sp,
                    color = EmeraldLight
                )
            }
        }
    }
}

@Composable
fun AchievementsHorizontalList(
    achievements: List<AchievementEntity>,
    isBn: Boolean
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(achievements) { ach ->
            Card(
                modifier = Modifier
                    .width(140.dp)
                    .height(130.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (ach.isUnlocked) GoldLight else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (ach.isUnlocked) GoldBright else Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (ach.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = ach.titleEn,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBn) ach.titleBn else ach.titleEn,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (ach.isUnlocked) Color(0xFF78350F) else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (ach.isUnlocked) (if (isBn) "আনলকড ✓" else "Unlocked ✓") else (if (isBn) "লক করা" else "Locked"),
                        fontSize = 10.sp,
                        color = if (ach.isUnlocked) EmeraldPrimary else Color.Gray
                    )
                }
            }
        }
    }
}

fun getCategoryIcon(name: String): ImageVector {
    return when (name) {
        "menu_book" -> Icons.Default.MenuBook
        "auto_stories" -> Icons.Default.AutoStories
        "flag" -> Icons.Default.Flag
        "military_tech" -> Icons.Default.MilitaryTech
        "public" -> Icons.Default.Public
        "memory" -> Icons.Default.Memory
        "rocket_launch" -> Icons.Default.RocketLaunch
        "account_balance" -> Icons.Default.AccountBalance
        "calculate" -> Icons.Default.Calculate
        "sports_soccer" -> Icons.Default.SportsSoccer
        "translate" -> Icons.Default.Translate
        "forest" -> Icons.Default.Forest
        "newspaper" -> Icons.Default.Newspaper
        else -> Icons.Default.Psychology
    }
}
