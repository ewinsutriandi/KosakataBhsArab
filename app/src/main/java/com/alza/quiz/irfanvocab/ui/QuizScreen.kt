package com.alza.quiz.irfanvocab.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onQuizCompleted: (Int, Int, Int) -> Unit,
    viewModel: SharedQuizViewModel
) {
    val quizLevel by viewModel.quizLevel.collectAsState()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarBgColor by remember { mutableStateOf(Color.Gray) }
    var snackbarFgColor by remember { mutableStateOf(Color.White) }
    val scope = rememberCoroutineScope()

    quizLevel?.let { level ->
        if (currentQuestionIndex >= level.questions.size) {
            onQuizCompleted(level.levelId, correctAnswers, incorrectAnswers)
            return@let
        }

        val currentQuestion = level.questions[currentQuestionIndex]

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Level ${level.levelId} - Question ${currentQuestionIndex + 1}/${level.totalQuestions}") }
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = snackbarBgColor,
                        contentColor = snackbarFgColor
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Question display with fade animation
                AnimatedContent(
                    targetState = currentQuestionIndex,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) { targetIndex ->
                    Card {
                        Text(
                            text = level.questions[targetIndex].questionText,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 51.sp), // Larger font
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        )
                    }
                }

                // Choices with fade animation
                AnimatedContent(
                    targetState = currentQuestionIndex,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { targetIndex ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        level.questions[targetIndex].choices.forEach { choice ->
                            Button(
                                onClick = {
                                    val isCorrect = choice == currentQuestion.correctAnswer
                                    if (isCorrect) {
                                        correctAnswers++
                                        snackbarBgColor = Color.Green
                                        snackbarFgColor = Color.Black
                                    } else {
                                        incorrectAnswers++
                                        snackbarBgColor = Color.Red
                                        snackbarFgColor = Color.White
                                    }
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = if (isCorrect) "Correct!" else "Incorrect!"
                                        )
                                    }
                                    currentQuestionIndex++
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = choice,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 27.sp), // Larger font
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Progress indicator
                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / level.totalQuestions },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )

                // Score display
                Text(
                    text = "Correct: $correctAnswers | Incorrect: $incorrectAnswers",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No quiz level selected", style = MaterialTheme.typography.bodyLarge)
        }
    }
}