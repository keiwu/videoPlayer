package com.silverorange.videoplayer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.silverorange.videoplayer.ui.VideoViewModel
import com.silverorange.videoplayer.util.VideoEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TopBar()
      TextCard()
      VideoPresenter()
    }
  }
}

@Preview
@Composable
fun TopBar() {
  TopAppBar(title = { Text(text = "Video Player") })
}

@Preview
@Composable
fun TextCard() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "Hello world!")
  }
}

@Composable
fun VideoPresenter(){
  val videoViewModel = hiltViewModel<VideoViewModel>()

  when (val videosEvent = videoViewModel.videosEvent.collectAsState().value){
    is VideoEvent.Success -> {
      val videoList = videosEvent.videos
    }

    else -> {

    }
  }
}
