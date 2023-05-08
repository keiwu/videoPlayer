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
Used Retrofit for downloading the list of videos
Used Dagger-hilt for dependency injection
Used ExoPlayer for play video



------------------------------
 1. Display a screen similar to the provided wireframe. The screen should
    contain a video player at the top and a scrollable details section at the
    bottom.
 2. Import and use the provided image assets in `assets/` for the media
    controls. For Android, use the provided SVG files. For iOS, use the provided PDF files.
 3. Fetch a list of videos from the provided API (see instructions below for
    running the API).
 4. Sort the received list of videos by date.
 5. Load the first video into the UI by default.
 6. Implement the play/pause button for the video player. The app should be
    paused on startup.
 7. Implement next/previous buttons for the video player. Clicking next should
    update the UI with the next video and video details. Buttons should be
    insensitive when at the start/end of the list.
 8. In the details section, show the returned description for the current video
    as rendered Markdown.
 9. In the details section, also display the title and author of the current
    video.
    -------------------------------------------------------

Getting Started With the Server Backend (/server)
-----------------------------------------------
For this exercise a pre-built server application is provided. The application runs by default on `localhost:4000` and has the following endpoints:

 - `http://localhost:4000/videos` - returns a JSON-encoded array of videos.

### Running the Server

The provided API server is needed as a data source for your project. To run the server you will need NodeJS and Yarn.

![](docs/apple.svg) On macOS you can install the requirements using Homebrew ([installation instructions](https://brew.sh/)) with:

```sh
brew install node yarn
```

![](docs/linux.svg) On Linux, use your distribution’s package manager to install Node JS and Yarn. Node 10 or greater is required. You may need to add repositories:

 - https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions
 - https://classic.yarnpkg.com/en/docs/install/#debian-stable

![](docs/windows.svg) On Windows, the best option is to use package installers from:

 - https://nodejs.org/en/download/, and
 - https://classic.yarnpkg.com/en/docs/install/#windows-stable

With dependencies installed, you can run the server with:

```sh
cd server
yarn install
yarn start
```

You can verify the API is working by visiting http://localhost:4000/videos in your browser or another HTTP client.
