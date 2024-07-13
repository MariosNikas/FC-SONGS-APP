package com.example.app.data.repositories

import com.example.app.models.TeamInformation
import com.example.app.models.dto.VideoTimeDTO

interface IFirestoreRepository {
    suspend fun getTeams(): List<TeamInformation>

    suspend fun addVideoTime(
        videoTimeDTO: VideoTimeDTO
    ): Boolean
}