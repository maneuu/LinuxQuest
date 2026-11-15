package br.com.linuxquest.app.data.model

// Modelos da API REST do Mankier (https://www.mankier.com/api)

data class ManSearchResponse(
    val q: String = "",
    val truncated: Boolean = false,
    val results: List<ManSearchResult> = emptyList()
)

data class ManSearchResult(
    val name: String = "",
    val section: String = "",
    val description: String = "",
    val url: String = ""
) {
    // Identificador usado pela API nas rotas de detalhe, ex: "tar.1"
    val pageId: String get() = "$name.$section"
}

data class ManPage(
    val name: String = "",
    val section: String = "",
    val description: String = "",
    val url: String = "",
    val sections: List<ManSectionRef> = emptyList()
)

data class ManSectionRef(
    val id: String = "",
    val title: String = "",
    val url: String = "",
    val sections: List<ManSectionRef> = emptyList()
)

data class ManSectionContent(
    val id: String = "",
    val title: String = "",
    val html: String = ""
)
