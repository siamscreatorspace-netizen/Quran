package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserEntity
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.IlmVerseViewModel

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val level: Int,
    val coins: Int,
    val xp: Int,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    viewModel: IlmVerseViewModel,
    currentUser: UserEntity?,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        if (isBn) "সাপ্তাহিক" else "Weekly",
        if (isBn) "সর্বকালীন" else "All-Time"
    )

    // Generate competitive rankings including the active user
    val userCoins = currentUser?.coins ?: 100
    val userXp = currentUser?.xp ?: 50
    val userName = currentUser?.name ?: "মুসাফির"

    val baseLeaders = remember(userCoins, userXp, selectedTab) {
        val multiplier = if (selectedTab == 0) 1 else 3
        val simulated = listOf(
            LeaderboardUser(1, "আব্দুল্লাহ আল-আমিন", 5, 450 * multiplier, 780 * multiplier),
            LeaderboardUser(2, "ফাতিমা তুয-যোহরা", 4, 380 * multiplier, 650 * multiplier),
            LeaderboardUser(3, "মুহাম্মদ তানভীর", 4, 310 * multiplier, 520 * multiplier),
            LeaderboardUser(4, "মারিয়াম ইসলাম", 3, 260 * multiplier, 410 * multiplier),
            LeaderboardUser(5, "হাসান মাহমুদ", 3, 210 * multiplier, 340 * multiplier),
            LeaderboardUser(6, "নুসরাত জাহান", 2, 170 * multiplier, 280 * multiplier)
        )
        val all = (simulated + LeaderboardUser(0, userName, currentUser?.level ?: 1, userCoins, userXp, true))
            .sortedByDescending { it.coins }

        all.mapIndexed { idx, u -> u.copy(rank = idx + 1) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("leaderboard_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("leaderboard_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = if (isBn) "লিডারবোর্ড ও সেরা শিক্ষার্থী" else "Leaderboard & Top Scholars",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = EmeraldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = EmeraldPrimary
                )
            }
        ) {
            tabs.forEachIndexed { idx, title ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Top 3 Podium
        TopThreePodium(baseLeaders.take(3), isBn)

        // List of all ranks
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("leaderboard_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(baseLeaders) { _, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("leaderboard_item_${item.rank}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isCurrentUser) EmeraldContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (item.isCurrentUser) 3.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (item.rank) {
                                            1 -> GoldBright
                                            2 -> Color(0xFF94A3B8)
                                            3 -> Color(0xFFD97706)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.rank}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (item.rank <= 3) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.name,
                                        fontSize = 14.sp,
                                        fontWeight = if (item.isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (item.isCurrentUser) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(EmeraldPrimary)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (isBn) "আপনি" else "You",
                                                fontSize = 9.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${if (isBn) "স্তর" else "Level"} ${item.level} • ${item.xp} XP",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Coins
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = GoldBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${item.coins}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopThreePodium(leaders: List<LeaderboardUser>, isBn: Boolean) {
    if (leaders.size < 3) return

    val first = leaders[0]
    val second = leaders[1]
    val third = leaders[2]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd Place
        PodiumColumn(second, 2, Color(0xFF94A3B8), 85.dp)
        // 1st Place (Taller)
        PodiumColumn(first, 1, GoldBright, 110.dp)
        // 3rd Place
        PodiumColumn(third, 3, Color(0xFFD97706), 70.dp)
    }
}

@Composable
fun PodiumColumn(user: LeaderboardUser, rank: Int, badgeColor: Color, height: androidx.compose.ui.unit.Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Rank $rank",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.name.split(" ").firstOrNull() ?: user.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = "${user.coins} কয়েন",
            fontSize = 10.sp,
            color = EmeraldLight
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(badgeColor.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = badgeColor
            )
        }
    }
}
