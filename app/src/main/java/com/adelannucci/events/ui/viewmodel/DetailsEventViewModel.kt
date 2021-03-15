package com.adelannucci.events.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.adelannucci.events.repository.EventRepository

class DetailsEventViewModel(private val repository: EventRepository) : ViewModel() {
    fun findById(id: Long) = repository.findById(id)
}
