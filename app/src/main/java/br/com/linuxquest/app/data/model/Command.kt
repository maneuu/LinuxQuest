package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

data class Command(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val syntax: String = "",
    val example: String = ""
)
