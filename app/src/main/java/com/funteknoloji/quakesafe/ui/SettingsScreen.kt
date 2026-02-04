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
        Text("Ayarlar", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Görünüm", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Yazı Tipi Boyutu: ${fontSize.toInt()}sp")
                Slider(
                    value = fontSize,
                    onValueChange = { viewModel.setFontSize(it) },
                    valueRange = 12f..30f
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ağ ve Bağlantı", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Mesh Ağ İletişimi", modifier = Modifier.weight(1f))
                    Switch(checked = isMeshActive, onCheckedChange = { viewModel.toggleMesh(it) })
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Hotspot Aktarma Modu", modifier = Modifier.weight(1f))
                    Switch(checked = isHotspotRelayEnabled, onCheckedChange = { viewModel.toggleHotspotRelay(it) })
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Text("Cihaz Kimliği: ${android.os.Build.MODEL}", style = MaterialTheme.typography.labelMedium)
        Text("Uygulama Sürümü: 1.0.0-PRO", style = MaterialTheme.typography.labelMedium)
    }
}
