package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.Mission
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FirebaseFirestore

class MissionRepository(
    private val db: FirebaseFirestore = FirestoreProvider.db
) {
    suspend fun getMissionsByLessonId(lessonId: String): Result<List<Mission>> = runCatching {
        val snapshot = db.collection(FirestoreProvider.COLLECTION_MISSIONS)
            .whereEqualTo("lessonId", lessonId)
            .orderBy("order")
            .get()
            .awaitTask()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Mission::class.java)?.let { mission ->
                if (mission.id.isEmpty()) mission.copy(id = doc.id) else mission
            }
        }
    }

    suspend fun getMissionById(id: String): Result<Mission?> = runCatching {
        val doc = db.collection(FirestoreProvider.COLLECTION_MISSIONS)
            .document(id)
            .get()
            .awaitTask()

        if (doc.exists()) {
            doc.toObject(Mission::class.java)?.let { mission ->
                if (mission.id.isEmpty()) mission.copy(id = doc.id) else mission
            }
        } else {
            null
        }
    }
}
