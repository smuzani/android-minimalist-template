package com.template

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.randomuser.network.UserStore
import com.template.randomuser.screen.USERS
import com.template.randomuser.screen.USER_DETAILS
import com.template.randomuser.screen.UserScreen
import com.template.randomuser.screen.UserViewModel
import com.template.randomuser.screen.UsersScreen

// This is the central nerve to navigate between screens
@Composable
fun TemplateNavHost(innerPadding: PaddingValues) {
  val navController = rememberNavController()
  NavHost(navController, startDestination = USERS) {
    composable(USERS) {
      val viewModel = hiltViewModel<UserViewModel>()
      UsersScreen(
        onNavigateToDetails = { selectedUser ->
          UserStore.setUser(selectedUser)
          navController.navigate(USER_DETAILS)
        },
        innerPadding, viewModel
      )
    }
    composable(USER_DETAILS) {
      UserScreen(innerPadding)
    }
  }
}