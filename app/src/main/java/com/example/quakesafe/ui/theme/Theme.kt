package com.example.quakesafe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quakesafe.ui.MeshViewModel

private val DarkColorScheme = darkColorScheme()

@Composable
fun QuakeSafeTheme(viewModel: MeshViewModel = viewModel(), content: @Composable () -> Unit) {
    val fontSize by viewModel.fontSize.collectAsState()

    val typography = Typography(
        bodyLarge = TextStyle(fontSize = fontSize.sp),
        bodyMedium = TextStyle(fontSize = (fontSize - 2).sp),
        titleLarge = TextStyle(fontSize = (fontSize + 6).sp),
        headlineMedium = TextStyle(fontSize = (fontSize + 12).sp)
    )

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = typography,
        content = content
    )
}
