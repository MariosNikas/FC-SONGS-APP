package com.example.app.presentation.uiKit

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    exoPlayer: Player,
    uri: String,
    modifier: Modifier = Modifier,
) {

    var position by rememberSaveable { mutableLongStateOf(-1L) }

    exoPlayer.apply {
        setMediaItem(
            MediaItem.fromUri(uri)
        )
        playWhenReady = false
    }

    val lifecycle = remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner.lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                position = exoPlayer.currentPosition
            }
            lifecycle.value = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        { context ->
            PlayerView(context).apply {
                player = exoPlayer
                setShutterBackgroundColor(Color.Transparent.value.toInt())
                setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
            }
        }, update = {
            when (lifecycle.value) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (position >= 0) {
                        exoPlayer.seekTo(position)
                    }
                    it.onResume()
                    it.player?.playWhenReady = true
                }
                else -> Unit
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    )
}
