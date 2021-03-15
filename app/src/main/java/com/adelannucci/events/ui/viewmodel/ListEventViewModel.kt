package com.adelannucci.events.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.adelannucci.events.repository.EventRepository

class ListEventViewModel(private val repository: EventRepository) : ViewModel() {
    fun findAll() = repository.findAll()
}