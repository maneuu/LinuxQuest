package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.User
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
        val user = result.documents.firstOrNull()?.toObject(User::class.java)
            ?: throw Exception("E-mail não encontrado")

        if (user.password != password) {
            throw Exception("Senha incorreta")
        }
        SessionManager.userId = user.id
    }

    suspend fun createAccount(username: String, email: String, password: String) {
        val existing = users.whereEqualTo("email", email).limit(1).get().awaitTask()
        if (!existing.isEmpty) {
            throw Exception("Já existe uma conta com esse e-mail")
        }

        val newUser = User(
            username = username,
            email = email,
            password = password,
            createdAt = System.currentTimeMillis()
        )
        val newDoc = users.add(newUser).awaitTask()
        SessionManager.userId = newDoc.id
    }

    // Busca o usuário completo (null se o documento não existir)
    suspend fun getUser(uid: String): User? {
        val doc = users.document(uid).get().awaitTask()
        return doc.toObject(User::class.java)
    }

    fun logout() {
        SessionManager.userId = null
    }
}
