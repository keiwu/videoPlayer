package com.silverorange.videoplayer.data

import com.silverorange.videoplayer.data.models.VideoResponse
import retrofit2.Response
import retrofit2.http.GET

interface VideoApi {
    //http://localhost:4000/videos
    @GET("videos")
    suspend fun getVideos(): Response<VideoResponse>
}