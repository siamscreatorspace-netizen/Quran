package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LearningModeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuranAudioScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.IlmVerseViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                IlmVerseApp()
            }
        }
    }
}

@Composable
fun IlmVerseApp(viewModel: IlmVerseViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val todayChallenge by viewModel.todayChallenge.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val adminQuestions by viewModel.adminQuestions.collectAsStateWithLifecycle()
    val userHistory by viewModel.userHistory.collectAsStateWithLifecycle()
    val quizSessionState by viewModel.quizSessionState.collectAsStateWithLifecycle()
    val playerState by viewModel.audioPlayerState.collectAsStateWithLifecycle()

    val isBn = user?.language != "en"

    // System Back button handling
    BackHandler(enabled = currentScreen != Screen.Home) {
        viewModel.navigateBack()
    }

    val showBottomBar = currentScreen is Screen.Home ||
            currentScreen is Screen.QuranAudio ||
            currentScreen is Screen.Hadith ||
            currentScreen is Screen.Learning ||
            currentScreen is Screen.Leaderboard

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                IlmVerseBottomNavigation(
                    currentScreen = currentScreen,
                    isBn = isBn,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { target ->
                when (target) {
                    is Screen.Home -> HomeScreen(
                        viewModel = viewModel,
                        user = user,
                        challenge = todayChallenge,
                        achievements = achievements
                    )
                    is Screen.QuizSession -> QuizScreen(
                        viewModel = viewModel,
                        sessionState = quizSessionState,
                        isBn = isBn
                    )
                    is Screen.QuranAudio -> QuranAudioScreen(
                        viewModel = viewModel,
                        playerState = playerState,
                        isBn = isBn
                    )
                    is Screen.Hadith -> HadithScreen(
                        viewModel = viewModel,
                        isBn = isBn
                    )
                    is Screen.Learning -> LearningModeScreen(
                        viewModel = viewModel,
                        questions = allQuestions,
                        isBn = isBn
                    )
                    is Screen.History -> HistoryScreen(
                        viewModel = viewModel,
                        history = userHistory,
                        questions = allQuestions,
                        isBn = isBn
                    )
                    is Screen.Leaderboard -> LeaderboardScreen(
                        viewModel = viewModel,
                        currentUser = user,
                        isBn = isBn
                    )
                    is Screen.Achievements -> AchievementsScreen(
                        viewModel = viewModel,
                        achievements = achievements,
                        isBn = isBn
                    )
                    is Screen.Profile -> ProfileScreen(
                        viewModel = viewModel,
                        user = user,
                        bookmarks = bookmarks,
                        isBn = isBn
                    )
                    is Screen.Admin -> AdminScreen(
                        viewModel = viewModel,
                        questions = adminQuestions,
                        isBn = isBn
                    )
                }
            }
        }
    }
}

@Composable
fun IlmVerseBottomNavigation(
    currentScreen: Screen,
    isBn: Boolean,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("main_bottom_nav"),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = EmeraldPrimary
    ) {
        // Home
        NavigationBarItem(
            selected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text(if (isBn) "হোম" else "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldPrimary,
                indicatorColor = EmeraldContainer
            ),
            modifier = Modifier.testTag("nav_home")
        )

        // Quran Audio
        NavigationBarItem(
            selected = currentScreen is Screen.QuranAudio,
            onClick = { onNavigate(Screen.QuranAudio) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.QuranAudio) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                    contentDescription = "Quran Audio"
                )
            },
            label = { Text(if (isBn) "কুরআন" else "Quran") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldPrimary,
                indicatorColor = EmeraldContainer
            ),
            modifier = Modifier.testTag("nav_quran")
        )

        // Hadith
        NavigationBarItem(
            selected = currentScreen is Screen.Hadith,
            onClick = { onNavigate(Screen.Hadith) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Hadith) Icons.Filled.AutoStories else Icons.Outlined.AutoStories,
                    contentDescription = "Hadith"
                )
            },
            label = { Text(if (isBn) "হাদিস" else "Hadith") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldPrimary,
                indicatorColor = EmeraldContainer
            ),
            modifier = Modifier.testTag("nav_hadith")
        )

        // Learning Mode
        NavigationBarItem(
            selected = currentScreen is Screen.Learning,
            onClick = { onNavigate(Screen.Learning) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Learning) Icons.Filled.Lightbulb else Icons.Outlined.Lightbulb,
                    contentDescription = "Learning"
                )
            },
            label = { Text(if (isBn) "পাঠাগার" else "Study") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldPrimary,
                indicatorColor = EmeraldContainer
            ),
            modifier = Modifier.testTag("nav_learning")
        )

        // Leaderboard
        NavigationBarItem(
            selected = currentScreen is Screen.Leaderboard,
            onClick = { onNavigate(Screen.Leaderboard) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Leaderboard) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                    contentDescription = "Leaderboard"
                )
            },
            label = { Text(if (isBn) "সেরা" else "Ranks") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldPrimary,
                indicatorColor = EmeraldContainer
            ),
            modifier = Modifier.testTag("nav_leaderboard")
        )
    }
}
