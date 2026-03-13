package com.example.weather_app.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val base_Url = "https://api.weatherapi.com"

    private val getInstance by lazy {
        Retrofit.Builder().baseUrl(base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weather_Api by lazy {
        getInstance.create(WeatherApi::class.java)
    }
}