package br.com.linuxquest.app.ui.screens.command

import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommandDetailViewModel(
    private val pageId: String,
    private val repository: DictionaryRepository = DictionaryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CommandDetailUiState>(CommandDetailUiState.Loading)
    val uiState: StateFlow<CommandDetailUiState> = _uiState.asStateFlow()

    init {
        loadCommand()
    }

    fun loadCommand() {
        viewModelScope.launch {
            _uiState.value = CommandDetailUiState.Loading

            repository.getManPage(pageId).fold(
                onSuccess = { page ->
                    val sections = page.sections
                        .filter { it.id != EXAMPLES_SECTION_ID }
                        .map { DetailSection(id = it.id, title = it.title) }

                    _uiState.value = CommandDetailUiState.Success(page = page, sections = sections)

                    if (page.sections.any { it.id == EXAMPLES_SECTION_ID }) loadExamples()
                    sections
                        .filter { it.id in EXPANDED_BY_DEFAULT }
                        .forEach { toggleSection(it.id) }
                },
                onFailure = { throwable ->
                    _uiState.value = CommandDetailUiState.Error(
                        throwable.localizedMessage ?: "Erro ao carregar comando"
                    )
                }
            )
        }
    }

    // Expande/recolhe uma seção, buscando o conteúdo na API apenas na primeira abertura
    fun toggleSection(sectionId: String) {
        val section = currentSuccess()?.sections?.find { it.id == sectionId } ?: return
        val shouldLoad = !section.expanded &&
            (section.content is SectionContentState.NotLoaded || section.content is SectionContentState.Error)

        updateSection(sectionId) { it.copy(expanded = !it.expanded) }
        if (!shouldLoad) return

        updateSection(sectionId) { it.copy(content = SectionContentState.Loading) }
        viewModelScope.launch {
            repository.getSection(pageId, sectionId).fold(
                onSuccess = { content ->
                    updateSection(sectionId) {
                        it.copy(content = SectionContentState.Loaded(stripSectionTitle(content?.html.orEmpty())))
                    }
                },
                onFailure = { throwable ->
                    updateSection(sectionId) {
                        it.copy(
                            content = SectionContentState.Error(
                                throwable.localizedMessage ?: "Erro ao carregar seção"
                            )
                        )
                    }
                }
            )
        }
    }

    private suspend fun loadExamples() {
        repository.getSection(pageId, EXAMPLES_SECTION_ID).onSuccess { content ->
            val examples = parseExamples(content?.html.orEmpty())
            _uiState.update { state ->
                if (state is CommandDetailUiState.Success) state.copy(examples = examples) else state
            }
        }
    }

    private fun currentSuccess(): CommandDetailUiState.Success? =
        _uiState.value as? CommandDetailUiState.Success

    private fun updateSection(sectionId: String, transform: (DetailSection) -> DetailSection) {
        _uiState.update { state ->
            if (state is CommandDetailUiState.Success) {
                state.copy(sections = state.sections.map { if (it.id == sectionId) transform(it) else it })
            } else {
                state
            }
        }
    }

    private companion object {
        const val EXAMPLES_SECTION_ID = "Examples_(TL;DR)"
        val EXPANDED_BY_DEFAULT = setOf("Synopsis", "Description")

        // Cada exemplo do TL;DR vem como <li><span>descrição</span> <code>comando</code></li>
        val EXAMPLE_REGEX = Regex("<li><span>(.*?)</span>\\s*<code>(.*?)</code></li>", RegexOption.DOT_MATCHES_ALL)
        val SECTION_TITLE_REGEX = Regex("^\\s*<section>\\s*<h2>.*?</h2>", RegexOption.DOT_MATCHES_ALL)

        fun parseExamples(html: String): List<CommandExample> =
            EXAMPLE_REGEX.findAll(html).map { match ->
                CommandExample(
                    description = htmlToText(match.groupValues[1]).removeSuffix(":"),
                    command = htmlToText(match.groupValues[2])
                )
            }.toList()

        // O título já aparece no cabeçalho do card, então é removido do HTML
        fun stripSectionTitle(html: String): String = html.replaceFirst(SECTION_TITLE_REGEX, "<section>")

        fun htmlToText(html: String): String =
            HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim()
    }
}
