package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(navController: NavController) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var eventDescription by remember { mutableStateOf(TextFieldValue("")) }
    var eventsByDate by remember { mutableStateOf(mapOf<LocalDate, MutableList<String>>()) }
    val currentMonth = selectedDate.month
    val currentYear = selectedDate.year

    val daysInMonth = getDaysInMonth(selectedDate.year, selectedDate.monthValue)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Header with navigation and current month
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                selectedDate = selectedDate.minusMonths(1)
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }
            Text(
                text = "${selectedDate.month} ${selectedDate.year}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            IconButton(onClick = {
                selectedDate = selectedDate.plusMonths(1)
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Days Grid
        CalendarDaysGrid(
            daysInMonth = daysInMonth,
            selectedDate = selectedDate,
            onDateSelected = { newDate ->
                selectedDate = newDate
                eventDescription = TextFieldValue(
                    eventsByDate[newDate]?.joinToString("\n") ?: ""
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Display events for the selected date
        Text(
            text = "Events for ${selectedDate.dayOfMonth}:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            eventsByDate[selectedDate]?.forEach { event ->
                Text(text = event, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Event input section
        Text(
            text = "Add Event:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        BasicTextField(
            value = eventDescription,
            onValueChange = { eventDescription = it },
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (eventDescription.text.isNotEmpty()) {
                        // Add the event to the list for the selected date
                        val updatedEvents = eventsByDate[selectedDate]?.toMutableList() ?: mutableListOf()
                        updatedEvents.add(eventDescription.text)
                        eventsByDate = eventsByDate.toMutableMap().apply {
                            put(selectedDate, updatedEvents)
                        }
                        eventDescription = TextFieldValue("") // Reset the input
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (eventDescription.text.isNotEmpty()) {
                    // Add the event
                    val updatedEvents = eventsByDate[selectedDate]?.toMutableList() ?: mutableListOf()
                    updatedEvents.add(eventDescription.text)
                    eventsByDate = eventsByDate.toMutableMap().apply {
                        put(selectedDate, updatedEvents)
                    }
                    eventDescription = TextFieldValue("") // Reset the field
                }
            },
            modifier = Modifier.padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Event", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDaysGrid(
    daysInMonth: List<LocalDate>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Create a grid for the calendar days (7 columns, one for each day of the week)
    val weeks = daysInMonth.chunked(7)

    Column {
        for (week in weeks) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (day in week) {
                    DayCell(
                        day = day,
                        isSelected = day == selectedDate,
                        onClick = { onDateSelected(day) }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(day: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

// Helper function to get days in the month
@RequiresApi(Build.VERSION_CODES.O)
fun getDaysInMonth(year: Int, month: Int): List<LocalDate> {
    val date = LocalDate.of(year, month, 1)
    val lengthOfMonth = date.lengthOfMonth()
    return (1..lengthOfMonth).map { date.withDayOfMonth(it) }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewCalendarView() {
    CalendarView(navController = rememberNavController())
}
