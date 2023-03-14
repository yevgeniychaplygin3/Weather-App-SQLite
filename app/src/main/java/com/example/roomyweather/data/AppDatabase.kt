package com.example.roomyweather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ForecastRoomEntity::class], version = 1 )
abstract class AppDatabase : RoomDatabase() {

    abstract  fun forecastDao(): ForecastDao

    companion object{
        // volatile ensures that any writes to this property
        // are immediately visible to all the threads
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "forecastCities.db"
            ).build()

        //
        fun getInstance(context: Context): AppDatabase{
            // if two threads get to this the same time,
            // double check that if the instance is null to
            // prevent creating another instance of it.
            return instance ?: synchronized(this){
                // also is used to take the return value of buildDatabase
                // and assign it to instance in addition to returning it
                // from getInstance
                instance ?: buildDatabase(context).also{
                    instance = it
                }
            }
        }
    }
}