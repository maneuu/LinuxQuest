package br.com.linuxquest.app.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.repository.AuthRepository
import br.com.linuxquest.app.data.repository.NoteRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class ProfileViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val noteRepository = NoteRepository()

    var isLoggedIn by mutableStateOf(false)
    var username by mutableStateOf("")
    var memberSince by mutableStateOf("")
    var notesCount by mutableIntStateOf(0)

    fun loadProfile() {
        val uid = authRepository.getCurrentUserId()

        if (uid == null) {
            isLoggedIn = false
            return
        }

        isLoggedIn = true

        viewModelScope.launch {
            try {
                username = authRepository.getUsername(uid)

                // Ano em que a conta foi criada
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = authRepository.getCreatedAt(uid) ?: System.currentTimeMillis()
                memberSince = calendar.get(Calendar.YEAR).toString()

                notesCount = noteRepository.getNotes().size
            } catch (e: Exception) {
                username = "user"
            }
        }
    }

    fun logout() {
        authRepository.logout()
        isLoggedIn = false
        username = ""
        notesCount = 0
    }
}
