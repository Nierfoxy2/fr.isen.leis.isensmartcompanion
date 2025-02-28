package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.frisenleisisensmartcompanion.ui.theme.model.FrisenleisisensmartcompanionTheme
import com.google.firebase.ktx.Firebase

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
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

            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, eventTab, historyTab)

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
/*
@Entity(tableName = "history_table")
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis() // Store timestamp
)
@Dao
interface InteractionDao {
    @Insert
    suspend fun insert(interaction: Interaction)

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    suspend fun getAllInteractions(): List<Interaction>

    @Delete
    suspend fun delete(interaction: Interaction)

    @Query("DELETE FROM history_table")
    suspend fun deleteAllInteractions()
}

@Database(entities = [Interaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interactionDao(): InteractionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "interactions_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}*/