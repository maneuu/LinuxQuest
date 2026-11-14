package br.com.linuxquest.app.ui.screens.track

import br.com.linuxquest.app.data.model.Lesson
import br.com.linuxquest.app.data.model.Level

data class LevelWithLessons(
    val level: Level,
    val lessons: List<Lesson>
)

sealed interface TrackUiState {
    object Loading : TrackUiState
    data class Success(val levelsWithLessons: List<LevelWithLessons>) : TrackUiState
    data class Error(val message: String) : TrackUiState
    object Empty : TrackUiState
}
