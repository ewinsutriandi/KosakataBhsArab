package com.alza.quiz.irfanvocab.ui

import android.content.ActivityNotFoundException
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun MoreAppsScreen(
    navController: NavController,
    backRoute: String
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aplikasi lain") }
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
                "Selain mengembangkan aplikasi ini, kita punya banyak aplikasi lain " +
                        "yang tersedia baik di Play Store (Android) maupun App Store (iOS). \n" +
                        "Kebanyakan sih aplikasi buat bantu belajar matematika. \n" +
                        "Antara lain seperti bilangan bulat, pecahan, aljabar, bangun datar, " +
                        "dan masih banyak lagi lainnya.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Kalau penasaran cek saja link di bawah ya.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        val devId ="Alza+Interactive"
                        // Try Play Store app first
                        val playStoreIntent = Intent(
                            Intent.ACTION_VIEW,
                            "market://dev?id=$devId".toUri()
                        )

                        // Fallback to web if Play Store not available
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            "https://play.google.com/store/apps/developer?id=$devId".toUri()
                        )

                        try {
                            context.startActivity(playStoreIntent)
                        } catch (e: ActivityNotFoundException) {
                            context.startActivity(webIntent)
                        }
                    }
                ) {
                    Text("Play Store")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val url = "https://apps.apple.com/developer/id1791663083"
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                ) {
                    Text("App Store")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { navController.navigate(backRoute) }
                ) {
                    Text("Kembali")
                }
            }
            // Back button

        }
    }
}
