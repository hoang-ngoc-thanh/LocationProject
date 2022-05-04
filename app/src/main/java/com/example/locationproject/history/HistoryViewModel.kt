package com.example.locationproject.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationproject.room.AppDatabase
import com.example.locationproject.room.LocationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val appDatabase: AppDatabase): ViewModel() {

    private var listLocation: MutableList<LocationInfo> = ArrayList()
    var locationLiveData: MutableLiveData<MutableList<LocationInfo>> = MutableLiveData<MutableList<LocationInfo>>()

    fun fetchListLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            listLocation.clear()
            listLocation.addAll(appDatabase.locationDao().getAll())
            locationLiveData.postValue(listLocation)
        }
    }
}