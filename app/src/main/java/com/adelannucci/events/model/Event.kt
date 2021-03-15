package com.adelannucci.events.model

class Event(
    val people: List<String> = listOf(),
    val date: Int,
    val description: String,
    val image: String? = null,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: Long
)