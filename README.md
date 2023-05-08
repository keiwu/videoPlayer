# videoPlayer
This project is a video player play a list of videos from cloud.

The app connect to a local server to get a list of videos from the cloud.
http://localhost:4000/videos (need to change the localhost to your local computer ip address to work)

This project uses jetpack compose.
The Top part is a ExoPlayer and the bottom part is the descripton of the video.
The default control of the ExoPlayer is hidden, instead I customized my own controls for Previous/ Play/ Pause / Next
The Previous control is disabled if it is the first item in the list
The Next control is disabled if it is the last item in the list
The custom control is auto hide after 5 seconds
The custom control will appear if touching the video screen
If the app is not in the foreground, the video is paused

Used Markdown to render the description of the video
