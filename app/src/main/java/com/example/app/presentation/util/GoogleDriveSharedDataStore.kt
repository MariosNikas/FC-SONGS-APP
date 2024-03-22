package com.example.app.presentation.util

import android.content.Context
import javax.inject.Inject

class GoogleDriveSharedDataStore @Inject constructor(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("GoogleDriveSharedDataStore", Context.MODE_PRIVATE)

    var googleAccountId: String? = null
        get() {
            return sharedPreferences.getString("accountId", field)
        }
        set(value) {
            sharedPreferences.edit().apply {
                putString("accountId", value)
                apply()
            }
        }
}
