package dev.rafiqulislam.projecttemplate.di

import dev.rafiqulislam.core.data.repository.TokenRepository
import dev.rafiqulislam.core.data.repositoryImpl.TokenRepositoryImpl
import dev.rafiqulislam.projecttemplate.features.tasks.data.repository.TaskRepositoryImpl
import dev.rafiqulislam.projecttemplate.features.tasks.domain.repository.TaskRepository
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

    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository
}