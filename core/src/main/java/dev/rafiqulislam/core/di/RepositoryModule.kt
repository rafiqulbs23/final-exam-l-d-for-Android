package dev.rafiqulislam.core.di

import dev.rafiqulislam.core.data.repository.TokenRepository
import dev.rafiqulislam.core.data.repositoryImpl.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository
}