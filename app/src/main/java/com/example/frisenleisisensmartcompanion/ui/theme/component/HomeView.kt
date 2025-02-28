package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.frisenleisisensmartcompanion.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeView(modifier: Modifier = Modifier) {
    var question by remember { mutableStateOf(TextFieldValue("")) }
    var aiResponse by remember { mutableStateOf("") } // Changed to empty string initially
    val conversation = remember { mutableStateListOf<String>() }

    val context = LocalContext.current // Get the current context for Toast
    //val database = AppDatabase.getDatabase(context)
    //val interactionDao = database.interactionDao()


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title and Logo
        Image(
            painter = painterResource(id = R.drawable.isen),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(180.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = "Smart Companion",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Chat history in a scrollable Column
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // Display initial message if conversation is empty
            if (conversation.isEmpty()) {
                Text(
                    text = "Ask me anything!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .padding(12.dp)
                )
            }
            conversation.forEach { message ->
                val isUserMessage = message.startsWith("You:")
                val backgroundColor = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .background(backgroundColor, shape = MaterialTheme.shapes.medium)
                        .padding(12.dp)
                )
            }
        }

        // Input field and button at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
        ) {
            TextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Ask a question") },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            )

            Button(
                onClick = {
                    if (question.text.isNotBlank()) {
                        val userMessage = "You: ${question.text}"
                        conversation.add(userMessage)
                        // Here we will add the chatbot logic
                        aiResponse = getChatbotResponse(question.text)
                        conversation.add("AI: $aiResponse")
                        question = TextFieldValue("")
                        /*// Save to database
                        val interaction = Interaction(
                            question = question.text,
                            aiResponse = aiResponse
                        )

                        // Save in background thread (using a coroutine)
                        CoroutineScope(Dispatchers.IO).launch {
                            interactionDao.insert(interaction)
                        }*/
                        Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray, // Change button background color
                    contentColor = Color.White  // Change text color
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text("Send")
            }
        }
    }
}

fun getChatbotResponse(userMessage: String): String {
    // Basic chatbot logic for now
    return when {
        userMessage.contains("hello", ignoreCase = true) -> "Hello there! How can I help you today?"
        userMessage.contains("how are you", ignoreCase = true) -> "I'm doing well, thank you for asking!"
        userMessage.contains("event", ignoreCase = true) -> "You can find all the events in the event tab"
        userMessage.contains("history", ignoreCase = true) -> "You can find all the history in the history tab"
        else -> "I'm not sure I understand. Can you rephrase your question?"
    }
}

