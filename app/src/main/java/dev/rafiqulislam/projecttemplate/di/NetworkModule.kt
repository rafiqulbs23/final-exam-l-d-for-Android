package dev.rafiqulislam.projecttemplate.di



import android.content.Context
import dev.rafiqulislam.core.network.NetworkFactory
import dev.rafiqulislam.core.network.TaskApiService
import com.incepta.msfa.shared.data.remote.AppApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Enables detailed debug logging
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }


    /**
     * Provides an instance of [AppApiService] using the provided [Retrofit] instance.
     *
     * @param retrofit The Retrofit instance to use for creating the service.
     * @return An instance of [AppApiService].
     */
    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context): AppApiService {
        return NetworkFactory.createService(
            context = context,
            serviceClass = AppApiService::class.java,
        )
    }

    /**
     * Provides an instance of [TaskApiService] using the provided [Retrofit] instance.
     *
     * @param context The application context.
     * @return An instance of [TaskApiService].
     */
    @Provides
    @Singleton
    fun provideTaskApiService(@ApplicationContext context: Context): TaskApiService {
        return NetworkFactory.createService(
            context = context,
            serviceClass = TaskApiService::class.java,
        )
    }

}
