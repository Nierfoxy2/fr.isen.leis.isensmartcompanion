package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EventView() {
    // Get the current context to use it in the intent
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Your Event-related content
        Text(text = "Event 1")
        Text(text = "Event 2")
        Text(text = "Event 3")

        // Button to launch EventDetailActivity
        Button(onClick = {
            // Launching the EventDetailActivity when clicked
            val intent = Intent(context, EventDetailActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("View Event Details")
        }
    }
}

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Displaying event details (this can be dynamic later)
            Text(text = "Event Details Here")
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