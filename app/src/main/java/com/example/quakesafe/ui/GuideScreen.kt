package com.example.quakesafe.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GuideScreen() {
    val guides = listOf(
        "Earthquake: Drop, Cover, Hold On",
        "First Aid: Basic CPR",
        "How to use Mesh: Reach others without internet",
        "Battery Saving: Use power-save mode in settings"
    )

    LazyColumn {
        items(guides) { guide ->
            ListItem(
                headlineContent = { Text(guide) },
                modifier = Modifier.padding(8.dp)
            )
            Divider()
        }
    }
}
