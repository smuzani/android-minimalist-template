package com.template.designSystem.atom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.template.designSystem.theme.TemplateTheme

@Preview(showBackground = true)
@Composable
fun SingleListItemPreview() {
  TemplateTheme {
    SingleListRow("List Item", {})
  }
}

@Composable
fun SingleListRow(
  title: String, onClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick.invoke() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = title,
        color = Black,
        modifier = Modifier
          .fillMaxWidth(0.90F)
          .padding(horizontal = 16.dp)
      )
    }
    Divider(
      color = Gray.copy(alpha = 0.15F),
      thickness = 0.5.dp,
    )
  }
}