package com.example.app.presentation.VideoSelection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.app.presentation.shared.SharedViewModel
import com.example.app.presentation.uiKit.getTeamColor

@Composable
fun VideoSelectionScreen(
    sharedViewModel: SharedViewModel,
    onNavigateToVideoPlayer: (index: Int) -> Unit
) {
    val selectedTeam = sharedViewModel.getTeam()
    var selectedVideoIndex by remember { mutableStateOf<Int?>(null) }

    val teamColor = getTeamColor(selectedTeam?.id)

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .padding(bottom = 40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = selectedTeam?.teamName ?: "loading....",
            style = MaterialTheme.typography.titleMedium,
        )
        selectedTeam?.teamName?.let {
            Text(
                text = "Choose a video to play",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        LazyColumn {
            selectedTeam?.videosCollection?.let {
                items(it.size) { index ->
                    VideoCard(
                        teamName = selectedTeam.teamName,
                        video = it[index],
                        index = index,
                        isSelected = selectedVideoIndex == index,
                        onClick = { selectedVideoIndex = index },
                        selectedColor = teamColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(vertical = 64.dp),
            text = "Make a video selection and then try to click capture when the video glows.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )

        Button(
            onClick = { selectedVideoIndex?.let(onNavigateToVideoPlayer) },
            enabled = selectedVideoIndex != null,
            modifier = Modifier.size(width = 250.dp, height = 60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = teamColor ?: MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                fontSize = TextUnit(24f, TextUnitType.Sp),
                text = "Proceed"
            )
        }
    }
}
