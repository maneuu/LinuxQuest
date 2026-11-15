package br.com.linuxquest.app.data.repository

import br.com.linuxquest.app.data.model.ManPage
import br.com.linuxquest.app.data.model.ManSearchResult
import br.com.linuxquest.app.data.model.ManSectionContent
import br.com.linuxquest.app.data.remote.api.MankierApi
import br.com.linuxquest.app.data.remote.api.RetrofitProvider
import retrofit2.HttpException

class DictionaryRepository(
    private val api: MankierApi = RetrofitProvider.mankierApi
) {
    suspend fun searchCommands(query: String): Result<List<ManSearchResult>> = runCatching {
        api.search(query.trim()).results
    }

    // A API responde 200 com {"error": ...} quando a página não existe
    suspend fun getManPage(pageId: String): Result<ManPage> = runCatching {
        api.getManPage(pageId).also { page ->
            if (page.name.isEmpty()) error("Comando não encontrado no manual")
        }
    }

    // Nem toda man page possui todas as seções (ex: TL;DR), então 404 vira null
    suspend fun getSection(pageId: String, sectionId: String): Result<ManSectionContent?> = runCatching {
        try {
            api.getSection(pageId, sectionId)
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
    }
}
