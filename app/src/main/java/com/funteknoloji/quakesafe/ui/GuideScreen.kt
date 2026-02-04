package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import java.io.InputStream

@Composable
fun GuideScreen() {
    val context = LocalContext.current
    val guides = remember {
        try {
            val inputStream: InputStream = context.assets.open("guides/emergency_guides.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer)
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<GuideCategory>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val category = obj.getString("category")
                val stepsArray = obj.getJSONArray("steps")
                val steps = mutableListOf<String>()
                for (j in 0 until stepsArray.length()) {
                    steps.add(stepsArray.getString(j))
                }
                list.add(GuideCategory(category, steps))
            }
            list
        } catch (e: Exception) {
            emptyList<GuideCategory>()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Afet Rehberleri", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(guides) { guide ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(guide.category, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    guide.steps.forEachIndexed { index, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("${index + 1}.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(step, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

data class GuideCategory(val category: String, val steps: List<String>)
