package com.alza.quiz.irfanvocab.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopyrightScreen(
    navController: NavController,
    backRoute: String
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Source code & lisensi") }
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
                "Perhatian! \nHalaman ini khusus bagi mereka yang tertarik " +
                        "belajar pengembangan aplikasi pada perangkat Android. " +
                        "Abaikan halaman ini jika ngerasa banyak istilah aneh di sini \uD83D\uDE0A.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Aplikasi ini dibuat menggunakan Kotlin dan Jetpack Compose, " +
                        "dengan level kode yang sesuai untuk pemula, " +
                        "sehingga cocok untuk mereka yang baru memulai belajar " +
                        "native android app development. " +
                        "Praktik Modern Android Development yang digunakan dalam aplikasi ini " +
                        "antara lain MVVM, modern navigation, state management, theming dengan Material 3, " +
                        "simple data persistence, dan resource management. ",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Kode aplikasi ini dirilis dengan lisensi opensource (MIT), " +
                        "dan dapat diakses pada github dengan menekan tombol di bawah ini.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        val url = "https://github.com/ewinsutriandi/KosakataBhsArab"
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                ) {
                    Text("Kode (github)")
                }

            }
            // Back button

        }
    }
}
