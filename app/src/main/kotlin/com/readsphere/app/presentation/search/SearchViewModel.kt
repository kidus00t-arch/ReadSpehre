package com.readsphere.app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.DocumentSearchResult
import com.readsphere.app.domain.repository.DocumentRepository
import com.readsphere.app.domain.usecase.SearchDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<DocumentSearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchDocumentsUseCase: SearchDocumentsUseCase,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            if (query.isNotBlank()) {
                _uiState.update { it.copy(isSearching = true, hasSearched = true) }
                searchDocumentsUseCase(query).collect { results ->
                    _uiState.update {
                        it.copy(
                            results = results,
                            isSearching = false
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        results = emptyList(),
                        isSearching = false,
                        hasSearched = false
                    )
                }
            }
        }
    }

    fun toggleFavorite(document: Document) {
        viewModelScope.launch {
            documentRepository.toggleFavorite(document.id)
        }
    }
}
