package com.example.roomyweather.data

class SearchCitiesRepository(private val dao: ForecastDao) {

    suspend fun insertSearchedCity(city: ForecastRoomEntity) = dao.insert(city)

    suspend fun deleteSearchedCity(city: ForecastRoomEntity) = dao.delete(city)

    fun getAllSearchedCities() = dao.getAllCities()

    fun getSearchedCityByName(name: String) = dao.getCityByName(name)
}