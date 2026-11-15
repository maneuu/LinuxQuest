package br.com.linuxquest.app.ui.screens.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.model.ManSearchResult
import br.com.linuxquest.app.data.repository.DictionaryRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class DictionaryViewModel(
    private val repository: DictionaryRepository = DictionaryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    // Resultados crus da API; o filtro é aplicado localmente sem nova requisição
    private var lastResults: List<ManSearchResult> = emptyList()

    init {
        viewModelScope.launch {
            _uiState
                .map { it.query.trim() }
                .distinctUntilChanged()
                .debounce(SEARCH_DEBOUNCE_MS)
                .collectLatest { query -> search(query) }
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onFilterSelected(filter: DictionaryFilter) {
        _uiState.update { it.copy(filter = filter) }
        if (lastResults.isNotEmpty()) publishResults()
    }

    fun retry() {
        viewModelScope.launch { search(_uiState.value.query.trim()) }
    }

    private suspend fun search(query: String) {
        if (query.isEmpty()) {
            lastResults = emptyList()
            _uiState.update { it.copy(resultState = DictionaryResultState.Idle) }
            return
        }

        _uiState.update { it.copy(resultState = DictionaryResultState.Loading) }

        repository.searchCommands(query).fold(
            onSuccess = { results ->
                lastResults = results
                publishResults()
            },
            onFailure = { throwable ->
                lastResults = emptyList()
                _uiState.update {
                    it.copy(
                        resultState = DictionaryResultState.Error(
                            throwable.localizedMessage ?: "Erro ao buscar comandos"
                        )
                    )
                }
            }
        )
    }

    private fun publishResults() {
        _uiState.update { state ->
            val filtered = lastResults.filter { state.filter.matches(it) }
            state.copy(
                resultState = if (filtered.isEmpty()) {
                    DictionaryResultState.Empty
                } else {
                    DictionaryResultState.Success(filtered)
                }
            )
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 400L
    }
}
