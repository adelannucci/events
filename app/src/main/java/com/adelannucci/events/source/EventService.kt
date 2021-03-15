package com.adelannucci.events.source

import com.adelannucci.events.model.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventService {

    @GET("events")
    suspend fun findAll(): Response<List<Event>>

    @GET("events/{id}")
    suspend fun findById(
        @Path("id") id: Long
    ): Response<Event>
}