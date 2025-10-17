package dev.rafiqulislam.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

/*    @Binds
    @Singleton
    abstract fun provideMovieRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostsRepository*/


}