package com.example.app.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.presentation.uiKit.VideoPlayer
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val videoIsReady = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewModel.getDrive()
    }

    val videoUrls by viewModel.videoUrls.observeAsState()

    LaunchedEffect(key1 = videoUrls) {
        Log.d("gay", videoUrls.toString())
        if (videoUrls.isNullOrEmpty().not() ) {
            videoIsReady.value = true
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (videoIsReady.value) {
            VideoPlayer(
                uri = ("https://drive.usercontent.google.com/download?id=17DrEVm3tRmLwW5RrEmZDsSsBkzP0L7Aa&export=download&authuser=0&confirm=t&uuid=726ac4ff-d0b5-45a7-b61e-4df4984d9e3e")
                        //viewModel.videoUrls.value?.get(0)!!),
            )
        } else {
            Box(modifier = Modifier.weight(0.7f)) {
                Text(text = "Video is Loading....")
            }
        }
        Button(onClick = { viewModel.buttonPress() }) {
            Text(text = "capture")
        }
    }
}