package com.alza.quiz.irfanvocab.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    navController: NavController,
    backRoute: String
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tentang aplikasi ini") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("main")
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
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
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = ("mailto:alza.interactive@gmail.com?" +
                                "&subject=" + Uri.encode("Feedback: Kosakata bahasa arab") +
                                "&body=" + Uri.encode("ganti teks ini dengan pesan anda")).toUri()
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context,"Perangkat anda tidak memiliki email client",
                            Toast.LENGTH_LONG).show();
                        println(e.message)
                    }
                }
            ) {
                Text("Kirim email")
            }
        }
    }
}
