package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EventView() {
    val context = LocalContext.current

    // Fake events data
    val events = getFakeEvents()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        // Displaying events using LazyColumn
        LazyColumn {
            items(events) { event ->
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(text = event.title)
                    Text(text = event.date)
                    Text(text = event.location)

                    // Button to navigate to EventDetailActivity
                    Button(onClick = {
                        // Launch EventDetailActivity with the selected event data
                        val intent = Intent(context, EventDetailActivity::class.java).apply {
                            putExtra("event_id", event.id)
                            putExtra("event_title", event.title)
                            putExtra("event_description", event.description)
                            putExtra("event_date", event.date)
                            putExtra("event_location", event.location)
                            putExtra("event_category", event.category)
                        }
                        context.startActivity(intent)
                    }) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}


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