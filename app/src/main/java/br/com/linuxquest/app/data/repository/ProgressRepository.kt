package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.User
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.SessionManager
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

// Progresso do usuário, salvo no próprio documento users/{uid}
// (campos xp, completedMissions e completedLessons do model User).
class ProgressRepository {

    private val db = FirestoreProvider.db

    // Visitante (ninguém logado) ou documento inexistente: User vazio, sem progresso
    suspend fun getProgress(): User {
        val uid = SessionManager.userId ?: return User()

        val doc = db.collection(FirestoreProvider.COLLECTION_USERS)
            .document(uid)
            .get()
            .awaitTask()

        return doc.toObject(User::class.java) ?: User()
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
