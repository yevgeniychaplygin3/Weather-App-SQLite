package com.example.roomyweather.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ForecastRoomEntity(

    @PrimaryKey
    @ColumnInfo(name = "City")
    val city: String,

    @ColumnInfo(name = "Time Stamp")
    val timeStamp: Long
): java.io.Serializable
