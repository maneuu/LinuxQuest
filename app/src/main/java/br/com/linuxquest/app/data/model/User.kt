package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

// Um usuário, salvo na coleção "users" (documento users/{id}, ex: "user_001").
// Reúne os dados da conta (username, email, password, createdAt)
// e o progresso (xp, completedMissions, completedLessons).
// createdAt pode vir null do banco (ex: documentos criados à mão no console).
data class User(
    @DocumentId
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val createdAt: Long? = null,
    val xp: Int = 0,
    val completedMissions: List<String> = emptyList(),
    val completedLessons: List<String> = emptyList()
)
