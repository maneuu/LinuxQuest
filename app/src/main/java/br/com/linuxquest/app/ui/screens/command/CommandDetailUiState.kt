package br.com.linuxquest.app.ui.screens.command

import br.com.linuxquest.app.data.model.ManPage

data class CommandExample(
    val description: String,
    val command: String
)

sealed interface SectionContentState {
    object NotLoaded : SectionContentState
    object Loading : SectionContentState
    data class Loaded(val html: String) : SectionContentState
    data class Error(val message: String) : SectionContentState
}

data class DetailSection(
    val id: String,
    val title: String,
    val expanded: Boolean = false,
    val content: SectionContentState = SectionContentState.NotLoaded
)

sealed interface CommandDetailUiState {
    object Loading : CommandDetailUiState
    data class Success(
        val page: ManPage,
        val examples: List<CommandExample> = emptyList(),
        val sections: List<DetailSection> = emptyList()
    ) : CommandDetailUiState
    data class Error(val message: String) : CommandDetailUiState
}
