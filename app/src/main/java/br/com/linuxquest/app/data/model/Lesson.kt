package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

data class Lesson(
    @DocumentId
    val id: String = "",
    val levelId: String = "",
    val title: String = "",
    val description: String = "",
    val order: Int = 0
)
