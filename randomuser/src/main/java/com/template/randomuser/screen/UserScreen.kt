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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.template.designSystem.atom.HLine

@Composable
fun UserScreen(vm: UserViewModel = viewModel()) {
  val userState by vm.selectedUser.collectAsState(null)
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (userState == null) {
      Text("Loading...")
    } else {
      val user = userState!!
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "User Profile",
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(16.dp))
      HLine()
      Spacer(modifier = Modifier.height(24.dp))
      Image(
        painter = rememberImagePainter(user.picture.large),
        contentDescription = "User photo",
        modifier = Modifier
          .size(200.dp)
          .clip(CircleShape)
      )
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Name: ${user.name.first} ${user.name.last}",
        style = MaterialTheme.typography.h5
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Email: ${user.email}",
        style = MaterialTheme.typography.body1
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Phone: ${user.phone}",
        style = MaterialTheme.typography.body1
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Address: ${user.location.street.number} ${user.location.street.name}, ${user.location.city}, ${user.location.state}, ${user.location.country}",
        style = MaterialTheme.typography.body1
      )
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}




