package com.silverorange.videoplayer.repository

import com.silverorange.videoplayer.data.models.VideoResponse
import com.silverorange.videoplayer.util.Resource

interface VideoRepository {
    suspend fun getVideos(): Resource<VideoResponse>
}