package com.example.app.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.fcsongsappthesis.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive



//fun getGoogleSignInClient(context: Context): GoogleSignInClient {
//    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestEmail()
//        .requestScopes(Scope("https://www.googleapis.com/auth/drive.readonly"), Scope("https://www.googleapis.com/auth/userinfo.profile"))
//        .build()
//    val googleClient =  GoogleSignIn.getClient(context, signInOptions)
//    return googleClient
//}
//
//@Composable
//fun startForResult(context: Context) = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//    if (result.resultCode == Activity.RESULT_OK) {
//        val intent = result.data
//        if (result.data != null) {
//            val task: Task<GoogleSignInAccount> =
//                GoogleSignIn.getSignedInAccountFromIntent(intent)
//            task.
//
//        } else {
//            Toast.makeText(context, "Google Login Error!", Toast.LENGTH_LONG).show()
//        }
//    }
//}

object GoogleClient {
    @Composable
    fun LaunchGoogleSignIn(onResult: (ActivityResult) -> Unit) {
        val context = LocalContext.current
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope("https://www.googleapis.com/auth/drive.readonly"),
                Scope("https://www.googleapis.com/auth/userinfo.profile")
            )
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val intent = googleSignInClient.signInIntent

        val startForResult =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    onResult(result)
                } else {
                    Toast.makeText(context, "Google Login Error!", Toast.LENGTH_LONG).show()
                }
            }
        startForResult.launch(intent)
    }

    fun getDrive(context: Context): Drive {
        // get credentials
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(
                "https://www.googleapis.com/auth/drive.readonly",
                "https://www.googleapis.com/auth/userinfo.profile"
            )
        )
        credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(context)?.account!!
        return Drive
            .Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }


//    val fileId = "1P_NaqaDXXoPINHOMvYF375QOwU1qaoPY"
//    val request = getDrive(context = context).files().get(fileId)
//    val file = request.execute()
//    val downloadUrl = file.webContentLink

}