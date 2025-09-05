package com.alza.quiz.irfanvocab.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onQuizCompleted: () -> Unit,
    viewModel: SharedQuizViewModel
) {
    val quizLevel by viewModel.quizLevel.collectAsState()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showSummaryDialog by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    quizLevel?.let { level ->
        // Show summary dialog if quiz is complete
        if (currentQuestionIndex >= level.questions.size) {
            showSummaryDialog = true
        }

        // Summary dialog
        if (showSummaryDialog) {
            AlertDialog(
                onDismissRequest = { /* Prevent dismissal by clicking outside */ },
                title = {
                    Text(
                        text = "${((correctAnswers.toFloat() / level.totalQuestions) * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Benar: $correctAnswers soal",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Salah: $incorrectAnswers soal",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSummaryDialog = false
                            onQuizCompleted()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ke daftar level")
                    }
                },
                dismissButton = {}
            )
            return@let
        }

        // Incorrect feedback dialog
        if (showFeedbackDialog) {
            AlertDialog(
                onDismissRequest = { showFeedbackDialog = false },
                title = {
                    Text(
                        text = "Salah!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = feedbackMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showFeedbackDialog = false
                            currentQuestionIndex++
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Lanjut")
                    }
                },
                dismissButton = {}
            )
            return@let
        }

        // Quiz UI (shown only if quiz is not complete)
        val currentQuestion = level.questions[currentQuestionIndex]

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Level ${level.levelId} - Question ${currentQuestionIndex + 1}/${level.totalQuestions}") }
                )
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
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 51.sp),
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
                                        currentQuestionIndex++
                                    } else {
                                        incorrectAnswers++
                                        feedbackMessage = "${currentQuestion.questionText} = ${currentQuestion.correctAnswer}"
                                        showFeedbackDialog = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = choice,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 27.sp),
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
                    text = "Benar: $correctAnswers | Salah: $incorrectAnswers",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Close button
                IconButton(
                    onClick = { showExitDialog = true },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Quiz",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // Exit confirmation dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Hentikan sesi") },
                text = { Text("Yakin ingin keluar dan menghentikan sesi permainan kali ini?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showExitDialog = false
                            onQuizCompleted()
                        }
                    ) {
                        Text("Keluar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showExitDialog = false }
                    ) {
                        Text("Batal")
                    }
                }
            )
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