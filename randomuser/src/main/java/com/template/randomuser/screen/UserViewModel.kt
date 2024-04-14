package com.template.randomuser.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.randomuser.network.RandomUser
import com.template.randomuser.network.RandomUserRepositoryImpl
import com.template.randomuser.network.RetrofitBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor() : ViewModel() {
  private val randomUserService = RandomUserRepositoryImpl(RetrofitBuilder.randomUserService)

  init {
    refreshUsers()
  }

  private val _users: MutableStateFlow<List<RandomUser>?> = MutableStateFlow(null)
  val users: StateFlow<List<RandomUser>?> = _users

  fun refreshUsers() {
    viewModelScope.launch {
      randomUserService.get50RandomUsers()
        .flowOn(Dispatchers.IO)
        .catch { e -> Log.e("UserViewModel", "Error: $e") }
        .collect { users ->
          _users.value = users
        }
    }
  }
}

