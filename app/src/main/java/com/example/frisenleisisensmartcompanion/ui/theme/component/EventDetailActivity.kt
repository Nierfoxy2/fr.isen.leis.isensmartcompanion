package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    // Retrieve user preference for notifications from SharedPreferences
    val notificationPreference = remember { mutableStateOf(PreferencesHelper.getNotificationPreference(context)) }
    val coroutineScope = rememberCoroutineScope()
    val notificationId = 1

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

            // Button to toggle notification preference
            Button(
                onClick = {
                    // Toggle the preference for notification (true -> false, false -> true)
                    notificationPreference.value = !notificationPreference.value

                    // Save the new preference to SharedPreferences
                    PreferencesHelper.saveNotificationPreference(context, notificationPreference.value)

                    // If the user has enabled notifications, trigger the notification
                    if (notificationPreference.value) {
                        coroutineScope.launch {
                            delay(10000)  // 10 seconds delay
                            if (notificationPreference.value) {
                            Log.d("EventDetailView", "Notification is about to be triggered.")

                            // Ensure notification channel is created
                            NotificationHelper.createNotificationChannel(context = context)

                            // Show the notification
                            NotificationHelper.showNotification(
                                context = context,
                                title = "Event Reminder",
                                content = "Reminder for your event: $title",
                                notificationId = notificationId // Pass the notification ID
                            )}
                        }
                    } else {
                        // If the user has disabled notifications, cancel the notification
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancel(notificationId)  // Cancel the notification by ID
                        Toast.makeText(context, "Notification cancelled!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Display text based on whether the user wants notifications
                Text(text = if (notificationPreference.value) "Disable Notification" else "Enable Notification")
            }
        }
    }
}