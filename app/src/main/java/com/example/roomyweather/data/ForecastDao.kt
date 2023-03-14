package com.example.roomyweather.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecast: ForecastRoomEntity)
    // update and read all
    @Delete
    suspend fun delete(forecast: ForecastRoomEntity)


    @Query("SELECT * FROM ForecastRoomEntity")
    fun getAllCities() : Flow<List<ForecastRoomEntity>>

    @Query("SELECT * FROM ForecastRoomEntity WHERE City = :city LIMIT 1")
    fun getCityByName(city: String): Flow<ForecastRoomEntity?>

//    @Query("SELECT * FROM ForecastRoomEntity ORDER BY timeStamp ASC")
//    fun getCityByName(name: String): Flow<ForecastRoomEntity?>
}