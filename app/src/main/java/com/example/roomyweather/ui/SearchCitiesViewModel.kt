package com.example.roomyweather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.roomyweather.data.AppDatabase
import com.example.roomyweather.data.ForecastDao
import com.example.roomyweather.data.ForecastRoomEntity
import com.example.roomyweather.data.SearchCitiesRepository
import kotlinx.coroutines.launch

class SearchCitiesViewModel(application: Application): AndroidViewModel(application) {

    private val repository = SearchCitiesRepository(AppDatabase.getInstance(application).forecastDao())

    val searchedCities = repository.getAllSearchedCities().asLiveData()
    fun addSearchedCity(city: ForecastRoomEntity){
        viewModelScope.launch { repository.insertSearchedCity(city) }
    }

    fun removeSearchedCity(city: ForecastRoomEntity){
        viewModelScope.launch { repository.insertSearchedCity(city) }

    }

    fun getSearchedCityByName(name: String) =
        repository.getSearchedCityByName(name).asLiveData()
}