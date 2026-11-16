package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.Note
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.SessionManager
import br.com.linuxquest.app.util.awaitTask

// CRUD das anotações no Cloud Firestore.
// Todas ficam na coleção "notes"; cada uma tem o campo userId do dono.
class NoteRepository {

    private val db = FirestoreProvider.db
    private val notes get() = db.collection(FirestoreProvider.COLLECTION_NOTES)

    private fun currentUserId(): String {
        return SessionManager.userId ?: throw Exception("Você precisa estar logado")
    }

    // READ: só as anotações do usuário logado, da mais nova para a mais antiga
    suspend fun getNotes(): List<Note> {
        val snapshot = notes
            .whereEqualTo("userId", currentUserId())
            .get()
            .awaitTask()
        return snapshot.toObjects(Note::class.java)
            .sortedByDescending { it.createdAt ?: 0L }
    }

    // CREATE
    suspend fun addNote(title: String, content: String) {
        val now = System.currentTimeMillis()
        val note = Note(
            userId = currentUserId(),
            title = title,
            content = content,
            createdAt = now,
            updatedAt = now
        )
        notes.add(note).awaitTask()
    }

    // UPDATE
    suspend fun updateNote(id: String, title: String, content: String) {
        val changes = mapOf(
            "title" to title,
            "content" to content,
            "updatedAt" to System.currentTimeMillis()
        )
        notes.document(id).update(changes).awaitTask()
    }

    // DELETE
    suspend fun deleteNote(id: String) {
        notes.document(id).delete().awaitTask()
    }
}
