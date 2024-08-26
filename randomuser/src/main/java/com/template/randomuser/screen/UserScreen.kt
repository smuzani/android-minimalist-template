package com.template.randomuser.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.template.randomuser.network.UserStore
import com.template.spine.Nerve

@Composable
fun UserScreen(nerve: Nerve) {
  val user = UserStore.getUser()

  LaunchedEffect(Unit) {
    nerve.setTitle(user?.name?.first?.plus(" ")?.plus(user.name.last) ?: "User Profile")
    nerve.setBottomBarButtonText("")
    nerve.setOnBottomBarButtonClicked { }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(nerve.currentScreenPadding)
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (user == null) {
      Text("Loading…", style = MaterialTheme.typography.displayLarge)
    } else {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "User Profile",
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider()
      Spacer(modifier = Modifier.height(24.dp))
      Image(
        painter = rememberAsyncImagePainter(user.picture.large),
        contentDescription = "User photo",
        modifier = Modifier
          .size(200.dp)
          .clip(CircleShape)
      )
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "${user.name.first} ${user.name.last}",
        style = MaterialTheme.typography.headlineLarge
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Email: ${user.email}",
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Phone: ${user.phone}",
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Address: ${user.location.street.number} ${user.location.street.name}, ${user.location.city}, ${user.location.state}, ${user.location.country}",
        style = MaterialTheme.typography.bodyMedium
      )
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

const val USER_DETAILS = "UserScreen"


