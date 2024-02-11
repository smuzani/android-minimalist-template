package com.template

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.randomuser.screen.USERS
import com.template.randomuser.screen.USER_DETAILS
import com.template.randomuser.screen.UserScreen
import com.template.randomuser.screen.UsersScreen

@Composable
fun TemplateNavHost() {
  val navController = rememberNavController()
  NavHost(navController, startDestination = USERS) {
    composable(USERS) {
      UsersScreen(onNavigateToDetails = {
        navController.navigate(USER_DETAILS)
      })
    }
    composable(USER_DETAILS) { UserScreen() }
  }
}