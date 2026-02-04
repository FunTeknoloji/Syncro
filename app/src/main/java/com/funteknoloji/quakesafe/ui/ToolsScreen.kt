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
        Text("Acil Durum Araçları", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (isPoliceMode) {
            PoliceFlasher()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Sinyalizasyon", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ToolButton("Siren", isSirenOn, Color.Red, Modifier.weight(1f)) { viewModel.toggleSiren() }
            ToolButton("Fener", isFlashlightOn, Color.Yellow, Modifier.weight(1f)) { viewModel.toggleFlashlight() }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ToolButton("Çakar", false, Color.White, Modifier.weight(1f)) { /* Add strobe toggle to VM */ }
            ToolButton("Polis Modu", isPoliceMode, Color.Blue, Modifier.weight(1f)) { isPoliceMode = !isPoliceMode }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Diğer", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ToolButton("Ekran Işığı", false, Color.Cyan, Modifier.fillMaxWidth()) { /* Max brightness white screen */ }
    }
}

@Composable
fun ToolButton(name: String, isActive: Boolean, activeColor: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(100.dp),
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
