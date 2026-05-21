package com.example.addictionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.Addiction
import com.data.model.AddictionWithRelapses
import com.data.model.Quote
import com.data.model.Relapse
import com.data.repository.AddictionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddictionViewModel @Inject constructor(
    private val repository: AddictionRepository
) : ViewModel() {

    // 1. All addictions for the Dashboard
    val allAddictions: StateFlow<List<AddictionWithRelapses>> = 
        repository.getAllAddictions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // 2. The daily motivation quote
    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote.asStateFlow()

    private val _isQuoteLoading = MutableStateFlow(false)
    val isQuoteLoading: StateFlow<Boolean> = _isQuoteLoading.asStateFlow()

    private val _aiAdvice = MutableStateFlow<String?>(null)
    val aiAdvice: StateFlow<String?> = _aiAdvice.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        fetchQuote()
    }

    fun fetchQuote() {
        viewModelScope.launch {
            _isQuoteLoading.value = true
            _quote.value = repository.getRandomQuote()
            _isQuoteLoading.value = false
        }
    }

    // 3. Actions
    fun addAddiction(addiction: Addiction) {
        viewModelScope.launch {
            repository.upsertAddiction(addiction)
        }
    }

    fun logRelapse(addictionId: Long, note: String) {
        viewModelScope.launch {
            val relapse = Relapse(
                addictionId = addictionId,
                note = note
            )
            repository.logRelapse(relapse)
        }
    }

    fun fetchAIAdvice(note: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiAdvice.value = repository.getAIAdvice(note)
            _isAiLoading.value = false
        }
    }

    fun clearAIAdvice() {
        _aiAdvice.value = null
    }

    fun deleteAddiction(addiction: Addiction) {
        viewModelScope.launch {
            repository.deleteAddiction(addiction)
        }
    }
    
    fun getAddictionById(id: Long): StateFlow<AddictionWithRelapses?> {
        return repository.getAddictionById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }
}
