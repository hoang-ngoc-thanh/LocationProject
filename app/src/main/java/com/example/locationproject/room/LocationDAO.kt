package com.example.locationproject.room

import androidx.room.*

@Dao
interface LocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(vararg users: LocationInfo)

    @Query("SELECT * FROM location_info")
    suspend fun getAll(): MutableList<LocationInfo>

    @Delete
    suspend fun delete(locationInfo: LocationInfo)
}