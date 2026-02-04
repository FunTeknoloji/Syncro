package com.funteknoloji.quakesafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.launch
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
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuakeSafeTheme {
                MainAppLayout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppLayout() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomItems = listOf("Ana Sayfa", "Mesajlar", "Araçlar", "Rehber")
    val bottomIcons = listOf(Icons.Default.Home, Icons.Default.Email, Icons.Default.Build, Icons.Default.List)

    var selectedItemRoute by remember { mutableStateOf("home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text("Ek Özellikler", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineSmall)
                NavigationDrawerItem(
                    label = { Text("Kişiler") },
                    selected = selectedItemRoute == "contacts",
                    onClick = {
                        selectedItemRoute = "contacts"
                        scope.launch { drawerState.close() }
                        navController.navigate("contacts")
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Harita") },
                    selected = selectedItemRoute == "map",
                    onClick = {
                        selectedItemRoute = "map"
                        scope.launch { drawerState.close() }
                        navController.navigate("map")
                    },
                    icon = { Icon(Icons.Default.Place, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Ayarlar") },
                    selected = selectedItemRoute == "settings",
                    onClick = {
                        selectedItemRoute = "settings"
                        scope.launch { drawerState.close() }
                        navController.navigate("settings")
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Uygulama Bilgisi", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodySmall)
                ListItem(
                    headlineContent = { Text("Versiyon: 1.0.0-PRO") },
                    supportingContent = { Text("com.funteknoloji.quakesafe") }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("QuakeSafe Offline") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menü")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    bottomItems.forEachIndexed { index, item ->
                        val route = when(item) {
                            "Ana Sayfa" -> "home"
                            "Mesajlar" -> "chat"
                            "Araçlar" -> "tools"
                            "Rehber" -> "guides"
                            else -> "home"
                        }
                        NavigationBarItem(
                            icon = { Icon(bottomIcons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedItemRoute == route,
                            onClick = {
                                selectedItemRoute = route
                                navController.navigate(route)
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
}
