package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve event details passed through the intent
        val eventId = intent.getStringExtra("event_id") ?: "Unknown ID"
        val eventTitle = intent.getStringExtra("event_title") ?: "Unknown Title"
        val eventDescription = intent.getStringExtra("event_description") ?: "No description available."
        val eventDate = intent.getStringExtra("event_date") ?: "No date available."
        val eventLocation = intent.getStringExtra("event_location") ?: "No location available."
        val eventCategory = intent.getStringExtra("event_category") ?: "No category available."

        setContent {
            EventDetailView(
                id = eventId,
                title = eventTitle,
                description = eventDescription,
                date = eventDate,
                location = eventLocation,
                category = eventCategory
            )
        }
    }
}
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
)

@Composable
fun EventDetailView(
    id: String,
    title: String,
    description: String,
    date: String,
    location: String,
    category: String
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Event Details", style = MaterialTheme.typography.headlineSmall)
        Text(text = "ID: $id")
        Text(text = "Title: $title")
        Text(text = "Description: $description")
        Text(text = "Date: $date")
        Text(text = "Location: $location")
        Text(text = "Category: $category")
    }
}
interface ApiService {
    @GET("events.json")
    suspend fun getEvents(): List<Event>
}

object RetrofitInstance {

    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

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

