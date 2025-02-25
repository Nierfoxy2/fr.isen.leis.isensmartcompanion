package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

@Composable
fun EventDetailView(
    id: Int,
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

fun getFakeEvents(): List<Event> {
    return listOf(
        Event(
            1,
            "BDE Evening",
            "An evening of fun and networking with ISEN students.",
            "March 10, 2025",
            "ISEN Campus",
            "Social"
        ),
        Event(
            2,
            "Gala",
            "A formal event with dinner, speeches, and networking.",
            "April 20, 2025",
            "Hotel Grand",
            "Formal"
        ),
        Event(
            3,
            "Cohesion Day",
            "A day to strengthen the bond between students and faculty.",
            "May 15, 2025",
            "ISEN Campus",
            "Social"
        )
    )
}