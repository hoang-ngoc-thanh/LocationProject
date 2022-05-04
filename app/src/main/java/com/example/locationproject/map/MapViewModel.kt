package com.example.locationproject.map

import android.location.Location
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationproject.room.AppDatabase
import com.example.locationproject.room.LocationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val database: AppDatabase) : ViewModel() {
    companion object {
        // 5 minutes tracking one time
        const val TIME_LIMIT_TRACKING = 300000L
    }

    private var listLocation: MutableList<LocationInfo> = mutableListOf()
    val currentLocationLiveData = MutableLiveData<LocationInfo>()
    val previousLocationLiveData = MutableLiveData<MutableList<LocationInfo>>()
    val startTrackingLiveData = MutableLiveData<Unit>()
    private var trackTimer: CountDownTimer? = null

    fun saveCurrentLocation(id: Int, location: Location) {
        val locationInfo = LocationInfo(
            locationId = id,
            time = location.time,
            lat = location.latitude,
            lng = location.longitude
        )
        listLocation.add(locationInfo)

        viewModelScope.launch(Dispatchers.IO) {
            database.locationDao().insertLocation(locationInfo)
        }
        currentLocationLiveData.postValue(locationInfo)
        trackTimer?.cancel()
        startCodeTimeLimit()
    }

    fun fetchListLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            listLocation.clear()
            listLocation.addAll(database.locationDao().getAll())
            previousLocationLiveData.postValue(listLocation)
        }
    }

    fun getListLocationTracked() = listLocation

    private fun startCodeTimeLimit() {
        trackTimer = object : CountDownTimer(TIME_LIMIT_TRACKING, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                startTrackingLiveData.postValue(Unit)
            }
        }
        trackTimer?.start()
    }
}