package com.readsphere.app.presentation.reader

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readsphere.app.core.theme.ReadingMode
import com.readsphere.app.data.local.file.DocumentParser
import com.readsphere.app.data.local.file.FileCacheManager
import com.readsphere.app.domain.model.*
import com.readsphere.app.domain.repository.AnnotationRepository
import com.readsphere.app.domain.repository.BookmarkRepository
import com.readsphere.app.domain.repository.DocumentRepository
import com.readsphere.app.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReaderUiState(
    val document: Document? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val bookmarks: List<Bookmark> = emptyList(),
    val annotations: List<Annotation> = emptyList(),
    val showControls: Boolean = true,
    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val readingMode: ReadingMode = ReadingMode.Light,
    val isFullscreen: Boolean = false,
    val isReadingAloud: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ReaderViewModel @Inject constructor(
    application: Application,
    private val documentRepository: DocumentRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val annotationRepository: AnnotationRepository,
    private val preferenceRepository: PreferenceRepository,
    private val documentParser: DocumentParser,
    private val fileCacheManager: FileCacheManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    private var documentId: String? = null

    fun loadDocument(docId: String) {
        documentId = docId
        viewModelScope.launch {
            documentRepository.getDocumentById(docId).collect { doc ->
                if (doc != null) {
                    _uiState.update {
                        it.copy(
                            document = doc,
                            currentPage = doc.currentPage,
                            totalPages = doc.pageCount.coerceAtLeast(1),
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Document not found") }
                }
            }
        }

        // Load bookmarks
        viewModelScope.launch {
            bookmarkRepository.getBookmarksForDocument(docId).collect { bookmarks ->
                _uiState.update { it.copy(bookmarks = bookmarks) }
            }
        }

        // Load annotations
        viewModelScope.launch {
            annotationRepository.getAnnotationsForDocument(docId).collect { annotations ->
                _uiState.update { it.copy(annotations = annotations) }
            }
        }

        // Load reading preferences
        viewModelScope.launch {
            preferenceRepository.preferences.collect { prefs ->
                _uiState.update { it.copy(readingMode = prefs.readingMode, isFullscreen = prefs.fullscreenMode) }
            }
        }
    }

    fun navigateToPage(page: Int) {
        val total = _uiState.value.totalPages
        val target = page.coerceIn(0, total - 1)
        _uiState.update { it.copy(currentPage = target) }
        saveProgress(target)
    }

    fun nextPage() {
        navigateToPage(_uiState.value.currentPage + 1)
    }

    fun previousPage() {
        navigateToPage(_uiState.value.currentPage - 1)
    }

    fun toggleControls() {
        _uiState.update { it.copy(showControls = !it.showControls) }
    }

    fun toggleFullscreen() {
        _uiState.update { it.copy(isFullscreen = !it.isFullscreen) }
    }

    fun setReadingMode(mode: ReadingMode) {
        _uiState.update { it.copy(readingMode = mode) }
        viewModelScope.launch { preferenceRepository.updateReadingMode(mode.name) }
    }

    fun toggleSearch() {
        _uiState.update { it.copy(showSearch = !it.showSearch, searchQuery = "") }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addBookmark() {
        val state = _uiState.value
        val docId = state.document?.id ?: return
        viewModelScope.launch {
            val isBookmarked = bookmarkRepository.isPageBookmarked(docId, state.currentPage)
            if (!isBookmarked) {
                bookmarkRepository.insertBookmark(
                    Bookmark(
                        documentId = docId,
                        pageNumber = state.currentPage,
                        title = "Page ${state.currentPage + 1}"
                    )
                )
            }
        }
    }

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmark(bookmark)
        }
    }

    fun toggleFavorite() {
        val docId = _uiState.value.document?.id ?: return
        viewModelScope.launch {
            documentRepository.toggleFavorite(docId)
        }
    }

    fun shareDocument() {
        val doc = _uiState.value.document ?: return
        val context = getApplication<Application>()
        val file = fileCacheManager.createSharedFile(doc.filePath, "${doc.title}.pdf")
        file?.let {
            context.shareFile(it.absolutePath, doc.fileType.mimeType)
        }
    }

    private fun saveProgress(page: Int) {
        val docId = documentId ?: return
        val total = _uiState.value.totalPages
        viewModelScope.launch {
            documentRepository.updateReadingProgress(docId, page, total)
        }
    }

    fun reportReadingTime(minutes: Long) {
        val docId = documentId ?: return
        viewModelScope.launch {
            documentRepository.incrementReadingTime(docId, minutes)
        }
    }
}
