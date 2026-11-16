package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.SessionManager
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

// Progresso do usuário, salvo no próprio documento users/{uid}:
//   xp: Int
//   completedMissions: lista de ids das missões concluídas
//   completedLessons: lista de ids das lições concluídas
class ProgressRepository {

    private val db = FirestoreProvider.db

    data class UserProgress(
        val xp: Int = 0,
        val completedMissions: List<String> = emptyList(),
        val completedLessons: List<String> = emptyList()
    )

    suspend fun getProgress(): UserProgress {
        val uid = SessionManager.userId ?: return UserProgress()

        val doc = db.collection(FirestoreProvider.COLLECTION_USERS)
            .document(uid)
            .get()
            .awaitTask()

        return UserProgress(
            xp = doc.getLong("xp")?.toInt() ?: 0,
            completedMissions = doc.get("completedMissions") as? List<String> ?: emptyList(),
            completedLessons = doc.get("completedLessons") as? List<String> ?: emptyList()
        )
    }

    // Chamado quando o usuário acerta o comando de uma missão
    suspend fun completeMission(missionId: String, lessonId: String, xpReward: Int) {
        // Visitante não salva progresso
        val uid = SessionManager.userId ?: return

        // Se já tinha feito essa missão, não ganha XP de novo
        val progress = getProgress()
        if (progress.completedMissions.contains(missionId)) return

        val changes = mapOf(
            "xp" to FieldValue.increment(xpReward.toLong()),
            "completedMissions" to FieldValue.arrayUnion(missionId),
            "completedLessons" to FieldValue.arrayUnion(lessonId)
        )
        db.collection(FirestoreProvider.COLLECTION_USERS)
            .document(uid)
            .set(changes, SetOptions.merge())
            .awaitTask()
    }
}
