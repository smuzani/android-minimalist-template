package com.template

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
import com.template.spine.Nerve

// This is the central nerve to navigate between screens
@Composable
fun TemplateNavHost(nerve: Nerve) {
  val navController = rememberNavController()
  NavHost(navController, startDestination = USERS) {
    composable(USERS) {
      val viewModel = hiltViewModel<UserViewModel>()
      UsersScreen(
        nerve,
        onNavigateToDetails = { selectedUser ->
          UserStore.setUser(selectedUser)
          navController.navigate(USER_DETAILS)
        }, viewModel
      )
    }
    composable(USER_DETAILS) {
      UserScreen(nerve)
    }
  }
}