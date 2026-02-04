package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(viewModel: MeshViewModel = viewModel()) {
    val isRecording by viewModel.isRecording.collectAsState()
    val isMeshActive by viewModel.isMeshActive.collectAsState()
    val isSirenOn by viewModel.isSirenOn.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Status Bar
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mesh: Aktif", color = Color.Green, style = MaterialTheme.typography.titleMedium)
                    Text("Cihazlar: 5", style = MaterialTheme.typography.bodySmall)
                }
                Text("Pil: %85", style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // PTT Button
        Box(
            modifier = Modifier
                .size(240.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            try {
                                viewModel.startPTT()
                                tryAwaitRelease()
                            } finally {
                                viewModel.stopPTT()
                            }
                        }
                    )
                }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer ring
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = if (isRecording) Color.Red.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.1f),
            ) {}

            // Inner button
            Surface(
                modifier = Modifier.size(180.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = if (isRecording) Color(0xFFD32F2F) else Color(0xFF424242),
                shadowElevation = 12.dp,
                tonalElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (isRecording) "KAYDEDİLİYOR" else "BAS KONUŞ",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Quick Tools
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { viewModel.toggleSiren() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isSirenOn) Color.Red else Color.White)
            ) {
                Text(if (isSirenOn) "SİREN AÇIK" else "SİREN")
            }
            OutlinedButton(
                onClick = { viewModel.toggleFlashlight() },
                modifier = Modifier.weight(1f)
            ) {
                Text("FENER")
            }
        }

        // Emergency Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Intent to dial 112/911 */ },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Text("ACİL ÇAĞRI (112)", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.sendEmergencySms("YARDIM! Deprem bölgesindeyim. Durumum: İYİ.") },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
            ) {
                Text("REHBERE YARDIM SMS'İ GÖNDER", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
