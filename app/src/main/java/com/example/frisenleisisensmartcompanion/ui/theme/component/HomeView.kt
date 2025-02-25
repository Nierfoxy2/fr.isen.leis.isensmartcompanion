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

@Composable
fun HomeView(modifier: Modifier = Modifier) {
    var question by remember { mutableStateOf(TextFieldValue("")) }
    var aiResponse by remember { mutableStateOf("Ask me anything!") }
    val conversation = remember { mutableStateListOf<String>() }

    val context = LocalContext.current // Get the current context for Toast

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title and Logo
        Image(
            painter = painterResource(id = R.drawable.isen), // Replace with your logo resource
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
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
                .weight(1f)  // Take up available space
                .verticalScroll(rememberScrollState()) // Make conversation scrollable
                .padding(bottom = 16.dp)  // Leave space for the input field at the bottom
        ) {
            conversation.forEachIndexed { index, message ->
                val isUserMessage = message.startsWith("You:")
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .padding(8.dp)
                )
            }
        }

        // Input field and button at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp)
        ) {
            TextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Ask a question") },
                modifier = Modifier.weight(1f)
            )


            Button(
                onClick = {
                    if (question.text.isNotBlank()) {
                        //conversation.add("You: ${question.text}")
                        aiResponse =
                            "You asked: '${question.text}'" // Replace with actual AI response
                        conversation.add("AI: $aiResponse")
                        question = TextFieldValue("") // Clear input field

                        Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()

                    }
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text("Send", color = Color.White)
            }
        }
    }
}