package com.data.repository

import com.data.dao.AddictionDao
import com.data.model.Addiction
import com.data.model.AddictionWithRelapses
import com.data.model.Quote
import com.data.model.Relapse
import com.google.ai.client.generativeai.GenerativeModel
import com.network.QuoteApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AddictionRepository(
    private val addictionDao: AddictionDao,
    private val quoteApi: QuoteApiService,
    private val generativeModel: GenerativeModel
) {
    fun getAllAddictions(): Flow<List<AddictionWithRelapses>> =
        addictionDao.getAllAddictionsWithRelapses()

    fun getAddictionById(id: Long): Flow<AddictionWithRelapses?> =
        addictionDao.getAddictionWithRelapsesById(id)

    suspend fun upsertAddiction(addiction: Addiction) = withContext(Dispatchers.IO) {
        addictionDao.upsertAddiction(addiction)
    }

    suspend fun deleteAddiction(addiction: Addiction) = withContext(Dispatchers.IO) {
        addictionDao.deleteAddiction(addiction)
    }

    suspend fun logRelapse(relapse: Relapse) = withContext(Dispatchers.IO) {
        addictionDao.insertRelapse(relapse)
    }

    suspend fun getRandomQuote(): Quote? = withContext(Dispatchers.IO) {
        try {
            val response = quoteApi.getRandomQuote()
            response.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAIAdvice(relapseNote: String): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = "The user is trying to quit an addiction. They just relapsed with this note: '$relapseNote'. Provide a short, empathetic, and encouraging advice to help them get back on track."
            val response = generativeModel.generateContent(prompt)
            response.text ?: getLocalFallbackAdvice(relapseNote)
        } catch (e: Exception) {
            val errorMessage = e.message ?: ""
            if (errorMessage.contains("404") || errorMessage.contains("not found")) {
                "The AI service is currently updating its models. Here is some guidance in the meantime: ${getLocalFallbackAdvice(relapseNote)}"
            } else {
                getLocalFallbackAdvice(relapseNote)
            }
        }
    }

    private fun getLocalFallbackAdvice(note: String): String {
        return "It's okay to have a setback. What matters is that you're aware of it. Take a deep breath and remember why you started this journey. You can do this!"
    }
}
