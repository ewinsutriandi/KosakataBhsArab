package com.alza.quiz.irfanvocab.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alza.quiz.irfanvocab.R
import com.alza.quiz.irfanvocab.model.ExerciseModel
import com.alza.quiz.irfanvocab.model.TransDirection
import com.alza.quiz.irfanvocab.model.loadAndGenerateQuiz
import com.alza.quiz.irfanvocab.ui.theme.Primary
import com.alza.quiz.irfanvocab.ui.theme.PrimaryContainer

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: SharedQuizViewModel
) {
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            NavigationBar(
                //containerColor = Color(0xFFFFF176),
                //contentColor = Color.Black
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                    label = { Text("Tentang Aplikasi") },
                    selected = false,
                    onClick = {  }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = "Contact") },
                    label = { Text("Kontak & Saran") },
                    selected = false,
                    onClick = {  }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Contact") },
                    label = { Text("Lainnya") },
                    selected = false,
                    onClick = {  }
                )

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color(0xFFB2EBF2))
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Belajar Kosakata Bahasa Arab",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                    //color = Color(0xFFFF4081),
                    //fontFamily = FontFamily.Cursive
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    listOf(
                        ExerciseModel.ExerciseType.NUMBERS,
                        ExerciseModel.ExerciseType.ANIMALS,
                        ExerciseModel.ExerciseType.COLORS,
                        ExerciseModel.ExerciseType.FRUITS,
                        ExerciseModel.ExerciseType.FAMILY,
                    )
                ) { exerciseType ->
                    VocabularyButton(
                        exerciseType = exerciseType,
                        onClick = {
                            when (exerciseType){
                                ExerciseModel.ExerciseType.NUMBERS -> navController.navigate("level_selection")
                                else -> {
                                    val arToIndQuestions = loadAndGenerateQuiz(
                                        direction = TransDirection.ARABIC_TO_INDONESIAN,
                                        context = context,
                                        exerciseType = exerciseType,
                                        numQuestions = 5
                                    )
                                    val indToArQuestions = loadAndGenerateQuiz(
                                        direction = TransDirection.INDONESIAN_TO_ARABIC,
                                        context = context,
                                        exerciseType = exerciseType,
                                        numQuestions = 5
                                    )
                                    val questions = indToArQuestions + arToIndQuestions
                                    // Store the generated QuizLevel in the ViewModel
                                    viewModel.setQuizLevel(
                                        ExerciseModel(
                                            levelId = 1,
                                            questions = questions.shuffled(),
                                            exerciseType = exerciseType
                                            //totalQuestions = questions.size
                                        ),
                                        title = "Kosakata"
                                    )
                                    // Navigate to QuizScreen
                                    navController.navigate("quiz_screen")

                                }

                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VocabularyButton(exerciseType: ExerciseModel.ExerciseType,
                     onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            /*
            .background(
                color = Primary,
                shape = RoundedCornerShape(16.dp)
            )*/
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = when (exerciseType) {
                ExerciseModel.ExerciseType.NUMBERS -> painterResource(id = R.drawable.icon_numbers)
                ExerciseModel.ExerciseType.ANIMALS -> painterResource(id = R.drawable.icon_animals)
                ExerciseModel.ExerciseType.FRUITS -> painterResource(id = R.drawable.icon_fruits)
                ExerciseModel.ExerciseType.COLORS -> painterResource(id = R.drawable.icon_colors)
                ExerciseModel.ExerciseType.FAMILY -> painterResource(id = R.drawable.icon_family)
                else -> painterResource(id = R.drawable.icon_animals)
            },

            contentDescription = exerciseType.name,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryContainer)
                .padding(8.dp)
        )
        Text(
            text = when (exerciseType) {
                ExerciseModel.ExerciseType.NUMBERS -> "Bilangan"
                ExerciseModel.ExerciseType.ANIMALS -> "Hewan"
                ExerciseModel.ExerciseType.APPARELS -> "Pakaian"
                ExerciseModel.ExerciseType.COLORS -> "Warna"
                ExerciseModel.ExerciseType.FRUITS -> "Buah"
                ExerciseModel.ExerciseType.FAMILY -> "Keluarga"
                else -> exerciseType.name.lowercase().replaceFirstChar { it.titlecase() }
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                //color = Color(0xFF0288D1),
                //fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
