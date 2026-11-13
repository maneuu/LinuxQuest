package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.Command
import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.awaitTask
import com.google.firebase.firestore.FirebaseFirestore

class CommandRepository(
    private val db: FirebaseFirestore = FirestoreProvider.db
) {
    suspend fun getCommands(): Result<List<Command>> = runCatching {
        val snapshot = db.collection(FirestoreProvider.COLLECTION_COMMANDS)
            .get()
            .awaitTask()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Command::class.java)?.let { command ->
                if (command.id.isEmpty()) command.copy(id = doc.id) else command
            }
        }
    }

    suspend fun getCommandById(id: String): Result<Command?> = runCatching {
        val doc = db.collection(FirestoreProvider.COLLECTION_COMMANDS)
            .document(id)
            .get()
            .awaitTask()

        if (doc.exists()) {
            doc.toObject(Command::class.java)?.let { command ->
                if (command.id.isEmpty()) command.copy(id = doc.id) else command
            }
        } else {
            null
        }
    }

    suspend fun getCommandsByCategory(category: String): Result<List<Command>> = runCatching {
        val snapshot = db.collection(FirestoreProvider.COLLECTION_COMMANDS)
            .whereEqualTo("category", category)
            .get()
            .awaitTask()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Command::class.java)?.let { command ->
                if (command.id.isEmpty()) command.copy(id = doc.id) else command
            }
        }
    }
}
