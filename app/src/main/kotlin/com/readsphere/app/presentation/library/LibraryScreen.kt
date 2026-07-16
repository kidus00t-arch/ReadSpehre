package com.readsphere.app.presentation.library

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.readsphere.app.core.common.*
import com.readsphere.app.domain.model.FileFilter
import com.readsphere.app.domain.model.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onDocumentClick: (com.readsphere.app.domain.model.Document) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Library",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Toggle grid/list view
                    IconButton(onClick = { viewModel.toggleGridView() }) {
                        Icon(
                            imageVector = if (uiState.isGridView)
                                Icons.Filled.ViewList
                            else
                                Icons.Filled.GridView,
                            contentDescription = if (uiState.isGridView) "List view" else "Grid view"
                        )
                    }

                    // Sort menu
                    var showSortMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.Filled.Sort,
                                contentDescription = "Sort"
                            )
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOrder.entries.forEach { sort ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(sort.label)
                                            if (uiState.selectedSort == sort) {
                                                Icon(
                                                    imageVector = Icons.Filled.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        viewModel.setSortOrder(sort)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FileFilter.entries.forEach { filter ->
                    FileTypeChip(
                        fileType = when (filter) {
                            FileFilter.All -> null
                            FileFilter.PDF -> com.readsphere.app.domain.model.FileType.PDF
                            FileFilter.DOCX -> com.readsphere.app.domain.model.FileType.DOCX
                            FileFilter.PPTX -> com.readsphere.app.domain.model.FileType.PPTX
                        },
                        label = filter.label,
                        selected = uiState.selectedFilter == filter,
                        onClick = { viewModel.setFilter(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                LoadingState()
            } else if (uiState.documents.isEmpty()) {
                EmptyState(
                    icon = Icons.Outlined.LibraryBooks,
                    title = "No documents found",
                    message = "Try a different filter or open a new document from the Home screen"
                )
            } else {
                if (uiState.isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.documents) { document ->
                            DocumentCard(
                                document = document,
                                onClick = { onDocumentClick(document) },
                                onFavoriteClick = { viewModel.toggleFavorite(document) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 80.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.documents) { document ->
                            DocumentCard(
                                document = document,
                                onClick = { onDocumentClick(document) },
                                onFavoriteClick = { viewModel.toggleFavorite(document) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
