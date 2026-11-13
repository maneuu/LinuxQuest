package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

data class Mission(
    @DocumentId
    val id: String = "",
    val lessonId: String = "",
    val title: String = "",
    val scenario: String = "",
    val explanation: String = "",
    val expectedCommand: String = "",
    val hint: String = "",
    val xpReward: Int = 0,
    val order: Int = 0
)
