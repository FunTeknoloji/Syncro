package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(viewModel: MeshViewModel = viewModel()) {
    val fontSize by viewModel.fontSize.collectAsState()
    val isHotspotRelayEnabled by viewModel.isHotspotRelayEnabled.collectAsState()
    val isMeshActive by viewModel.isMeshActive.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Font Size: ${fontSize.toInt()}sp")
        Slider(
            value = fontSize,
            onValueChange = { viewModel.setFontSize(it) },
            valueRange = 12f..30f
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Mesh Networking", modifier = Modifier.weight(1f))
            Switch(checked = isMeshActive, onCheckedChange = { viewModel.toggleMesh(it) })
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Hotspot Relay Mode", modifier = Modifier.weight(1f))
            Switch(checked = isHotspotRelayEnabled, onCheckedChange = { viewModel.toggleHotspotRelay(it) })
        }

        Spacer(modifier = Modifier.weight(1f))

        Text("Device ID: ${android.os.Build.MODEL}", style = MaterialTheme.typography.labelMedium)
        Text("App Version: 1.0.0-PRO", style = MaterialTheme.typography.labelMedium)
    }
}
