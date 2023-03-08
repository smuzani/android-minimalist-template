package com.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.template.designSystem.theme.TemplateTheme
import com.template.randomuser.screen.Screen
import com.template.randomuser.screen.UserScreen
import com.template.randomuser.screen.UserViewModel
import com.template.randomuser.screen.UsersScreen

class MainActivity : ComponentActivity() {
  private val userViewModel by viewModels<UserViewModel>()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TemplateTheme {
        when (userViewModel.displayedScreen) {
          Screen.USERS -> UsersScreen()
          Screen.USER_DETAILS -> UserScreen()
        }
      }
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    when (userViewModel.displayedScreen) {
      Screen.USERS -> super.onBackPressed()
      Screen.USER_DETAILS -> userViewModel.showAllUsers()
    }
  }
}