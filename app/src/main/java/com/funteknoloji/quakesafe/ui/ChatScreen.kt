package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(viewModel: MeshViewModel = viewModel()) {
    val messages by viewModel.messages.collectAsState(initial = emptyList())
    var textState by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mesh Chat", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages) { msg ->
                Card(
                    modifier = Modifier.padding(vertical = 4.dp).align(if (msg.isSent) Alignment.End else Alignment.Start),
                    colors = CardDefaults.cardColors(
                        containerColor = if (msg.isSent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = msg.senderId, style = MaterialTheme.typography.labelSmall)
                        Text(text = msg.content)
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            IconButton(onClick = {
                if (textState.isNotBlank()) {
                    viewModel.sendMessage(textState)
                    textState = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}
