package com.adelannucci.events.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.adelannucci.events.datasource.remote.CheckIn
import com.adelannucci.events.repository.CheckInRepository
import com.adelannucci.events.repository.EventRepository

class DialogCheckInViewModel (private val repository: CheckInRepository) : ViewModel() {
    fun checkIn(checkIn: CheckIn) = repository.checkIn(checkIn)
}