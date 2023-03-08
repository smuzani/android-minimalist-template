package com.template.designSystem.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.ui.unit.dp
import com.template.designSystem.theme.ReallyLightBlack

@Composable fun HLine() {
  Canvas(
    Modifier
      .fillMaxWidth()
      .height(1.dp)
  ) {
    drawLine(
      color = ReallyLightBlack,
      strokeWidth = 1.dp.toPx(),
      start = Offset(0f, 0f),
      end = Offset(size.width, 0f),
    )
  }
}