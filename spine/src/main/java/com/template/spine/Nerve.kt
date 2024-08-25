package com.template.spine

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Nerve handles communication between the lower level screens and the top level (scaffold)
class Nerve {
  // Used as Flow because we want it to change on UI as soon as it changes
  private var _title = MutableStateFlow("")
  val title: StateFlow<String> = _title
  fun setTitle(title: String) {
    Log.d("Nerve", "setTitle: $title")
    _title.value = title
  }

  // Does not need to be a StateFlow because it is only set and accessed once
  var currentScreenPadding = PaddingValues(0.dp)
}