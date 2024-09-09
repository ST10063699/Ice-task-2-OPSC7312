package com.example.ice2opsc7312

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

object RetrofitClient {

    private const val BASE_URL = "https://open.er-api.com/"

    private val client = OkHttpClient.Builder()
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: APIInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIInterface::class.java)
    }
}
