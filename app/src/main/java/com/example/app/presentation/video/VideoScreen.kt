package com.example.app.presentation.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.presentation.shared.SharedViewModel
import com.example.app.presentation.uiKit.VideoPlayer
import com.example.app.presentation.uiKit.getTeamColor
import com.example.fcsongsappthesis.R


@Composable
fun VideoScreen(
    viewModel: VideoScreenViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    selectedVideoIndex: Int,
) {
    val selectedTeam = sharedViewModel.getTeam()
    val currentVideo by viewModel.currentVideo.collectAsState()
    val videoIndex = remember { mutableIntStateOf(selectedVideoIndex) }
    val teamColor = getTeamColor(selectedTeam?.id)


    LaunchedEffect(Unit) {
        selectedTeam?.videosCollection?.getOrNull(videoIndex.intValue)?.let {
            viewModel.setVideo(it)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                enabled = videoIndex.intValue - 1 >= 0,
                onClick = {
                    videoIndex.intValue -= 1
                    viewModel.setVideo(selectedTeam?.videosCollection!![videoIndex.intValue])
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                    contentDescription = "left arrow"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = selectedTeam?.teamName + " " + (videoIndex.intValue + 1) + "/" + (selectedTeam?.videosCollection?.size),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                enabled = videoIndex.intValue + 1 <= (selectedTeam?.videosCollection?.size ?: 3),
                onClick = {
                    videoIndex.intValue += 1
                    viewModel.setVideo(selectedTeam?.videosCollection!![videoIndex.intValue])
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.baseline_arrow_circle_right_24),
                    contentDescription = "right arrow"
                )
            }
        }
        currentVideo?.let { video ->
            VideoPlayer(
                uri = video.videoUrl,
                exoPlayer = viewModel.player,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(teamColor ?: Color.LightGray)
                .clickable {
                    viewModel.buttonPress(selectedTeam?.teamName ?: "")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Capture",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}