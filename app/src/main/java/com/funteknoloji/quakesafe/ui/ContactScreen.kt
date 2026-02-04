package com.funteknoloji.quakesafe.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ContactScreen(viewModel: MeshViewModel = viewModel()) {
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Kişiler", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Kişi Ekle")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(contacts) { contact ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text(contact.name, style = MaterialTheme.typography.titleMedium) },
                        supportingContent = { Text(contact.phoneNumber) },
                        trailingContent = { if (contact.isEmergencyContact) Text("🚨", style = MaterialTheme.typography.headlineSmall) }
                    )
                }
            }
        }

        if (showDialog) {
            AddContactDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, phone, isEmergency ->
                    viewModel.addContact(name, phone, isEmergency)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddContactDialog(onDismiss: () -> Unit, onConfirm: (String, String, Boolean) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isEmergency by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kişi Ekle") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("İsim") })
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefon") })
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = isEmergency, onCheckedChange = { isEmergency = it })
                    Text("Acil Durum Kişisi")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, phone, isEmergency) }) { Text("Ekle") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("İptal") }
        }
    )
}
