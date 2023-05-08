package com.silverorange.videoplayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import com.silverorange.videoplayer.ui.VideoViewModel
import com.silverorange.videoplayer.util.VideoEvent
import dagger.hilt.android.AndroidEntryPoint
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TopBar()
      VideoPresenter()
    }
  }
}

@Preview
@Composable
fun TopBar() {
  TopAppBar(title = { Text(text = "Video Player") })
}

@Composable
fun VideoPresenter(){
  val videoViewModel = hiltViewModel<VideoViewModel>()
  val showControls = remember {
    mutableStateOf(true)
  }

  when (val videosEvent = videoViewModel.videosEvent.collectAsState().value){
    is VideoEvent.Success -> {
      val videoList = videosEvent.videos.sortedByDescending{it.publishedAt }

      videoViewModel.clearVideos()
      for (video in videoList){
          videoViewModel.addVideoUri(Uri.parse(video.fullURL))
      }

      // set the current video and videoList if list is not empty
      if (videoList.isNotEmpty()) {
        videoViewModel.setCurrentVideoItem(videoList[0])
        videoViewModel.setVideoList(videoList)
      }
    }

    else -> {
      // error message or loading screen
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
  ) {

    Box(modifier = Modifier
      .fillMaxWidth()
      .align(Alignment.CenterHorizontally),
      contentAlignment = Center   // center the video controls
    ) {
      AndroidView(
        factory = { context ->
          PlayerView(context).also {
            it.player = videoViewModel.player
            it.useController = false
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            showControls.value = true
          }
          .aspectRatio(16 / 9.0f), //16:9 ratio
      )

      TimedControl(videoViewModel = videoViewModel, showControls)
    }

    VideoDetail(videoViewModel)

  }
}

@Composable
fun VideoDetail(videoViewModel: VideoViewModel){
  val currentVideoItem = videoViewModel.currentVideoItem.collectAsState()
  val scroll = rememberScrollState(0)


  Column(modifier = Modifier
    .fillMaxWidth()
    .padding(10.dp)
    .verticalScroll(scroll)

    ) {
    val title = currentVideoItem.value?.title

    title?.let {
      MarkdownText(
        markdown = it,
        fontResource = androidx.media3.ui.R.font.roboto_medium_numbers,
        fontSize = 18.sp
      )
    }

    val author = currentVideoItem.value?.author?.name

    author?.let {
      MarkdownText(
        markdown = it,
        color = Color.DarkGray,
        fontResource = androidx.media3.ui.R.font.roboto_medium_numbers,
        fontSize = 18.sp
      )
    }

    val description = currentVideoItem.value?.description
    description?.let {
      MarkdownText(
        markdown = it,
        fontResource = androidx.media3.ui.R.font.roboto_medium_numbers,
        fontSize = 18.sp
      )
    }
  }
}


@Composable
fun TimedControl(videoViewModel: VideoViewModel, showControls: MutableState<Boolean>){
  if (showControls.value){
    Controls(videoViewModel)

    LaunchedEffect(key1 = Unit){
      delay(5000)
      showControls.value = false
    }
  }
}

@Composable
fun Controls(videoViewModel: VideoViewModel){
  val resource = if (videoViewModel.isVideoPlaying()) {
      R.drawable.pause
  } else {
        R.drawable.play
  }
  val playPauseResourceId = remember{
    mutableStateOf(resource)
  }

  val isLastVideo = remember {
    mutableStateOf(videoViewModel.isLastVideo())
  }

  val isFirstVideo = remember {
    mutableStateOf(videoViewModel.isFirstVideo())
  }

  Row(modifier = Modifier
    .fillMaxWidth(),

      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
  ){
    IconButton(onClick = {
        videoViewModel.playPrev()
        isFirstVideo.value = videoViewModel.isFirstVideo()
        isLastVideo.value = videoViewModel.isLastVideo()
    }) {
      if (isFirstVideo.value) {
        Icon(
          painterResource(id = R.drawable.previous),
          tint = Color.Gray,
          contentDescription = "Play Previous"
        )
      } else {
        Icon(
          painterResource(id = R.drawable.previous),
          contentDescription = "Play Previous",
        )
      }
    }

    IconButton(onClick = {
      if (videoViewModel.isVideoPlaying()) {
        // if video is playing, clicking the button will stop the video
        videoViewModel.stopVideo()
        // since the video is stopped, display the "Play" button so user can play it
        playPauseResourceId.value = R.drawable.play
      } else {
        // if video is not playing, clicking the button will play the video
        videoViewModel.playVideo()
        // since the video is playing, display the "Pause button so user can pause it
        playPauseResourceId.value = R.drawable.pause
      }

    }) {
      Icon(painterResource(id = playPauseResourceId.value), contentDescription = "Play/Pause")
    }

    IconButton(onClick = {
        videoViewModel.playNext()
        // this will update the previous button
        isFirstVideo.value = videoViewModel.isFirstVideo()
        isLastVideo.value = videoViewModel.isLastVideo()
    }) {
      Icon(painterResource(id = R.drawable.next), contentDescription = "Play Next")

      if (isLastVideo.value) {
        Icon(
          painterResource(id = R.drawable.next),
          tint = Color.Gray,
          contentDescription = "Play Next"
        )
      } else {
        Icon(
          painterResource(id = R.drawable.next),
          contentDescription = "Play Next",
        )
      }
    }
  }
}
