package com.silverorange.videoplayer.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.silverorange.videoplayer.data.models.VideoResponseItem
import com.silverorange.videoplayer.repository.DefaultVideoRepository
import com.silverorange.videoplayer.util.Resource
import com.silverorange.videoplayer.util.VideoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: DefaultVideoRepository,
    val player: Player
): ViewModel() {
    private val _videosEvent = MutableStateFlow<VideoEvent>(VideoEvent.Empty)
    val videosEvent: StateFlow<VideoEvent> = _videosEvent

    private val _currentVideoItem = MutableStateFlow<VideoResponseItem?>(null)
    val currentVideoItem: StateFlow<VideoResponseItem?> = _currentVideoItem

    private var videoList = listOf<VideoResponseItem>()

    init {
        loadVideos()
        player.prepare()
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


    fun setCurrentVideoItem(video: VideoResponseItem){
        _currentVideoItem.value = video
    }

    fun setVideoList(videos: List<VideoResponseItem>){
        videoList = videos
    }


    fun playVideo(){
        player.play()
    }

    fun playNext(){
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()

            // update the current video item by index
            val videoIndex = player.currentMediaItemIndex
            _currentVideoItem.value = videoList[videoIndex]
        }
    }

    fun playPrev(){
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()

            // update the current video item by index
            val videoIndex = player.currentMediaItemIndex
            _currentVideoItem.value = videoList[videoIndex]
        }
    }

    fun isVideoPlaying(): Boolean {
        return player.isPlaying
    }

    fun isLastVideo(): Boolean {
        return player.currentMediaItemIndex == player.mediaItemCount - 1
    }

    fun isFirstVideo(): Boolean {
        return player.currentMediaItemIndex == 0
    }

    fun stopVideo(){
        player.pause()
    }

    fun getVideoSize() : Int {
        return player.mediaItemCount
    }

    fun clearVideos(){
        player.clearMediaItems()
    }

    private val mediaItems = mutableListOf<MediaItem>()

    fun addVideoUri(uri: Uri){
        player.addMediaItem(MediaItem.fromUri(uri))
        mediaItems.add(MediaItem.fromUri(uri))

        //clear
        player.clearMediaItems()
        player.setMediaItems(mediaItems)
    }
}