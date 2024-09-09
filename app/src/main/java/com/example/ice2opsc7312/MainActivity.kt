package com.example.ice2opsc7312

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner)
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner)
        amountEditText = findViewById(R.id.amountEditText)
        resultTextView = findViewById(R.id.resultTextView)
        val convertButton = findViewById<Button>(R.id.convertButton)

        // Fetch and populate currencies
        fetchCurrencies { currencies ->
            val currencyList = currencies.keys.toList()
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fromCurrencySpinner.adapter = adapter
            toCurrencySpinner.adapter = adapter
        }

        // Set up button click listener
        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun fetchCurrencies(callback: (Map<String, Double>) -> Unit) {
        val call = RetrofitClient.instance.getCurrencies()
        call.enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                if (response.isSuccessful) {
                    val currencies = response.body()?.rates ?: emptyMap()
                    callback(currencies)
                } else {
                    // Handle the error
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                // Handle the failure
                println("Failure: ${t.message}")
            }
        })
    }

    private fun convertCurrency() {
        val amountString = amountEditText.text.toString()
        if (amountString.isEmpty()) {
            resultTextView.text = "Please enter an amount."
            return
        }

        val amount = amountString.toDouble()
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()

        fetchCurrencies { currencies ->
            val fromRate = currencies[fromCurrency] ?: 1.0
            val toRate = currencies[toCurrency] ?: 1.0
            val result = (amount / fromRate) * toRate
            resultTextView.text = String.format("%.2f %s", result, toCurrency)
        }
    }
}
