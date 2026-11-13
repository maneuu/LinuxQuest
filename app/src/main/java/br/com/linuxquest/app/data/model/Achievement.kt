package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

data class Achievement(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val requirement: Int = 0,
    val type: String = ""
)
