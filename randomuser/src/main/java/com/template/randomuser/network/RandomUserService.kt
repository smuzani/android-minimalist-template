package com.template.randomuser.network

import retrofit2.http.GET

interface RandomUserService {
  @GET("api")
  suspend fun getRandomUser(): RandomUserResponse

  @GET("api/?results=50")
  suspend fun getRandomUsers(): RandomUserResponse
}
