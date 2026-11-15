package br.com.linuxquest.app.data.remote.api

import br.com.linuxquest.app.data.model.ManPage
import br.com.linuxquest.app.data.model.ManSearchResponse
import br.com.linuxquest.app.data.model.ManSectionContent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MankierApi {

    @GET("mans/")
    suspend fun search(@Query("q") query: String): ManSearchResponse

    @GET("mans/{pageId}")
    suspend fun getManPage(@Path("pageId") pageId: String): ManPage

    @GET("mans/{pageId}/sections/{sectionId}")
    suspend fun getSection(
        @Path("pageId") pageId: String,
        @Path("sectionId") sectionId: String
    ): ManSectionContent
}
