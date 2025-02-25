package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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