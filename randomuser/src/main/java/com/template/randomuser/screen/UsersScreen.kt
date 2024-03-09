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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.template.randomuser.network.RandomUser

@Composable
fun UsersScreen(onNavigateToDetails: (RandomUser) -> Unit, vm: UserViewModel = viewModel()) {
  val usersState by vm.users.collectAsState(null)
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(onClick = {
      vm.refreshUsers()
    }) {
      Text("Get Users")
    }
    if (usersState == null) {
      Text("Loading...")
    } else {
      val users = usersState!!

      // TODO improve scrolling
      LazyColumn(
        modifier = Modifier
          .padding(4.dp)
      ) {
        items(users) { user ->
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
      .padding(vertical = 2.dp)
      .clickable {
        onNavigateToDetails(user)
      }, verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = rememberAsyncImagePainter(user.picture.thumbnail),
      contentDescription = "User photo",
      modifier = Modifier
        .size(20.dp)
        .clip(CircleShape)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = "${user.name.first} ${user.name.last}",
      style = MaterialTheme.typography.headlineSmall
    )
  }
}

const val USERS = "UsersScreen"