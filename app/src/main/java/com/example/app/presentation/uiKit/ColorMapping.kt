package com.example.app.presentation.uiKit

import androidx.compose.ui.graphics.Color

fun getTeamColor(teamId: String?): Color? {
    return when (teamId) {
        "Aris" -> Color(0xFFE6D39A)
        "Olumpiakos" -> Color(0xFFC65D59)
        "Panathinaikos" -> Color(0xFF77A87B)
        "Paok" -> Color(0xFF606060)
        else -> {
            null
        }
    }
}