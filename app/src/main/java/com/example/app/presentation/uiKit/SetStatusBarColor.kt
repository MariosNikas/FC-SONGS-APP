package com.example.app.presentation.uiKit

import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
@Composable
fun SetStatusBarColor(color: Color?) {
    if (color !=null) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as android.app.Activity).window
                window.statusBarColor = color.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    color.luminance() > 0.5f
            }
        }
    }
}