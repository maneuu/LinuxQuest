package br.com.linuxquest.app.ui.screens.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.model.Mission
import br.com.linuxquest.app.data.repository.MissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MissionViewModel(
    private val missionRepository: MissionRepository = MissionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<MissionUiState>(MissionUiState.Loading)
    val uiState: StateFlow<MissionUiState> = _uiState.asStateFlow()

    fun loadMissionForLesson(lessonId: String?) {
        viewModelScope.launch {
            _uiState.value = MissionUiState.Loading

            if (!lessonId.isNullOrEmpty()) {
                missionRepository.getMissionsByLessonId(lessonId).fold(
                    onSuccess = { missions ->
                        val sortedMissions = missions.sortedBy { it.order }
                        if (sortedMissions.isNotEmpty()) {
                            _uiState.value = MissionUiState.Success(
                                mission = sortedMissions.first(),
                                currentStage = MissionStage.SCENARIO
                            )
                        } else {
                            fetchFallbackMission()
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.value = MissionUiState.Error(
                            throwable.localizedMessage ?: "Erro ao carregar missão da lição"
                        )
                    }
                )
            } else {
                fetchFallbackMission()
            }
        }
    }

    fun loadMissionById(missionId: String) {
        viewModelScope.launch {
            _uiState.value = MissionUiState.Loading

            missionRepository.getMissionById(missionId).fold(
                onSuccess = { mission ->
                    if (mission != null) {
                        _uiState.value = MissionUiState.Success(
                            mission = mission,
                            currentStage = MissionStage.SCENARIO
                        )
                    } else {
                        _uiState.value = MissionUiState.Error("Missão não encontrada")
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = MissionUiState.Error(
                        throwable.localizedMessage ?: "Erro ao carregar próxima missão"
                    )
                }
            )
        }
    }

    private suspend fun fetchFallbackMission() {
        missionRepository.getMissionById("mission_01").fold(
            onSuccess = { mission ->
                if (mission != null) {
                    _uiState.value = MissionUiState.Success(
                        mission = mission,
                        currentStage = MissionStage.SCENARIO
                    )
                } else {
                    _uiState.value = MissionUiState.Error("Nenhuma missão disponível")
                }
            },
            onFailure = { throwable ->
                _uiState.value = MissionUiState.Error(
                    throwable.localizedMessage ?: "Erro ao carregar missão"
                )
            }
        )
    }

    fun onCommandInputChanged(input: String) {
        val currentState = _uiState.value
        if (currentState is MissionUiState.Success) {
            _uiState.value = currentState.copy(
                userCommandInput = input,
                errorMessage = null
            )
        }
    }

    fun validateCommand() {
        val currentState = _uiState.value
        if (currentState is MissionUiState.Success) {
            val expected = currentState.mission.steps.terminal.expectedCommand
            val normalizedUser = normalizeCommand(currentState.userCommandInput)
            val normalizedExpected = normalizeCommand(expected)

            if (normalizedUser == normalizedExpected && normalizedUser.isNotEmpty()) {
                _uiState.value = currentState.copy(
                    isCommandCorrect = true,
                    errorMessage = null
                )
            } else {
                _uiState.value = currentState.copy(
                    isCommandCorrect = false,
                    errorMessage = "Comando incorreto. Verifique a sintaxe e tente novamente."
                )
            }
        }
    }

    fun toggleHint() {
        val currentState = _uiState.value
        if (currentState is MissionUiState.Success) {
            _uiState.value = currentState.copy(
                showHint = !currentState.showHint
            )
        }
    }

    fun advanceStage() {
        val currentState = _uiState.value
        if (currentState is MissionUiState.Success) {
            val nextStage = when (currentState.currentStage) {
                MissionStage.SCENARIO -> MissionStage.LEARNING
                MissionStage.LEARNING -> MissionStage.TERMINAL
                MissionStage.TERMINAL -> if (currentState.isCommandCorrect) MissionStage.FEEDBACK else MissionStage.TERMINAL
                MissionStage.FEEDBACK -> MissionStage.FEEDBACK
            }

            _uiState.value = currentState.copy(
                currentStage = nextStage
            )
        }
    }

    fun handleFeedbackCompletion(onFinish: () -> Unit) {
        val currentState = _uiState.value
        if (currentState is MissionUiState.Success) {
            val nextMissionId = currentState.mission.steps.feedback.nextMissionId
            if (nextMissionId.isNotEmpty()) {
                loadMissionById(nextMissionId)
            } else {
                onFinish()
            }
        }
    }

    private fun normalizeCommand(command: String): String {
        return command.trim().replace(Regex("\\s+"), " ")
    }
}
