package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MeshMapScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mesh Topoloji Görünümü", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Canvas(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(Color.Blue, radius = 20f, center = center) // Self

            // Draw some dummy nodes and connections
            val nodes = listOf(
                Offset(center.x + 100, center.y - 50),
                Offset(center.x - 80, center.y + 120),
                Offset(center.x + 150, center.y + 100)
            )

            nodes.forEach { node ->
                drawLine(Color.Gray, start = center, end = node, strokeWidth = 5f)
                drawCircle(Color.Green, radius = 15f, center = node)
            }
        }
    }
}
