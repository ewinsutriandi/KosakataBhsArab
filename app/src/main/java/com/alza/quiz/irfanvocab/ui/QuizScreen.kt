package com.alza.quiz.irfanvocab.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alza.quiz.irfanvocab.model.ExerciseModel
import com.alza.quiz.irfanvocab.ui.theme.OnErrorContainer
import com.alza.quiz.irfanvocab.util.SoundUtil
import com.alza.quiz.irfanvocab.util.StatsUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onQuizCompleted: (exerciseType: ExerciseModel.ExerciseType) -> Unit,
    viewModel: SharedQuizViewModel
) {
    val context = LocalContext.current
    val quizLevel by viewModel.exerciseModel.collectAsState()
    val levelTitle by viewModel.levelTitle.collectAsState()
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
                        text = "${((correctAnswers.toFloat() / level.questions.size) * 100).toInt()}%",
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
                            // Save stats before navigating back
                            StatsUtil.saveStats(context,level.exerciseType, level.levelId, correctAnswers, incorrectAnswers)
                            showSummaryDialog = false
                            onQuizCompleted(level.exerciseType)
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
                        text = "Jawaban salah",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnErrorContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = feedbackMessage,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
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
                    title = { Text(levelTitle ?: "Level ${level.levelId}") }
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
                                        SoundUtil.playCorrectSound(context)
                                        currentQuestionIndex++
                                    } else {
                                        incorrectAnswers++
                                        SoundUtil.playWrongSound(context)
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
                    progress = { (currentQuestionIndex + 1).toFloat() / level.questions.size },
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
                            onQuizCompleted(level.exerciseType)
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