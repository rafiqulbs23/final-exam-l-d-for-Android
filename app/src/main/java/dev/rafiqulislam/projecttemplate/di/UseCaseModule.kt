package dev.rafiqulislam.projecttemplate.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Abdullah on 14/5/25.
 */

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

 /*   @Provides
    @Singleton
    fun provideGetAllMovieListUseCase(
        postsRepository: PostsRepository
    ): GetAllPostUseCase {
        return GetAllPostUseCase(postsRepository)
    }*/


}