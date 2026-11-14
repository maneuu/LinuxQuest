package br.com.linuxquest.app.ui.screens.mission

import br.com.linuxquest.app.data.model.Mission

enum class MissionStage {
    SCENARIO,
    LEARNING,
    TERMINAL,
    FEEDBACK
}

sealed interface MissionUiState {
    object Loading : MissionUiState
    data class Error(val message: String) : MissionUiState
    data class Success(
        val mission: Mission,
        val currentStage: MissionStage = MissionStage.SCENARIO,
        val userCommandInput: String = "",
        val isCommandCorrect: Boolean = false,
        val showHint: Boolean = false,
        val errorMessage: String? = null
    ) : MissionUiState
}
