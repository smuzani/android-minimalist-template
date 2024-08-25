package com.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.template.designSystem.theme.TemplateTheme
import com.template.spine.Nerve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  val nerve = Nerve()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Hide the default action bar to replace with Material3's TopAppBar
    actionBar?.hide()
    setContent {
      val title by nerve.title.collectAsState()

      TemplateTheme {
        // tonalElevation plays with Material 3 to decide the color
        Surface(tonalElevation = 5.dp) {
          Scaffold(
            topBar = {
              // can be TopAppBar, CenterAlignedTopAppBar, MediumTopAppBar, LargeTopAppBar
              CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                  containerColor = MaterialTheme.colorScheme.primaryContainer,
                  titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                  Text(title)
                }
              )
            },
            bottomBar = {

            }
          ) { innerPadding ->
            nerve.currentScreenPadding = innerPadding
            TemplateNavHost(nerve)
          }
        }
      }
    }
  }
}

