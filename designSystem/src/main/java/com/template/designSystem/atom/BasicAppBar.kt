package com.template.designSystem.atom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.template.designSystem.theme.LightBlack
import com.template.designSystem.theme.TemplateTheme
import com.template.designSystem.theme.Type

@Preview(showBackground = true)
@Composable
fun BasicAppBarPreview() {
  TemplateTheme {
    BasicAppBar("foobeans")
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAppBar(
  title: String
) {
  Column {
    TopAppBar(
      title = { Text(text = title, style = Type.headlineMedium) },
    )
    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = LightBlack)
  }
}