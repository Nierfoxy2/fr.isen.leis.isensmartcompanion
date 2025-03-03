package com.example.frisenleisisensmartcompanion.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frisenleisisensmartcompanion.ui.theme.component.AppDatabase
import com.example.frisenleisisensmartcompanion.ui.theme.component.Chat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryView(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Access the database
    val db = AppDatabase.getDatabase(context)
    val chatDao = db.chatDao()

    var chatHistory by remember { mutableStateOf<List<Chat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch chat history on screen launch
    LaunchedEffect(Unit) {
        scope.launch {
            chatHistory = chatDao.getAllChats()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "Clear History" Button
        Button(
            onClick = {
                scope.launch {
                    chatDao.deleteAllChats()  // Delete all chats from the database
                    chatHistory = emptyList() // Update UI after clearing history
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Effacer l'historique", color = MaterialTheme.colorScheme.onError)
        }

        // Title
        Text(
            text = "Historique des Chats",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Loading Indicator or Chat List
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            ChatList(chatHistory)
        }
    }
}



@Composable
fun ChatList(chatHistory: List<Chat>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {       items(chatHistory) { chat ->
        ChatItem(chat = chat)
    }
    }
}


@Composable
fun ChatItem(chat: Chat) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Ajouter un comportement au clic si nécessaire
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Question de chat
            Text(
                text = "Question: ${chat.question}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Réponse de chat sous la question
            Text(
                text = "Reponse: ${chat.answer}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp) // Espacement entre la question et la réponse
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Affichage de la date de chat
            Text(
                text = "Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date(chat.timestamp))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

