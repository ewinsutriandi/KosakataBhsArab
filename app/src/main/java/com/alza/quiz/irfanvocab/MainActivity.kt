package com.alza.quiz.irfanvocab

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alza.quiz.irfanvocab.model.ExerciseModel
import com.alza.quiz.irfanvocab.ui.CopyrightScreen
import com.alza.quiz.irfanvocab.ui.InfoScreen
import com.alza.quiz.irfanvocab.ui.LevelSelectionScreen
import com.alza.quiz.irfanvocab.ui.MainScreen
import com.alza.quiz.irfanvocab.ui.MoreAppsScreen
import com.alza.quiz.irfanvocab.ui.QuizScreen
import com.alza.quiz.irfanvocab.ui.SharedQuizViewModel
import com.alza.quiz.irfanvocab.ui.theme.BelajarKosakataArabTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BelajarKosakataArabTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: SharedQuizViewModel = viewModel()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable("info") {
            InfoScreen(navController = navController, backRoute = "main")
        }
        composable("copy") {
            CopyrightScreen(navController = navController, backRoute = "main")
        }
        composable("store") {
            MoreAppsScreen(navController = navController, backRoute = "main")
        }
        composable("level_selection") {
            LevelSelectionScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable("quiz_screen") {
            QuizScreen(
                onQuizCompleted = { exerciseType ->
                    when (exerciseType) {
                        ExerciseModel.ExerciseType.NUMBERS -> navController.navigate("level_selection")
                        else -> navController.navigate("main")
                    }
                },
                viewModel = sharedViewModel
            )
        }
    }
}

