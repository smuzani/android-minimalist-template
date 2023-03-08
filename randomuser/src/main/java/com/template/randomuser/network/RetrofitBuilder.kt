package com.template.randomuser.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

  private fun getRetrofit(): Retrofit {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(BODY)
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
    return Builder()
      .baseUrl("https://randomuser.me/")
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  val randomUserService: RandomUserService = getRetrofit().create(RandomUserService::class.java)
}