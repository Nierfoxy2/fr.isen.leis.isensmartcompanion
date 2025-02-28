package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frisenleisisensmartcompanion.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.os.Handler
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

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
) {val context = LocalContext.current
    Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "📅 $date",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "📍 $location",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "🏷 Category: $category",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Button to trigger notification after 10 seconds
        Spacer(modifier = Modifier.height(16.dp)) // Add some space before the button
        val coroutineScope = rememberCoroutineScope()  // Remember coroutine scope

        Button(
            onClick = {
                // Launch the coroutine with a delay
                coroutineScope.launch {
                    // Delay for 10 seconds before triggering the notification
                    delay(10000)  // 10 seconds delay

                    // Ensure notification channel is created and then show the notification
                    NotificationHelper.createNotificationChannel(context = context)
                    NotificationHelper.showNotification(
                        context = context,
                        title = "Event Reminder",
                        content = "Reminder for your event: $title"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Get Notified")
        }
    }
}
}
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

object NotificationHelper {
    private const val CHANNEL_ID = "event_reminder_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Event Reminders", importance).apply {
                description = "Channel for event reminders"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, content: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Set an appropriate icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
}
