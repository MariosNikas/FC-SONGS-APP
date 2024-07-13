package com.example.app.domain

import com.example.app.data.repositories.IFirestoreRepository
import com.example.app.models.dto.VideoTimeDTO
import javax.inject.Inject

class CaptureVideoTimestampUseCase @Inject constructor(private val repository: IFirestoreRepository) {
    suspend operator fun invoke(videoTimeDTO: VideoTimeDTO) : Boolean{
        return repository.addVideoTime(videoTimeDTO)
    }
}