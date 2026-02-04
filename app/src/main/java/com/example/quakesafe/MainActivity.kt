package com.example.quakesafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quakesafe.ui.ChatScreen
import com.example.quakesafe.ui.ContactScreen
import com.example.quakesafe.ui.SettingsScreen
import com.example.quakesafe.ui.GuideScreen
import com.example.quakesafe.ui.MainScreen
import com.example.quakesafe.ui.MeshMapScreen
import com.example.quakesafe.ui.ToolsScreen
import com.example.quakesafe.ui.theme.QuakeSafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuakeSafeTheme {
                MainAppLayout()
            }
        }
    }
}

@Composable
fun MainAppLayout() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Chat", "Contacts", "Map", "Tools", "Guides", "Settings")
    val icons = listOf(Icons.Default.Home, Icons.Default.Email, Icons.Default.Person, Icons.Default.Place, Icons.Default.Build, Icons.Default.List, Icons.Default.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.lowercase())
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
            composable("home") { MainScreen() }
            composable("chat") { ChatScreen() }
            composable("contacts") { ContactScreen() }
            composable("map") { MeshMapScreen() }
            composable("tools") { ToolsScreen() }
            composable("guides") { GuideScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}
