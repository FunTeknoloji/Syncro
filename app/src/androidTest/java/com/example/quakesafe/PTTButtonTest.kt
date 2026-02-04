package com.example.quakesafe

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.quakesafe.ui.MainScreen
import com.example.quakesafe.ui.theme.QuakeSafeTheme
import org.junit.Rule
import org.junit.Test

class PTTButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pttButton_isDisplayed() {
        composeTestRule.setContent {
            QuakeSafeTheme {
                MainScreen()
            }
        }

        composeTestRule.onNodeWithText("PUSH TO TALK").assertIsDisplayed()
    }
}
