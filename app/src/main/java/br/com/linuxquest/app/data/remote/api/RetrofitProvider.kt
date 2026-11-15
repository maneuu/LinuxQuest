package br.com.linuxquest.app.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    private const val MANKIER_BASE_URL = "https://www.mankier.com/api/v2/"

    val mankierApi: MankierApi by lazy {
        Retrofit.Builder()
            .baseUrl(MANKIER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MankierApi::class.java)
    }
}
