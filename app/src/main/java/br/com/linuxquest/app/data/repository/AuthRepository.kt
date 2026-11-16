package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.remote.FirestoreProvider
import br.com.linuxquest.app.util.SessionManager
import br.com.linuxquest.app.util.awaitTask

// Login simples: confere e-mail e senha direto na coleção "users" do Firestore.
class AuthRepository {

    private val db = FirestoreProvider.db
    private val users get() = db.collection(FirestoreProvider.COLLECTION_USERS)

    // Id do usuário logado agora (ou null se for visitante)
    fun getCurrentUserId(): String? = SessionManager.userId

    suspend fun login(email: String, password: String) {
        val result = users.whereEqualTo("email", email).limit(1).get().awaitTask()
        val doc = result.documents.firstOrNull()
            ?: throw Exception("E-mail não encontrado")

        if (doc.getString("password") != password) {
            throw Exception("Senha incorreta")
        }
        SessionManager.userId = doc.id
    }

    suspend fun createAccount(username: String, email: String, password: String) {
        val existing = users.whereEqualTo("email", email).limit(1).get().awaitTask()
        if (!existing.isEmpty) {
            throw Exception("Já existe uma conta com esse e-mail")
        }

        val userData = hashMapOf(
            "username" to username,
            "email" to email,
            "password" to password,
            "createdAt" to System.currentTimeMillis()
        )
        val newDoc = users.add(userData).awaitTask()
        SessionManager.userId = newDoc.id
    }

    suspend fun getUsername(uid: String): String {
        val doc = users.document(uid).get().awaitTask()
        return doc.getString("username") ?: "user"
    }

    // Data de criação da conta em milissegundos (null se o documento não tiver)
    suspend fun getCreatedAt(uid: String): Long? {
        val doc = users.document(uid).get().awaitTask()
        return doc.getLong("createdAt")
    }

    fun logout() {
        SessionManager.userId = null
    }
}
