package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionEntity
import com.example.data.model.QuizCategory
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.IlmVerseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: IlmVerseViewModel,
    questions: List<QuestionEntity>,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        if (isBn) "পরিসংখ্যান ও প্রশ্নসমূহ" else "Stats & Bank",
        if (isBn) "নতুন প্রশ্ন যুক্ত করুন" else "Add Question"
    )

    // Form fields for adding question
    var selectedCategory by remember { mutableStateOf(QuizCategory.QURAN.id) }
    var expandedCatDropdown by remember { mutableStateOf(false) }
    var questionTextBn by remember { mutableStateOf("") }
    var questionTextEn by remember { mutableStateOf("") }
    var optionABn by remember { mutableStateOf("") }
    var optionBBn by remember { mutableStateOf("") }
    var optionCBn by remember { mutableStateOf("") }
    var optionDBn by remember { mutableStateOf("") }
    var correctIndex by remember { mutableStateOf(0) }
    var explanationBn by remember { mutableStateOf("") }
    var arabicText by remember { mutableStateOf("") }
    var sourceRef by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("admin_screen")
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
                modifier = Modifier.testTag("admin_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = if (isBn) "অ্যাডমিন প্যানেল ও প্রশ্ন ব্যাংক" else "Admin Dashboard & Question Bank",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = EmeraldPrimary
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

        if (selectedTab == 0) {
            // Stats & Question list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("admin_stats_view"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // System Stats Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Admin",
                                    tint = EmeraldPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) "সিস্টেম সারসংক্ষেপ" else "System Overview",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = EmeraldDark
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "${questions.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmeraldDark)
                                    Text(text = if (isBn) "মোট প্রশ্ন" else "Total Questions", fontSize = 11.sp, color = EmeraldDark.copy(alpha = 0.7f))
                                }
                                Column {
                                    Text(text = "${QuizCategory.values().size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                    Text(text = if (isBn) "মোট বিষয়" else "Categories", fontSize = 11.sp, color = EmeraldDark.copy(alpha = 0.7f))
                                }
                                Column {
                                    Text(text = "+10 / +20", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GoldBright)
                                    Text(text = if (isBn) "কয়েন পুরষ্কার" else "Coin Reward", fontSize = 11.sp, color = EmeraldDark.copy(alpha = 0.7f))
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = if (isBn) "প্রশ্ন ব্যাংকে সংরক্ষিত প্রশ্নসমূহ (${questions.size})" else "Active Question Bank (${questions.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                items(questions) { q ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldContainer)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = q.category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                }
                                Text(
                                    text = "ID: ${q.id}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isBn) q.questionBn else q.questionEn,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "উৎস: ${q.referenceSource}",
                                fontSize = 11.sp,
                                color = EmeraldLight
                            )
                        }
                    }
                }
            }
        } else {
            // Add Question Form
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("admin_add_question_form"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = if (isBn) "যাচাইকৃত নতুন প্রশ্ন যোগ করুন" else "Add Verified Question",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Text(
                        text = if (isBn) "সিস্টেম স্বয়ংক্রিয়ভাবে প্রশ্নটি ডুপ্লিকেট কিনা তা পরীক্ষা করবে।" else "The system will check normalized fingerprints to prevent duplicates.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Category selector
                item {
                    ExposedDropdownMenuBox(
                        expanded = expandedCatDropdown,
                        onExpandedChange = { expandedCatDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isBn) "বিভাগ" else "Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCatDropdown,
                            onDismissRequest = { expandedCatDropdown = false }
                        ) {
                            QuizCategory.values().forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.titleBn) },
                                    onClick = {
                                        selectedCategory = cat.id
                                        expandedCatDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Question text
                item {
                    OutlinedTextField(
                        value = questionTextBn,
                        onValueChange = { questionTextBn = it },
                        label = { Text(if (isBn) "প্রশ্নের বিবরণ (বাংলা)" else "Question (Bengali)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Arabic Text (optional)
                item {
                    OutlinedTextField(
                        value = arabicText,
                        onValueChange = { arabicText = it },
                        label = { Text(if (isBn) "মূল আরবি আয়াত/হাদিস (প্রযোজ্য ক্ষেত্রে)" else "Arabic Text (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 4 Options
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (isBn) "৪টি উত্তর অপশন (রেডিও বাটন দিয়ে সঠিক উত্তর চিহ্নিত করুন):" else "4 Options (Select correct via radio):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = correctIndex == 0, onClick = { correctIndex = 0 })
                            OutlinedTextField(value = optionABn, onValueChange = { optionABn = it }, label = { Text("অপশন ক (A)") }, modifier = Modifier.weight(1f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = correctIndex == 1, onClick = { correctIndex = 1 })
                            OutlinedTextField(value = optionBBn, onValueChange = { optionBBn = it }, label = { Text("অপশন খ (B)") }, modifier = Modifier.weight(1f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = correctIndex == 2, onClick = { correctIndex = 2 })
                            OutlinedTextField(value = optionCBn, onValueChange = { optionCBn = it }, label = { Text("অপশন গ (C)") }, modifier = Modifier.weight(1f))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = correctIndex == 3, onClick = { correctIndex = 3 })
                            OutlinedTextField(value = optionDBn, onValueChange = { optionDBn = it }, label = { Text("অপশন ঘ (D)") }, modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Explanation
                item {
                    OutlinedTextField(
                        value = explanationBn,
                        onValueChange = { explanationBn = it },
                        label = { Text(if (isBn) "ব্যাখ্যা / তাফসীর / তথ্যসূত্র" else "Explanation & Source Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Reference
                item {
                    OutlinedTextField(
                        value = sourceRef,
                        onValueChange = { sourceRef = it },
                        label = { Text(if (isBn) "সুনির্দিষ্ট রেফারেন্স (যেমন: সূরা বাকারা ২:২৫৫)" else "Exact Reference Source") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Submit Button
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (questionTextBn.isBlank() || optionABn.isBlank() || optionBBn.isBlank() || sourceRef.isBlank()) {
                                Toast.makeText(context, "দয়া করে সকল প্রয়োজনীয় তথ্য পূরণ করুন।", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.addAdminQuestion(
                                category = selectedCategory,
                                questionBn = questionTextBn,
                                questionEn = questionTextEn,
                                optABn = optionABn,
                                optBBn = optionBBn,
                                optCBn = optionCBn,
                                optDBn = optionDBn,
                                correctIdx = correctIndex,
                                explanationBn = explanationBn,
                                explanationEn = explanationBn,
                                arabic = arabicText,
                                ref = sourceRef
                            ) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                if (success) {
                                    questionTextBn = ""
                                    optionABn = ""
                                    optionBBn = ""
                                    optionCBn = ""
                                    optionDBn = ""
                                    explanationBn = ""
                                    arabicText = ""
                                    sourceRef = ""
                                    selectedTab = 0
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("admin_submit_question_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "যাচাই করুন ও অনুমোদন দিন" else "Verify & Approve Question",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}
