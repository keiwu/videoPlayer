package com.silverorange.videoplayer.di

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.silverorange.videoplayer.repository.DefaultVideoRepository
import com.silverorange.videoplayer.data.VideoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


const val BASE_URL = "http://10.0.0.146:4000/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideVideoApi(): VideoApi = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VideoApi::class.java)

    @Provides
    @Singleton
    fun provideVideoRepository(api: VideoApi) = DefaultVideoRepository(api)

    @Provides
    @Singleton
    fun provideVideoPlayer(app: Application): Player {
        return ExoPlayer
            .Builder(app)
            .build()
    }
}