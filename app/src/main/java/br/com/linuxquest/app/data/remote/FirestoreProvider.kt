package br.com.linuxquest.app.data.remote

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreProvider {
    val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    const val COLLECTION_LEVELS = "levels"
    const val COLLECTION_LESSONS = "lessons"
    const val COLLECTION_MISSIONS = "missions"
    const val COLLECTION_COMMANDS = "commands"
    const val COLLECTION_ACHIEVEMENTS = "achievements"
}
