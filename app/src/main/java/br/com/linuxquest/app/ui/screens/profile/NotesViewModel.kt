package br.com.linuxquest.app.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.model.Note
import br.com.linuxquest.app.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val repository = NoteRepository()

    var notes by mutableStateOf<List<Note>>(emptyList())
    var searchText by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Controle da janela de criar/editar
    var showDialog by mutableStateOf(false)
    var editingNote by mutableStateOf<Note?>(null) // null = nova anotação
    var dialogTitle by mutableStateOf("")
    var dialogContent by mutableStateOf("")

    // Busca com filtro: mostra só as notas que contêm o texto digitado
    fun filteredNotes(): List<Note> {
        if (searchText.isBlank()) return notes

        return notes.filter { note ->
            note.title.contains(searchText, ignoreCase = true) ||
                note.content.contains(searchText, ignoreCase = true)
        }
    }

    fun loadNotes() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                notes = repository.getNotes()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao carregar anotações"
            }
            isLoading = false
        }
    }

    fun openNewNote() {
        editingNote = null
        dialogTitle = ""
        dialogContent = ""
        showDialog = true
    }

    fun openNote(note: Note) {
        editingNote = note
        dialogTitle = note.title
        dialogContent = note.content
        showDialog = true
    }

    fun closeDialog() {
        showDialog = false
    }

    fun saveNote() {
        if (dialogTitle.isBlank()) return

        val note = editingNote
        viewModelScope.launch {
            try {
                if (note == null) {
                    repository.addNote(dialogTitle.trim(), dialogContent.trim())
                } else {
                    repository.updateNote(note.id, dialogTitle.trim(), dialogContent.trim())
                }
                showDialog = false
                loadNotes()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao salvar anotação"
            }
        }
    }

    fun deleteNote() {
        val note = editingNote ?: return
        viewModelScope.launch {
            try {
                repository.deleteNote(note.id)
                showDialog = false
                loadNotes()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao excluir anotação"
            }
        }
    }
}
