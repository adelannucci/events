package com.adelannucci.events.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.adelannucci.events.R
import com.adelannucci.events.datasource.EventService
import com.adelannucci.events.datasource.ResultRequest
import com.adelannucci.events.datasource.remote.Event
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.SocketTimeoutException

class EventRepository(private val service: EventService) {

    fun findAll(): LiveData<ResultRequest<List<Event>>?> =
        liveData(Dispatchers.IO) {
            val result =
                runService(errorMessage = R.string.failed_fetch_events) {
                    service.findAll()
                }
            emit(result)
        }

    fun findById(id: Long): LiveData<ResultRequest<Event>?> =
        liveData(Dispatchers.IO) {
            val result =
                runService(errorMessage = R.string.failed_fetch_event) {
                    service.findById(id)
                }
            emit(result)
        }

    private suspend fun <Boolean> runService(
        errorMessage: Int = R.string.request_failed,
        run: suspend () -> Response<Boolean>,
    ): ResultRequest<Boolean>? {
        return try {
            makeRequest(run(), errorMessage)
        } catch (e: SocketTimeoutException) {
            ResultRequest(error = R.string.failed_connect_server)
        } catch (e: Exception) {
            ResultRequest(error = R.string.unknown_error)
        }
    }

    private fun <T> makeRequest(
        response: Response<T>,
        errorMessage: Int? = null,
    ): ResultRequest<T>? {
        if (response.isSuccessful) {
            return response.body()?.let { ResultRequest(it) }
        }
        return ResultRequest(error = errorMessage)
    }
}