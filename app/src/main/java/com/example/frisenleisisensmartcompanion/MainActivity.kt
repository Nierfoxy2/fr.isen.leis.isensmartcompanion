package com.example.frisenleisisensmartcompanion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frisenleisisensmartcompanion.ui.theme.FrisenleisisensmartcompanionTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // setting up the individual tabs
            val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val eventTab = TabBarItem(title = "Event", selectedIcon = Icons.Filled.DateRange, unselectedIcon = Icons.Outlined.DateRange)
            val historyTab = TabBarItem(title = "History", selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List)

            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, eventTab, historyTab)

            // creating our navController
            val navController = rememberNavController()

            FrisenleisisensmartcompanionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(bottomBar = { TabView(tabBarItems, navController) }) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = homeTab.title,
                            modifier = Modifier.padding(innerPadding)) {
                            composable(homeTab.title) {
                                HomeView()
                            }
                            composable(eventTab.title) {
                                EventView()
                            }
                            composable(historyTab.title) {
                                HistoryView()
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------
// This is a wrapper view that allows us to easily and cleanly
// reuse this component in any future project
@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(containerColor = Color.DarkGray) {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {Text(tabBarItem.title)})
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}

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
            modifier = Modifier.size(200.dp).padding(bottom = 8.dp)
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
                        aiResponse = "You asked: '${question.text}'" // Replace with actual AI response
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
        Event(1, "BDE Evening", "An evening of fun and networking with ISEN students.", "March 10, 2025", "ISEN Campus", "Social"),
        Event(2, "Gala", "A formal event with dinner, speeches, and networking.", "April 20, 2025", "Hotel Grand", "Formal"),
        Event(3, "Cohesion Day", "A day to strengthen the bond between students and faculty.", "May 15, 2025", "ISEN Campus", "Social")
    )
}




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



@Composable
fun HistoryView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Thing 1")
        Text("Thing 2")
        Text("Thing 3")
        Text("Thing 4")
        Text("Thing 5")
    }
}
