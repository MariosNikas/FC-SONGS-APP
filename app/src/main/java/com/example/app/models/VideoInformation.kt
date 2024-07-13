package com.example.app.models


data class TeamInformation (
    val id: String = "",
    val teamName : String = "",
    val imageUrl: String = "",
    val videosCollection : List<VideoInformation> = emptyList()
)
data class VideoInformation(
    val id: String = "",
    val duration: Int = 0,
    val index: Int = 0,
    val videoUrl: String = "",
    val sections : List<Int> = listOf()
)


