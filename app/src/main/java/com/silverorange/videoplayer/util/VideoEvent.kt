package com.silverorange.videoplayer.util

import com.silverorange.videoplayer.data.models.VideoResponse

sealed class VideoEvent {
        class Success(val videos: VideoResponse): VideoEvent()
        class Failure(val errorText: String): VideoEvent()
        object Loading: VideoEvent()
        object Empty: VideoEvent()
}