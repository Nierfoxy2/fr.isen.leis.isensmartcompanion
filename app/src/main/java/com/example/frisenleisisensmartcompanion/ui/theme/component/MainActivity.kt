package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frisenleisisensmartcompanion.ui.theme.model.FrisenleisisensmartcompanionTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // setting up the individual tabs
            val homeTab = TabBarItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val eventTab = TabBarItem(
                title = "Event",
                selectedIcon = Icons.Filled.DateRange,
                unselectedIcon = Icons.Outlined.DateRange
            )
            val historyTab = TabBarItem(
                title = "History",
                selectedIcon = Icons.Filled.List,
                unselectedIcon = Icons.Outlined.List
            )
            val calendarTab = TabBarItem(
                title = "Calendar",
                selectedIcon = Icons.Filled.DateRange,
                unselectedIcon = Icons.Outlined.DateRange
            )

            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, eventTab, historyTab, calendarTab)

            // creating our navController
            val navController = rememberNavController()

            FrisenleisisensmartcompanionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(bottomBar = { TabView(tabBarItems, navController) }) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = homeTab.title,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(homeTab.title) {
                                HomeView(navController)
                            }
                            composable(eventTab.title) {
                                EventView(navController)
                            }
                            composable(historyTab.title) {
                                HistoryView(navController)
                            }
                            composable(calendarTab.title) {
                                CalendarView(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
