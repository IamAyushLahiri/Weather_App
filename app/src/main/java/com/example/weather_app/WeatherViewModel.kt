package com.example.weather_app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.api.RetrofitInstance
import com.example.weather_app.api.weatherModel
import com.example.weather_app.ui.theme.NetworkResponse
import com.example.weather_app.ui.theme.apiKey
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitInstance.weather_Api
    private val _weatherResult = MutableLiveData<NetworkResponse<weatherModel>>()
     val weatherResult : LiveData<NetworkResponse<weatherModel>> = _weatherResult
    fun getData(city : String){
        _weatherResult.value = NetworkResponse.Loading
       try {
           viewModelScope.launch {
               val response = weatherApi.getWeather(apiKey.apiKeyMain,city)
               if (response.isSuccessful){
                   response.body()?.let {
                       _weatherResult.value = NetworkResponse.Success(it)
                   }
               } else {
                   _weatherResult.value = NetworkResponse.Error("Failed to fetch the data")
               }
           }
       } catch (e : Exception){
           _weatherResult.value = NetworkResponse.Error("Failed to fetch the data")
       }
    }
}