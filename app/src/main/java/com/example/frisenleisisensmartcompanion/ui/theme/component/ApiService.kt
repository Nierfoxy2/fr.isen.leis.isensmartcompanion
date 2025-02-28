package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("events.json")
    suspend fun getEvents(): List<Event>
}

object RetrofitInstance {

    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

class EventViewModel : ViewModel() {

    // StateFlow to expose the event list to the UI
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    init {
        // Call the API inside a coroutine
        getEventsFromApi()
    }

    private fun getEventsFromApi() {
        viewModelScope.launch {
            try {
                // Run the network request on IO thread
                val eventList = withContext(Dispatchers.IO) {
                    RetrofitInstance.apiService.getEvents()
                }
                _events.value = eventList

                // Log to confirm the data is fetched
                Log.d("EventViewModel", "Events fetched: $eventList")

            } catch (e: Exception) {
                // Handle the error
                Log.e("EventViewModel", "Error fetching events", e)
                _events.value = emptyList() // Or show an error message
            }
        }
    }
}