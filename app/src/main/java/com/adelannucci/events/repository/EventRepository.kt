package com.adelannucci.events.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.adelannucci.events.model.Event
import com.adelannucci.events.source.EventService
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.SocketTimeoutException

class EventRepository(private val service: EventService) {

    fun findAll(): LiveData<ResultRequest<List<Event>>?> =
        liveData(Dispatchers.IO) {
            val result =
                runService(errorMessage = "Falha ao buscar eventos") {
                    service.findAll()
                }
            emit(result)
        }

    fun findById(id: Long): LiveData<ResultRequest<Event>?> =
        liveData(Dispatchers.IO) {
            val result =
                runService(errorMessage = "Falha ao buscar evento") {
                    service.findById(id)
                }
            emit(result)
        }

    private suspend fun <T> runService(
        errorMessage: String = "Falha na requisição",
        run: suspend () -> Response<T>,
    ): ResultRequest<T>? {
        return try {
            makeRequest(run(), errorMessage)
        } catch (e: SocketTimeoutException) {
            ResultRequest(error = "Falha ao conectar com o servidor")
        } catch (e: Exception) {
            ResultRequest(error = "Erro desconhecido")
        }
    }

    private fun <T> makeRequest(
        response: Response<T>,
        errorMessage: String? = null,
    ): ResultRequest<T>? {
        if (response.isSuccessful) {
            return response.body()?.let { ResultRequest(it) }
        }
        return ResultRequest(error = errorMessage)
    }
}