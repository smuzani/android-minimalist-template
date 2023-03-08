package com.template.randomuser.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.template.designSystem.atom.HLine

@Composable
fun UsersScreen(vm: UserViewModel = viewModel()) {
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
          Row(modifier = Modifier.clickable {
            vm.selectUser(user)
          }, verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = rememberImagePainter(user.picture.thumbnail),
              contentDescription = "User photo",
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "${user.name.first} ${user.name.last}",
              style = MaterialTheme.typography.h5
            )
          }
          HLine()
        }
      }
    }
  }
}