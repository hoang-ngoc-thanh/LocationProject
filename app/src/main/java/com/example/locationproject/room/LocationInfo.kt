package com.example.locationproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "location_info")
data class LocationInfo(
    @PrimaryKey val locationId: Int,
    @ColumnInfo(name = "time") val time: Long?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "lng") val lng: Double?
): Serializable