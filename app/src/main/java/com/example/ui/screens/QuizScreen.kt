package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionEntity
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.IlmVerseViewModel
import com.example.ui.viewmodel.QuizSessionState

@Composable
fun QuizScreen(
    viewModel: IlmVerseViewModel,
    sessionState: QuizSessionState,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    if (sessionState.isCompleted) {
        QuizCompletionView(
            sessionState = sessionState,
            isBn = isBn,
            onPlayAgain = { viewModel.startQuiz(sessionState.category) },
            onHome = { viewModel.navigateBack() }
        )
        return
    }

    if (sessionState.questions.isEmpty()) {
        QuizEmptyBankView(
            category = sessionState.category,
            isBn = isBn,
            onBack = { viewModel.navigateBack() }
        )
        return
    }

    val currentQ = sessionState.questions.getOrNull(sessionState.currentIndex) ?: return
    val totalCount = sessionState.questions.size
    val currentNum = sessionState.currentIndex + 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("quiz_session_screen")
    ) {
        // Quiz Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("quiz_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            // Streak and Session Coins
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (sessionState.consecutiveStreak > 1) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = Color(0xFFEA580C),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${sessionState.consecutiveStreak} ${if (isBn) "ধারাবাহিক" else "Streak"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = GoldBright,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${sessionState.sessionCoinsEarned}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = EmeraldDark
                    )
                }
            }

            // Bookmark Action
            IconButton(
                onClick = {
                    viewModel.toggleBookmark(
                        type = "QUESTION",
                        title = if (isBn) currentQ.questionBn else currentQ.questionEn,
                        subtitle = currentQ.category,
                        arabic = currentQ.arabicText,
                        reference = currentQ.referenceSource
                    )
                },
                modifier = Modifier.testTag("quiz_bookmark_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = EmeraldPrimary
                )
            }
        }

        // Progress Bar & Timer
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "প্রশ্ন $currentNum / $totalCount" else "Question $currentNum of $totalCount",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = if (sessionState.timeRemainingSeconds <= 5) ErrorRed else EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${sessionState.timeRemainingSeconds}s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (sessionState.timeRemainingSeconds <= 5) ErrorRed else EmeraldPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { currentNum.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EmeraldPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            // Arabic Text if available (with RTL direction)
            if (!currentQ.arabicText.isNullOrBlank()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldContainer.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = currentQ.arabicText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldDark,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

            // Question Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (isBn) currentQ.questionBn else currentQ.questionEn,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "উৎস: ${currentQ.referenceSource}",
                            fontSize = 11.sp,
                            color = EmeraldLight
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // 4 Answer Options
            val optionsBn = listOf(currentQ.optionABn, currentQ.optionBBn, currentQ.optionCBn, currentQ.optionDBn)
            val optionsEn = listOf(currentQ.optionAEn, currentQ.optionBEn, currentQ.optionCEn, currentQ.optionDEn)
            val options = if (isBn) optionsBn else optionsEn
            val labels = listOf("A", "B", "C", "D")

            items(options.size) { idx ->
                val optionText = options[idx]
                val label = labels[idx]
                val isSelected = sessionState.selectedOptionIndex == idx
                val isCorrectAnswer = idx == currentQ.correctIndex

                val borderColor by animateColorAsState(
                    targetValue = when {
                        sessionState.isSubmitted && isCorrectAnswer -> SuccessGreen
                        sessionState.isSubmitted && isSelected && !isCorrectAnswer -> ErrorRed
                        isSelected -> EmeraldPrimary
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    },
                    label = "border"
                )

                val backgroundColor by animateColorAsState(
                    targetValue = when {
                        sessionState.isSubmitted && isCorrectAnswer -> SuccessGreen.copy(alpha = 0.15f)
                        sessionState.isSubmitted && isSelected && !isCorrectAnswer -> ErrorRed.copy(alpha = 0.15f)
                        isSelected -> EmeraldContainer.copy(alpha = 0.5f)
                        else -> MaterialTheme.colorScheme.surface
                    },
                    label = "bg"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                        .clickable(enabled = !sessionState.isSubmitted) {
                            viewModel.selectOption(idx)
                        }
                        .testTag("quiz_option_$idx"),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        sessionState.isSubmitted && isCorrectAnswer -> SuccessGreen
                                        sessionState.isSubmitted && isSelected -> ErrorRed
                                        isSelected -> EmeraldPrimary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected || (sessionState.isSubmitted && isCorrectAnswer)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = optionText,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected || (sessionState.isSubmitted && isCorrectAnswer)) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (sessionState.isSubmitted) {
                            if (isCorrectAnswer) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = SuccessGreen
                                )
                            } else if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Incorrect",
                                    tint = ErrorRed
                                )
                            }
                        }
                    }
                }
            }

            // Explanation & Result Box (after submission)
            item {
                AnimatedVisibility(
                    visible = sessionState.isSubmitted,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (sessionState.isCorrect) EmeraldContainer else Color(0xFFFFE4E6)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (sessionState.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                        contentDescription = "Status",
                                        tint = if (sessionState.isCorrect) EmeraldPrimary else ErrorRed
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (sessionState.isCorrect) (if (isBn) "মাশাআল্লাহ! সঠিক উত্তর (+১০ কয়েন)" else "Correct! (+10 Coins)") else (if (isBn) "ভুল উত্তর! সঠিকটি জেনে নিন" else "Incorrect! See verified answer"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (sessionState.isCorrect) EmeraldDark else Color(0xFF9F1239)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isBn) currentQ.explanationBn else currentQ.explanationEn,
                                    fontSize = 13.sp,
                                    color = if (sessionState.isCorrect) EmeraldDark else Color(0xFF881337),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.nextQuestion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("quiz_next_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (isBn) "পরবর্তী প্রশ্ন →" else "Next Question →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Submit Button before submission
            item {
                if (!sessionState.isSubmitted) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.submitAnswer() },
                        enabled = sessionState.selectedOptionIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("quiz_submit_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isBn) "উত্তর নিশ্চিত করুন" else "Confirm Answer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun QuizCompletionView(
    sessionState: QuizSessionState,
    isBn: Boolean,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(GoldBright),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isBn) "কুইজ সম্পন্ন হয়েছে!" else "Quiz Completed!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (isBn) "জ্ঞানার্জনে এগিয়ে থাকার জন্য অভিনন্দন!" else "Congratulations on expanding your knowledge!",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(EmeraldContainer)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${sessionState.sessionCoinsEarned}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark
                        )
                        Text(
                            text = if (isBn) "অর্জিত কয়েন" else "Coins Earned",
                            fontSize = 12.sp,
                            color = EmeraldDark.copy(alpha = 0.8f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${sessionState.sessionXpEarned}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = if (isBn) "অর্জিত XP" else "XP Earned",
                            fontSize = 12.sp,
                            color = EmeraldDark.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("play_again_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isBn) "পুনরায় খেলুন" else "Play Again",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("back_to_home_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isBn) "হোমে ফিরে যান" else "Return to Home",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuizEmptyBankView(
    category: String,
    isBn: Boolean,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = EmeraldPrimary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isBn) "এই বিভাগের সমস্ত নতুন প্রশ্ন সম্পন্ন!" else "All Unseen Questions Completed!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isBn) "আপনি এই বিভাগের সকল প্রশ্ন অনুশীলন করে ফেলেছেন। ইতিহাস পর্যালোচনা করুন অথবা অন্যান্য বিভাগ খেলুন।" else "You have answered all unique questions in this category. Review history or play other categories.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (isBn) "ফিরে যান" else "Go Back")
            }
        }
    }
}
