package com.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.template.designSystem.theme.TemplateTheme
import com.template.randomuser.screen.UserViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TemplateTheme {
        TemplateNavHost()
      }
    }
  }
}

