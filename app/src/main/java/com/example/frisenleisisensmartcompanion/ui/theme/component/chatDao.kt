package com.example.frisenleisisensmartcompanion.ui.theme.component

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.frisenleisisensmartcompanion.ui.theme.component.Chat

@Dao
interface ChatDao {
    @Insert
    suspend fun insertChat(chat: Chat)

    @Query("SELECT * FROM chat_history ORDER BY timestamp DESC")
    suspend fun getAllChats(): List<Chat>

    @Query("DELETE FROM chat_history") // This will clear all records
    suspend fun deleteAllChats()

    @Query("DELETE FROM chat_history WHERE id = :chatId")
    suspend fun deleteChat(chatId: Int)
}