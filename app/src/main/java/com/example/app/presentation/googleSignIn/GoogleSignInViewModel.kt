package com.example.app.presentation.googleSignIn

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.app.presentation.util.GoogleDriveSharedDataStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val application: Application,
    private val sharedDataStore: GoogleDriveSharedDataStore,
    ) : ViewModel() {
    private val _signInStatus = MutableLiveData<Boolean>(sharedDataStore.googleAccountId != null)
    val signInStatus: LiveData<Boolean> = _signInStatus

    fun launchGoogleSignIn(): Intent {
        val context = application.applicationContext
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope("https://www.googleapis.com/auth/drive.readonly"),
                Scope("https://www.googleapis.com/auth/userinfo.profile")
            )
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult() {
        _signInStatus.value = sharedDataStore.googleAccountId != null
    }

    fun setGoogleAccount(account: GoogleSignInAccount) {
        sharedDataStore.googleAccountId = account.email
    }
}