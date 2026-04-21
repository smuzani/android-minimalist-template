package com.template.randomuser.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.randomuser.network.RandomUser
import com.template.randomuser.network.RandomUserRepository
import com.template.spine.UiState
import com.template.spine.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserViewModel @Inject constructor(
    repository: RandomUserRepository,
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    val uiState: StateFlow<UiState<List<RandomUser>>> =
        refreshTrigger
            .flatMapLatest { repository.get50RandomUsers().asUiState() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Pending())

    fun refreshUsers() {
        refreshTrigger.tryEmit(Unit)
    }
}
