package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

data class Level(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val order: Int = 0,
    val requiredXp: Int = 0,
    val totalLessons: Int = 0,
    val active: Boolean = true
)
