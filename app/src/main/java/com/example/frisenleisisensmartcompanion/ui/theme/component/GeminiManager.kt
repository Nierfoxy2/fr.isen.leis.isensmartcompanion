package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.content.Context
import com.example.frisenleisisensmartcompanion.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiManager(context: Context) {

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun generateContent(prompt: String): Flow<GenerateContentResponse> = flow {
        val response = generativeModel.generateContent(prompt)
        emit(response)
    }
}