package com.adelannucci.events.datasource

import com.adelannucci.events.datasource.remote.CheckIn
import com.adelannucci.events.datasource.remote.Event
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {

    @GET("events")
    suspend fun findAll(): Response<List<Event>>

    @GET("events/{id}")
    suspend fun findById(
        @Path("id") id: Long
    ): Response<Event>

    @POST("checkin")
    suspend fun checkIn(
        @Body request: CheckIn
    ): Response<ResponseBody>
}