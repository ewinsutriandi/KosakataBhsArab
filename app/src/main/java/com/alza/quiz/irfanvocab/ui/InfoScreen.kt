package com.alza.quiz.irfanvocab.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    navController: NavController,
    backRoute: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tentang aplikasi ini") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Paragraphs
            Text(
                "Aplikasi ini dibuat untuk Zahra dan Haidar, teman-temannya di SDIT NUFI, " +
                        "dan tentunya siapa saja yang membutuhkan. " +
                        "Semoga aplikasinya bisa membantu proses belajarnya biar jadi makin seru.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Yang kenal Zahra atau Haidar, kalau mau usul ngasi ide/saran " +
                        "atau apa saja yang mau disampaikan, " +
                        "bisa langsung hubungi mereka ya \uD83D\uDE0A.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Buat yang lain, bisa dengan mengirim email ke alza.interactive@gmail.com",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Back button
            Button(
                onClick = { navController.navigate(backRoute) }
            ) {
                Text("Kembali")
            }
        }
    }
}
