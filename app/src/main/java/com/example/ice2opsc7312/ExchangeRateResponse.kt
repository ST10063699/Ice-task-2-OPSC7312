package com.example.ice2opsc7312

data class ExchangeRateResponse(
    val base: String,
    val rates: Map<String, Double>
)
