package com.readsphere.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.repository.DocumentRepository
import com.readsphere.app.domain.usecase.GetDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recentDocuments: List<Document> = emptyList(),
    val favoriteDocuments: List<Document> = emptyList(),
    val totalDocuments: Int = 0,
    val weeklyReadingMinutes: Long = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        // Observe recent documents
        viewModelScope.launch {
            getDocumentsUseCase.getRecentDocuments(10).collect { documents ->
                _uiState.update { it.copy(recentDocuments = documents) }
            }
        }

        // Observe favorite documents
        viewModelScope.launch {
            getDocumentsUseCase.getFavoriteDocuments().collect { documents ->
                _uiState.update { it.copy(favoriteDocuments = documents) }
            }
        }

        // Load statistics
        viewModelScope.launch {
            val count = documentRepository.getDocumentCount()
            val weeklyReading = documentRepository.getTotalReadingTimeThisWeek()
            _uiState.update {
                it.copy(
                    totalDocuments = count,
                    weeklyReadingMinutes = weeklyReading,
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavorite(document: Document) {
        viewModelScope.launch {
            documentRepository.toggleFavorite(document.id)
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = true) }
        loadData()
    }
}
