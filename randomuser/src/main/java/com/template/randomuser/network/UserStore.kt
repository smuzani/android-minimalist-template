package com.template.randomuser.network

object UserStore {
  private var selectedUser: RandomUser? = null

  fun setUser(user: RandomUser) {
    selectedUser = user
  }

  fun getUser(): RandomUser? = selectedUser
}
