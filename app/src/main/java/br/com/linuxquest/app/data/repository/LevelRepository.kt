package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.Level
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FirebaseFirestore

class LevelRepository(
    private val db: FirebaseFirestore = FirestoreProvider.db
) {
    suspend fun getLevels(): Result<List<Level>> = runCatching {
        val snapshot = db.collection(FirestoreProvider.COLLECTION_LEVELS)
            .orderBy("order")
            .get()
            .awaitTask()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Level::class.java)?.let { level ->
                if (level.id.isEmpty()) level.copy(id = doc.id) else level
            }
        }
    }

    suspend fun getLevelById(id: String): Result<Level?> = runCatching {
        val doc = db.collection(FirestoreProvider.COLLECTION_LEVELS)
            .document(id)
            .get()
            .awaitTask()

        if (doc.exists()) {
            doc.toObject(Level::class.java)?.let { level ->
                if (level.id.isEmpty()) level.copy(id = doc.id) else level
            }
        } else {
            null
        }
    }
}
