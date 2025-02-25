package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EventView() {
    val context = LocalContext.current
    val viewModel: EventViewModel = viewModel()

    // Collect events from ViewModel
    val events = viewModel.events.collectAsState(initial = emptyList())

    Log.d("EventView", "Events: ${events.value}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        if (events.value.isEmpty()) {
            Text("No events available", color = Color.White)
        } else {
            LazyColumn {
                items(events.value) { event ->
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(text = event.title)
                        Text(text = event.date)
                        Text(text = event.location)

                        Button(onClick = {
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
}
