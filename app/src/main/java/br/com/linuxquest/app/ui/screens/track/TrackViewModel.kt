package br.com.linuxquest.app.ui.screens.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.repository.LessonRepository
import br.com.linuxquest.app.data.repository.LevelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackViewModel(
    private val levelRepository: LevelRepository = LevelRepository(),
    private val lessonRepository: LessonRepository = LessonRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrackUiState>(TrackUiState.Loading)
    val uiState: StateFlow<TrackUiState> = _uiState.asStateFlow()

    init {
        loadTrackData()
    }

    fun loadTrackData() {
        viewModelScope.launch {
            _uiState.value = TrackUiState.Loading

            levelRepository.getLevels().fold(
                onSuccess = { levels ->
                    val activeLevels = levels.filter { it.active }.sortedBy { it.order }
                    if (activeLevels.isEmpty()) {
                        _uiState.value = TrackUiState.Empty
                        return@launch
                    }

                    val levelWithLessonsList = mutableListOf<LevelWithLessons>()

                    for (level in activeLevels) {
                        val lessonsResult = lessonRepository.getLessonsByLevelId(level.id)
                        val lessons = lessonsResult.getOrDefault(emptyList()).sortedBy { it.order }
                        levelWithLessonsList.add(LevelWithLessons(level, lessons))
                    }

                    if (levelWithLessonsList.isEmpty() || levelWithLessonsList.all { it.lessons.isEmpty() }) {
                        _uiState.value = TrackUiState.Empty
                    } else {
                        _uiState.value = TrackUiState.Success(levelWithLessonsList)
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = TrackUiState.Error(
                        throwable.localizedMessage ?: "Erro ao carregar dados da trilha"
                    )
                }
            )
        }
    }
}
