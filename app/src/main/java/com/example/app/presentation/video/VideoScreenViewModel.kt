package com.example.app.presentation.video

import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.example.app.domain.CaptureVideoTimestampUseCase
import com.example.app.models.VideoInformation
import com.example.app.models.dto.VideoTimeDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoScreenViewModel @Inject constructor(
    val player: Player,
    private val captureVideoTimestampUseCase: CaptureVideoTimestampUseCase,
) : ViewModel() {

    private val _currentVideo = MutableStateFlow<VideoInformation?>(null)
    val currentVideo: StateFlow<VideoInformation?> = _currentVideo

    private val _playerPosition = MutableStateFlow(0)
    val playerPosition: StateFlow<Int> = _playerPosition

    init {
        player.prepare()
    }

    fun setVideo(video: VideoInformation) {
        _currentVideo.value = video
    }

    @OptIn(UnstableApi::class)
    fun buttonPress(teamName: String) {
        _playerPosition.value = player.currentPosition.toInt()
        Log.d("PRESS", "buttonPress: $playerPosition")
        viewModelScope.launch {
            currentVideo.value?.let { video ->
                val videoTimeDTO = VideoTimeDTO(
                    currentPosition = getFirebaseTime(),
                    index = video.index + 1,
                    passNum = getPassNumber(),
                    videoId = video.id,
                    teamName = teamName
                )
                captureVideoTimestampUseCase(videoTimeDTO)
            }
        }
    }

    private fun getPassNumber(): Int {
        currentVideo.value?.let { video ->
            (playerPosition.value / 1000).let {
                when {
                    it in video.sections[0]..video.sections[1] -> return 1
                    it in video.sections[1]..video.sections[2] -> return 2
                    it > video.sections[2] -> return 3
                    else -> return 0
                }
            }
        }
        return 0
    }

    private fun getFirebaseTime(): Int {
        currentVideo.value?.let { video ->
            (playerPosition.value / 1000).let {
                when {
                    it in video.sections[0]..video.sections[1] -> return (it - video.sections[0])
                    it in video.sections[1]..video.sections[2] -> return (it - video.sections[1])
                    it > video.sections[2] -> return (it - video.sections[2])
                    else -> return (it - video.sections[0])
                }
            }
        }
        return 0
    }
}