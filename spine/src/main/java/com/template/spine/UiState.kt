package com.template.spine

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface UiState<out T> {
    data class Pending<T>(val previous: T? = null) : UiState<T>

    data class Done<T>(val data: T) : UiState<T>

    data class Failed(val message: String) : UiState<Nothing>
}

fun <T> Flow<T>.asUiState(): Flow<UiState<T>> =
    this
        .map<T, UiState<T>> { UiState.Done(it) }
        .onStart { emit(UiState.Pending()) }
        .catch { emit(UiState.Failed(it.message ?: "Something went wrong")) }
