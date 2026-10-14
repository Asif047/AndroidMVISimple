package com.asif047.myapplication.data.di

import com.asif047.myapplication.data.repository.AuthRepositoryImpl
import com.asif047.myapplication.data.repository.UserRepositoryImpl
import com.asif047.myapplication.domain.repository.AuthRepository
import com.asif047.myapplication.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
