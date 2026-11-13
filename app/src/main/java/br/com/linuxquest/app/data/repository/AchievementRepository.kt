package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.Achievement
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FirebaseFirestore

class AchievementRepository(
    private val db: FirebaseFirestore = FirestoreProvider.db
) {
    suspend fun getAchievements(): Result<List<Achievement>> = runCatching {
        val snapshot = db.collection(FirestoreProvider.COLLECTION_ACHIEVEMENTS)
            .get()
            .awaitTask()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Achievement::class.java)?.let { achievement ->
                if (achievement.id.isEmpty()) achievement.copy(id = doc.id) else achievement
            }
        }
    }

    suspend fun getAchievementById(id: String): Result<Achievement?> = runCatching {
        val doc = db.collection(FirestoreProvider.COLLECTION_ACHIEVEMENTS)
            .document(id)
            .get()
            .awaitTask()

        if (doc.exists()) {
            doc.toObject(Achievement::class.java)?.let { achievement ->
                if (achievement.id.isEmpty()) achievement.copy(id = doc.id) else achievement
            }
        } else {
            null
        }
    }
}
