package com.example.app.presentation.googleSignIn

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.presentation.home.HomeScreenViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun GoogleSignInScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    moveToNextScreen: () -> Unit
) {
    val context = LocalContext.current
    val loginState by viewModel.signInStatus.observeAsState()
    val loginError = remember { mutableStateOf(false) }


    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    viewModel.setGoogleAccount(task.getResult(ApiException::class.java))
                    viewModel.handleSignInResult(true)
                } catch (e: ApiException) {
                    loginError.value = true
                }
            }
        }

    LaunchedEffect(Unit) {
        Log.d("sign", "sign")
    }

    LaunchedEffect(key1 = loginError.value, key2 = loginState) {
        if (loginError.value && viewModel.signInStatus.value == false) {
            Toast.makeText(
                context,
                "There was an Error In the Login. Try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (loginState == false) {
                    startForResult.launch(viewModel.launchGoogleSignIn())
                } else moveToNextScreen()
            }
        ) {
            Text(text = "Sign In To Google")
        }
    }
}