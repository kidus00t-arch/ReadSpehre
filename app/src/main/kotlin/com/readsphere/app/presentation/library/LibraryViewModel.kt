package com.readsphere.app.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.FileFilter
import com.readsphere.app.domain.model.SortOrder
import com.readsphere.app.domain.repository.DocumentRepository
import com.readsphere.app.domain.usecase.GetDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryUiState(
    val documents: List<Document> = emptyList(),
    val selectedFilter: FileFilter = FileFilter.All,
    val selectedSort: SortOrder = SortOrder.DateModified,
    val isGridView: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private val _filter = MutableStateFlow(FileFilter.All)
    private val _sortOrder = MutableStateFlow(SortOrder.DateModified)

    init {
        loadDocuments()
    }

    private fun loadDocuments() {
        viewModelScope.launch {
            combine(_filter, _sortOrder) { filter, sort ->
                Pair(filter, sort)
            }.collectLatest { (filter, sort) ->
                getDocumentsUseCase.getDocumentsByType(filter, sort).collect { documents ->
                    _uiState.update {
                        it.copy(
                            documents = documents,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun setFilter(filter: FileFilter) {
        _filter.value = filter
        _uiState.update { it.copy(selectedFilter = filter, isLoading = true) }
    }

    fun setSortOrder(sort: SortOrder) {
        _sortOrder.value = sort
        _uiState.update { it.copy(selectedSort = sort, isLoading = true) }
    }

    fun toggleGridView() {
        _uiState.update { it.copy(isGridView = !it.isGridView) }
    }

    fun toggleFavorite(document: Document) {
        viewModelScope.launch {
            documentRepository.toggleFavorite(document.id)
        }
    }
}
