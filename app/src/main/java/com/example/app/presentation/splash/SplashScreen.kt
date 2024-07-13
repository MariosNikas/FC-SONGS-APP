package com.example.app.presentation.splash


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.presentation.shared.SharedViewModel
import com.example.fcsongsappthesis.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    goToNextScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
    splashScreenViewModel: SpashScreenViewModel = hiltViewModel()
) {
    val pulsingInfiniteTransition = rememberInfiniteTransition(label = "PulsingInfiniteTransition")

    val scale by pulsingInfiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    val isDataLoaded by splashScreenViewModel.isReady.collectAsState()
    var moveToNextScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        splashScreenViewModel.onTeamsFetched = { teams ->
            sharedViewModel.fetchTeams(teams)
        }
        delay(3000)
        moveToNextScreen = true
    }

    LaunchedEffect(isDataLoaded, moveToNextScreen) {
        if (isDataLoaded && moveToNextScreen) {
            goToNextScreen()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .scale(scale),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(24.dp)),
                painter = painterResource(id = R.drawable.screenshot_2024_06_20_at_5_25_10pm),
                contentScale = ContentScale.FillHeight,
                contentDescription = "app logo"
            )
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Fc Songs App",
            )
        }
    }
}