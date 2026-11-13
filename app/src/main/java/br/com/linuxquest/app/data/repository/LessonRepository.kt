package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.Lesson
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FirebaseFirestore

class LessonRepository(
    private val db: FirebaseFirestore = FirestoreProvider.db
) {
    suspend fun getLessonsByLevelId(levelId: String): Result<List<Lesson>> = runCatching {
        val snapshot = db.collection(FirestoreProvider.COLLECTION_LESSONS)
            .whereEqualTo("levelId", levelId)
            .orderBy("order")
            .get()
            .awaitTask()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Lesson::class.java)?.let { lesson ->
                if (lesson.id.isEmpty()) lesson.copy(id = doc.id) else lesson
            }
        }
    }

    suspend fun getLessonById(id: String): Result<Lesson?> = runCatching {
        val doc = db.collection(FirestoreProvider.COLLECTION_LESSONS)
            .document(id)
            .get()
            .awaitTask()

        if (doc.exists()) {
            doc.toObject(Lesson::class.java)?.let { lesson ->
                if (lesson.id.isEmpty()) lesson.copy(id = doc.id) else lesson
            }
        } else {
            null
        }
    }
}
