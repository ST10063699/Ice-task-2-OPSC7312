package com.example.ice2opsc7312

import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("v6/latest/USD")
    fun getCurrencies(): Call<ExchangeRateResponse>
}
