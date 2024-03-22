package com.example.app.presentation.uiKit

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(uri: String) {
    val context = LocalContext.current
    val exoPlayer = remember(context) {
            ExoPlayer.Builder(context).setRenderersFactory(
                DefaultRenderersFactory(context).setEnableDecoderFallback(true)
            ).build().apply {
                setMediaItem(
                    MediaItem.fromUri(uri)
                )
                prepare()
                playWhenReady = false
            }

    }



    AndroidView({ context ->
        PlayerView(context).apply {
            player = exoPlayer
            setShutterBackgroundColor(Color.Transparent.value.toInt())
            setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
        }
    }, update = { view ->
        view.player = exoPlayer
    })
}
