package com.silverorange.videoplayer.repository

import com.silverorange.videoplayer.data.VideoApi
import com.silverorange.videoplayer.data.models.VideoResponse
import com.silverorange.videoplayer.util.Resource
import javax.inject.Inject

class DefaultVideoRepository @Inject constructor(private val api: VideoApi) : VideoRepository {
    override suspend fun getVideos(): Resource<VideoResponse> {
        return try {
            val response = api.getVideos()
            val result = response.body()

            if (response.isSuccessful && result != null){
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }

        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}