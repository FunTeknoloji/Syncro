package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ToolsScreen(viewModel: MeshViewModel = viewModel()) {
    val isSirenOn by viewModel.isSirenOn.collectAsState()
    val isFlashlightOn by viewModel.isFlashlightOn.collectAsState()
    var isPoliceMode by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Emergency Tools", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (isPoliceMode) {
            PoliceFlasher()
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ToolButton("Siren", isSirenOn, Color.Red) { viewModel.toggleSiren() }
            }
            item {
                ToolButton("Flashlight", isFlashlightOn, Color.Yellow) { viewModel.toggleFlashlight() }
            }
            item {
                ToolButton("Strobe", false, Color.White) { /* Add strobe toggle to VM */ }
            }
            item {
                ToolButton("Police Mode", isPoliceMode, Color.Blue) { isPoliceMode = !isPoliceMode }
            }
            item {
                ToolButton("Screen Light", false, Color.Cyan) { /* Max brightness white screen */ }
            }
        }
    }
}

@Composable
fun ToolButton(name: String, isActive: Boolean, activeColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) activeColor else Color.DarkGray,
            contentColor = if (isActive) Color.Black else Color.White
        )
    ) {
        Text(name)
    }
}

@Composable
fun PoliceFlasher() {
    var state by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            state = !state
            kotlinx.coroutines.delay(200)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(if (state) Color.Blue else Color.Red)
    )
}
