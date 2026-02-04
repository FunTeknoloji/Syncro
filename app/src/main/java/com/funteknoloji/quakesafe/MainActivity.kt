package com.funteknoloji.quakesafe

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
import com.funteknoloji.quakesafe.ui.ChatScreen
import com.funteknoloji.quakesafe.ui.ContactScreen
import com.funteknoloji.quakesafe.ui.SettingsScreen
import com.funteknoloji.quakesafe.ui.GuideScreen
import com.funteknoloji.quakesafe.ui.MainScreen
import com.funteknoloji.quakesafe.ui.MeshMapScreen
import com.funteknoloji.quakesafe.ui.ToolsScreen
import com.funteknoloji.quakesafe.ui.theme.QuakeSafeTheme

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
