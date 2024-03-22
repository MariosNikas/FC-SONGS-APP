package com.example.app.presentation.home

import android.accounts.Account
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.presentation.util.GoogleDriveSharedDataStore
import com.example.fcsongsappthesis.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val application: Application,
    //private val sharedDataStore: GoogleDriveSharedDataStore,
) : ViewModel() {

    private val _videoUrls = MutableLiveData<List<String>>()
    val videoUrls: LiveData<List<String>> = _videoUrls

    private val _signInStatus = MutableLiveData<Boolean>(false)
    val signInStatus: LiveData<Boolean> = _signInStatus

//    private val _googleAccount = MutableLiveData<Boolean>(sharedDataStore.googleAccountId != null)
//    val googleAccount: LiveData<Boolean> = _signInStatus


    fun getDrive() {
        viewModelScope.launch {
            val videoUrls = withContext(Dispatchers.IO) {
                val context = application.applicationContext
                val credential = GoogleAccountCredential.usingOAuth2(
                    getApplication(context), listOf(
                        "https://www.googleapis.com/auth/drive.readonly",
                        "https://www.googleapis.com/auth/userinfo.profile"
                    )
                ).apply {
                    selectedAccount = GoogleSignIn.getLastSignedInAccount(context)?.account!!
                }

                val driveService = Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                    .setApplicationName(context.getString(R.string.app_name))
                    .build()

                val request = driveService.files().list().setFields("files(id, name, mimeType, webContentLink)")
                val fileList = request.execute()
                fileList.files.filter { it.mimeType == "video/mp4"}//.map{ it.webViewLink }
            }
            _videoUrls.value = videoUrls.map { it.webContentLink }
        }
    }


    fun buttonPress(){ //TODO

    }

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

    fun handleSignInResult(success : Boolean) {
        _signInStatus.value = success
    }

    fun setGoogleAccount(account: GoogleSignInAccount) {
        Log.d("googleSignInAccount", account.id + account.idToken + account.serverAuthCode + account.requestedScopes + account.grantedScopes )
        Log.d("Account", account.account.toString())
        //sharedDataStore.googleAccountId = account.id

    }

}