package com.template.randomuser.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.randomuser.network.RandomUser
import com.template.randomuser.network.RandomUserRepositoryImpl
import com.template.randomuser.network.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

  var displayedScreen by mutableStateOf(Screen.USERS)
    private set

  private val _selectedUser: MutableStateFlow<RandomUser?> = MutableStateFlow(null)
  val selectedUser: StateFlow<RandomUser?> = _selectedUser
  fun selectUser(user: RandomUser?) {
    _selectedUser.value = user
    displayedScreen = Screen.USER_DETAILS
  }

  fun showAllUsers() {
    displayedScreen = Screen.USERS
  }

  private val randomUserService = RandomUserRepositoryImpl(RetrofitBuilder.randomUserService)

  init {
    refreshUsers()
  }

  private val _users: MutableStateFlow<List<RandomUser>?> = MutableStateFlow(null)
  val users: StateFlow<List<RandomUser>?> = _users

  fun refreshUsers() {
    viewModelScope.launch {
      randomUserService.get500RandomUsers()
        .flowOn(Dispatchers.IO)
        .catch { e -> Log.e("UserViewModel", "Error: $e") }
        .collect { users ->
          _users.value = users
        }
    }
  }
}

enum class Screen {
  USERS,
  USER_DETAILS
}

