package com.example.app.domain

import com.example.app.data.repositories.IFirestoreRepository
import com.example.app.models.TeamInformation
import javax.inject.Inject

class GetTeamsUseCase @Inject constructor(private val repository: IFirestoreRepository) {
    suspend operator fun invoke(): List<TeamInformation> {
        return repository.getTeams()
    }
}