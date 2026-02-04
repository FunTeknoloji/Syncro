package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Status Bar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Mesh: Online", color = Color.Green)
            Text("Nodes: 5")
            Text("Battery: 85%")
        }

        // PTT Button
        Box(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            viewModel.startPTT()
                            tryAwaitRelease()
                            viewModel.stopPTT()
                        }
                    )
                }
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = if (isRecording) Color.Red else Color.DarkGray,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(if (isRecording) "RECORDING" else "PUSH TO TALK", fontSize = 20.sp, color = Color.White)
                }
            }
        }

        // Emergency Tools (Quick Access)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { viewModel.toggleSiren() },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSirenOn) Color.Black else Color.Red)
            ) {
                Text(if (isSirenOn) "STOP SIREN" else "SIREN")
            }
            Button(onClick = { viewModel.toggleFlashlight() }) {
                Text("FLASHLIGHT")
            }
        }

        // Emergency Actions
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { /* Intent to dial 112/911 */ },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Text("EMERGENCY CALL (112)", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.sendEmergencySms("HELP! I am in an earthquake zone. My status is: OK.") },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
            ) {
                Text("SEND HELP SMS TO CONTACTS", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
