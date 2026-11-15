package br.com.linuxquest.app.ui.screens.dictionary

import br.com.linuxquest.app.data.model.ManSearchResult

// Filtros baseados nas seções do manual do Linux (man 1, man 8, ...)
enum class DictionaryFilter(val label: String, val sections: Set<Char>) {
    ALL("Todos", emptySet()),
    COMMANDS("Comandos", setOf('1')),
    ADMIN("Admin", setOf('8')),
    CONFIG("Config", setOf('5')),
    PROGRAMMING("Dev", setOf('2', '3'));

    fun matches(result: ManSearchResult): Boolean =
        sections.isEmpty() || result.section.firstOrNull() in sections
}

sealed interface DictionaryResultState {
    object Idle : DictionaryResultState
    object Loading : DictionaryResultState
    data class Success(val results: List<ManSearchResult>) : DictionaryResultState
    object Empty : DictionaryResultState
    data class Error(val message: String) : DictionaryResultState
}

data class DictionaryUiState(
    val query: String = "",
    val filter: DictionaryFilter = DictionaryFilter.COMMANDS,
    val resultState: DictionaryResultState = DictionaryResultState.Idle
)
