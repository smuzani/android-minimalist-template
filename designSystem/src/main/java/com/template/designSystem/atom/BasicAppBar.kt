package com.template.designSystem.atom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.template.designSystem.theme.LightBlack
import com.template.designSystem.theme.TemplateTheme
import com.template.designSystem.theme.Purple500
import com.template.designSystem.theme.Type

@Preview(showBackground = true)
@Composable
fun BasicAppBarPreview() {
  TemplateTheme {
    BasicAppBar("foobeans")
  }
}

@Composable
fun BasicAppBar(
  title: String
) {
  Column {
    TopAppBar(
      title = { Text(text = title, style = Type.h5) },
      backgroundColor = Purple500
    )
    Divider(modifier = Modifier.fillMaxWidth(), color = LightBlack)
  }
}