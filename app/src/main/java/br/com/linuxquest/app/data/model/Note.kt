package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

// Uma anotação do usuário, salva na coleção "notes".
// userId = id do documento do dono na coleção "users" (ex: "user_001").
// createdAt/updatedAt podem vir null do banco (ex: documentos criados à mão no console).
data class Note(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)
