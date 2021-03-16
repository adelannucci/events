package com.adelannucci.events.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.adelannucci.events.datasource.EventService
import com.adelannucci.events.datasource.remote.CheckIn
import kotlinx.coroutines.Dispatchers

class CheckInRepository(private val service: EventService) {

    fun checkIn(request: CheckIn): LiveData<Boolean> =
        liveData(Dispatchers.IO) {
            emit(service.checkIn(request).isSuccessful)
        }
}