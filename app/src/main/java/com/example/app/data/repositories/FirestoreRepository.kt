package com.example.app.data.repositories

import android.util.Log
import com.example.app.models.TeamInformation
import com.example.app.models.VideoInformation
import com.example.app.models.dto.VideoTimeDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore,
    firebaseInstallations: FirebaseInstallations
) : IFirestoreRepository {

    private lateinit var installationId: String

    init {
        // Asynchronously retrieve the installation ID
        firebaseInstallations.id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                installationId = task.result
            } else {
                installationId = ""
            }
        }
    }

    override suspend fun getTeams(): List<TeamInformation> {
        return try {
            val snapshot = db.collection("Teams Collection")
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                val teamName = document.getString("team Name") ?: ""
                val imageUrl = document.getString("image Url") ?: ""
                val videoCollection = document.reference
                    .collection("Videos Collection")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { videoDocument ->
                        val index = videoDocument.getLong("index")?.toInt() ?: 0
                        val videoUrl = videoDocument.getString("video Url") ?: ""
                        val duration = videoDocument.getLong("duration")?.toInt() ?: 0
                        val sections = videoDocument.get("sections") as? List<Int> ?: emptyList()

                        VideoInformation(
                            id = videoDocument.id,
                            duration = duration,
                            index = index,
                            videoUrl = videoUrl,
                            sections = sections
                        )
                    }.sortedBy { it.index }
                TeamInformation(
                    id = document.id,
                    teamName = teamName,
                    imageUrl = imageUrl,
                    videosCollection = videoCollection
                )
            }.also { Log.d("FirestoreRepository", "Teams: $it") }
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "Error getting documents: ", e)
            emptyList()
        }
    }

    override suspend fun addVideoTime(
        videoTimeDTO: VideoTimeDTO
    ): Boolean {
        return try {
            val videoTime = hashMapOf(
                "installationId" to installationId,
                "videoId" to videoTimeDTO.videoId,
                "time" to videoTimeDTO.currentPosition,
                "pass Number" to videoTimeDTO.passNum,
                "team Name" to videoTimeDTO.teamName,
                "video Number" to videoTimeDTO.index,
            )
            val task = db.collection("videoTimes")
                .document("ButtonPress")
                .collection("sessions")
                .add(videoTime)
                .await()
            Log.d("VideoRepository", "DocumentSnapshot added with ID: ${task.id}")
            true
        } catch (e: Exception) {
            Log.w("VideoRepository", "Error adding document", e)
            false
        }
    }
}