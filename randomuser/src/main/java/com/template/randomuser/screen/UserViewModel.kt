package com.template.randomuser.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.randomuser.network.RandomUser
import com.template.randomuser.network.RandomUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: RandomUserRepository,
) : ViewModel() {
    private val _users = MutableStateFlow<List<RandomUser>?>(null)
    val users: StateFlow<List<RandomUser>?> = _users

    init {
        refreshUsers()
    }

    fun refreshUsers() {
        viewModelScope.launch {
            repository.get50RandomUsers()
                .flowOn(Dispatchers.IO)
                .catch { e -> Log.e("UserViewModel", "Error: $e") }
                .collect { users -> _users.value = users }
        }
    }
}
