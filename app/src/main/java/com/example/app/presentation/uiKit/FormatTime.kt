package com.example.app.presentation.uiKit

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatSecondsToMinutes(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}