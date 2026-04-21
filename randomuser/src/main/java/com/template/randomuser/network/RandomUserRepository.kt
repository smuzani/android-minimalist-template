package com.template.randomuser.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RandomUserRepository {
    fun get50RandomUsers(): Flow<List<RandomUser>>
}

class RandomUserRepositoryImpl(private val randomUserService: RandomUserService) :
    RandomUserRepository {
    override fun get50RandomUsers(): Flow<List<RandomUser>> =
        flow {
            emit(randomUserService.getRandomUsers().results)
        }
}
