package com.template.randomuser.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RandomUserRepository {
  fun getRandomUser(): Flow<RandomUser?>
  fun get500RandomUsers(): Flow<List<RandomUser>?>
}

class RandomUserRepositoryImpl(private val randomUserService: RandomUserService) :
  RandomUserRepository {
  override fun getRandomUser(): Flow<RandomUser?> = flow {
    emit(randomUserService.getRandomUser().results.firstOrNull())
  }

  override fun get500RandomUsers(): Flow<List<RandomUser>?> = flow {
    emit(randomUserService.getRandomUsers().results)
  }
}