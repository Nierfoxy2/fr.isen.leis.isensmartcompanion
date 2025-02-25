package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.frisenleisisensmartcompanion.ui.theme.component.ui.theme.FrisenleisisensmartcompanionTheme

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve event details passed through the intent
        val eventId = intent.getIntExtra("event_id", -1)
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
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
)