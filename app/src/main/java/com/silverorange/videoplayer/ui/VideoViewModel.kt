package com.silverorange.videoplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.repository.DefaultVideoRepository
import com.silverorange.videoplayer.util.Resource
import com.silverorange.videoplayer.util.VideoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(val repository: DefaultVideoRepository): ViewModel() {
    private val _videosEvent = MutableStateFlow<VideoEvent>(VideoEvent.Empty)
    val videosEvent: StateFlow<VideoEvent> = _videosEvent

    init {
        loadVideos()
    }

    private fun loadVideos(){
        _videosEvent.value = VideoEvent.Loading

        viewModelScope.launch {
            when (val videoResponse = repository.getVideos()) {
                is Resource.Success -> {
                    //if (videoResponse.data != null) {
                    _videosEvent.value = videoResponse.data?.let { VideoEvent.Success(it) } ?:
                        VideoEvent.Failure("Unexpected Error")
                }

                is Resource.Error -> {
                    _videosEvent.value = VideoEvent.Failure(videoResponse.message!!)
                }
            }
        }
    }
}