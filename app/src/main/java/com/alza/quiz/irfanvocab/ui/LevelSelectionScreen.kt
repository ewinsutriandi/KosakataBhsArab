package com.alza.quiz.irfanvocab.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alza.quiz.irfanvocab.model.QuestionGenerator
import com.alza.quiz.irfanvocab.model.QuizLevel
import com.alza.quiz.irfanvocab.model.QuizQuestion
import com.alza.quiz.irfanvocab.util.StatsUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(
    navController: NavController,
    viewModel: SharedQuizViewModel = viewModel()
) {
    val levels = listOf(
        "Level 1: Bilangan 1-10",
        "Level 2: Bilangan 11-19",
        "Level 2: Bilangan 20-50",
        "Level 3: Bilangan 51-100",
        "Level 4: Bilangan 101-1000",
        "Level 5: Bilangan 1001-10000"
    )

    // Define number ranges for each level
    val ranges = listOf(
        1 to 10,
        11 to 19,
        20 to 50,
        51 to 100,
        101 to 1000,
        1001 to 10000
    )


    // Retrieve stats
    val context = LocalContext.current
    val stats = StatsUtil.getStats(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pilih Level") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(levels.size) { index ->
                // Calculate stats for the current level
                val levelId = index + 1
                val levelStats = stats.filter { it.levelId == levelId }
                val playCount = levelStats.size
                val totalCorrect = levelStats.sumOf { it.correctAnswers }
                val totalIncorrect = levelStats.sumOf { it.incorrectAnswers }
                val totalAnswers = totalCorrect + totalIncorrect
                val accuracy = if (totalAnswers > 0) {
                    ((totalCorrect.toFloat() / totalAnswers) * 100).roundToInt()
                } else {
                    0
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val (rangeStart, rangeEnd) = ranges[index]
                            // Generate equal number of questions for each type (3 of each type, total 6)
                            val wordToNumberQuestions = QuestionGenerator.generateQuestions(
                                count = 4,
                                type = QuizQuestion.QuestionType.WORD_TO_NUMBER,
                                rangeStart = rangeStart,
                                rangeEnd = rangeEnd
                            )
                            val numberToWordQuestions = QuestionGenerator.generateQuestions(
                                count = 4,
                                type = QuizQuestion.QuestionType.NUMBER_TO_WORD,
                                rangeStart = rangeStart,
                                rangeEnd = rangeEnd
                            )
                            val regularToArabicQuestions = QuestionGenerator.generateQuestions(
                                count = 4,
                                type = QuizQuestion.QuestionType.REGULAR_TO_ARABIC_NUMBER,
                                rangeStart = rangeStart,
                                rangeEnd = rangeEnd
                            )
                            val arabicToRegularQuestions = QuestionGenerator.generateQuestions(
                                count = 4,
                                type = QuizQuestion.QuestionType.ARABIC_TO_REGULAR_NUMBER,
                                rangeStart = rangeStart,
                                rangeEnd = rangeEnd
                            )
                            // Combine questions from all types
                            val questions = regularToArabicQuestions + arabicToRegularQuestions + wordToNumberQuestions + numberToWordQuestions
                            // Store the generated QuizLevel in the ViewModel
                            viewModel.setQuizLevel(
                                QuizLevel(
                                    levelId = index + 1,
                                    questions = questions.shuffled(),
                                    totalQuestions = questions.size
                                ),
                                title = levels[index]
                            )
                            // Navigate to QuizScreen
                            navController.navigate("quiz_screen")
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = levels[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,

                        ) {
                            Text(
                                text = "Main: $playCount",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Benar: $totalCorrect",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Salah: $totalIncorrect",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Akurasi: $accuracy%",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}



class SharedQuizViewModel : ViewModel() {
    private val _quizLevel = MutableStateFlow<QuizLevel?>(null)
    val quizLevel: StateFlow<QuizLevel?> = _quizLevel
    private val _levelTitle = MutableStateFlow<String?>(null)
    val levelTitle: StateFlow<String?> = _levelTitle

    val instanceId = UUID.randomUUID().toString()

    fun setQuizLevel(level: QuizLevel, title: String) {
        println("SharedQuizViewModel $instanceId set QuizLevel: $level, Title: $title")
        _quizLevel.value = level
        _levelTitle.value = title
    }
}