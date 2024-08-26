package com.template.randomuser.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.template.randomuser.network.RandomUser
import com.template.spine.Nerve

@Composable
fun UsersScreen(
  nerve: Nerve,
  onNavigateToDetails: (RandomUser) -> Unit,
  vm: UserViewModel
) {
  val usersState by vm.users.collectAsState(null)
  val users = remember(usersState) {
    usersState ?: emptyList()
  }
  // Setting the title in LaunchedEffect will only happen once when they enter the screen
  LaunchedEffect(Unit) {
    nerve.setTitle("Users")
    nerve.setBottomBarButtonText("Get Users")
    nerve.setOnBottomBarButtonClicked { vm.refreshUsers() }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(nerve.currentScreenPadding)
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (usersState == null) {
      Text("Loading…", style = MaterialTheme.typography.displayLarge)
    } else {
      LazyColumn(
        modifier = Modifier.padding(4.dp)
      ) {
        items(users, key = { user -> user.id.value }) { user ->
          UserRow(user, onNavigateToDetails)
          HorizontalDivider()
        }
      }
    }
  }
}

@Composable fun UserRow(user: RandomUser, onNavigateToDetails: (RandomUser) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp)
      .clickable {
        onNavigateToDetails(user)
      }, verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = rememberAsyncImagePainter(user.picture.thumbnail),
      contentDescription = "User photo",
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
    )
    Spacer(modifier = Modifier.width(25.dp))
    Text(
      text = "${user.name.first} ${user.name.last}",
      style = MaterialTheme.typography.headlineLarge
    )
  }
}

const val USERS = "UsersScreen"