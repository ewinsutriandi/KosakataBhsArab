package com.alza.quiz.irfanvocab.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alza.quiz.irfanvocab.model.QuestionGenerator
import com.alza.quiz.irfanvocab.model.QuizLevel
import com.alza.quiz.irfanvocab.model.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(
    navController: NavController,
    viewModel: SharedQuizViewModel = viewModel()
) {
    val levels = listOf(
        "Level 1: 1-10",
        "Level 2: 11-50",
        "Level 3: 51-100",
        "Level 4: 101-1000",
        "Level 5: 1001-10000"
    )

    // Define number ranges for each level
    val ranges = listOf(
        1 to 10,
        11 to 50,
        51 to 100,
        101 to 1000,
        1001 to 10000
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select a Level") }
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val (rangeStart, rangeEnd) = ranges[index]
                            // Generate equal number of questions for each type (3 of each type, total 6)
                            val wordToNumberQuestions = QuestionGenerator.generateQuestions(
                                count = 3,
                                type = QuizQuestion.QuestionType.WORD_TO_NUMBER,
                                rangeStart = rangeStart,
                                rangeEnd = rangeEnd
                            )
                            val numberToWordQuestions = QuestionGenerator.generateQuestions(
                                count = 3,
                                type = QuizQuestion.QuestionType.NUMBER_TO_WORD,
                                rangeStart = rangeStart,
                                rangeEnd = rangeEnd
                            )
                            // Combine questions from both types
                            val questions = wordToNumberQuestions + numberToWordQuestions
                            // Store the generated QuizLevel in the ViewModel
                            viewModel.setQuizLevel(
                                QuizLevel(
                                    levelId = index + 1,
                                    questions = questions.shuffled(),
                                    totalQuestions = questions.size
                                )
                            )
                            // Navigate to QuizScreen
                            navController.navigate("quiz_screen")
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = levels[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}



class SharedQuizViewModel : ViewModel() {
    private val _quizLevel = MutableStateFlow<QuizLevel?>(null)
    val quizLevel: StateFlow<QuizLevel?> = _quizLevel
    val instanceId = UUID.randomUUID().toString()

    fun setQuizLevel(level: QuizLevel) {
        println("SharedQuizViewModel $instanceId set QuizLevel: $level")
        _quizLevel.value = level
    }
}