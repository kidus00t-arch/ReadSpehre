package com.readsphere.app.presentation.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.readsphere.app.core.common.*
import com.readsphere.app.core.theme.FileTypeColors
import com.readsphere.app.core.util.formatDuration
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.FileType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onDocumentClick: (Document) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // File picker for opening documents
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            // Handle opened document via SAF
            viewModel.refresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ReadSphere",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Your premium document reader",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        documentPickerLauncher.launch(
                            arrayOf(
                                "application/pdf",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                                "application/msword",
                                "application/vnd.ms-powerpoint"
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Open file"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingState(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Stats section
                if (uiState.totalDocuments > 0) {
                    item {
                        StatisticsSection(
                            totalDocuments = uiState.totalDocuments,
                            weeklyReadingMinutes = uiState.weeklyReadingMinutes
                        )
                    }
                }

                // Recent documents
                item {
                    SectionHeader(
                        title = "Recent Documents",
                        action = {
                            TextButton(
                                onClick = { /* Navigate to library */ },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text("See all")
                            }
                        }
                    )
                }

                if (uiState.recentDocuments.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Outlined.Description,
                            title = "No documents yet",
                            message = "Tap + to open a PDF, Word, or PowerPoint file",
                            modifier = Modifier.height(250.dp),
                            action = {
                                FilledTonalButton(
                                    onClick = {
                                        documentPickerLauncher.launch(
                                            arrayOf(
                                                "application/pdf",
                                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                                                "application/msword",
                                                "application/vnd.ms-powerpoint"
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Open Document")
                                }
                            }
                        )
                    }
                } else {
                    items(uiState.recentDocuments) { document ->
                        DocumentCard(
                            document = document,
                            onClick = { onDocumentClick(document) },
                            onFavoriteClick = { viewModel.toggleFavorite(document) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }

                // Favorites section
                if (uiState.favoriteDocuments.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(title = "Favorites")
                    }

                    items(uiState.favoriteDocuments) { document ->
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

@Composable
private fun StatisticsSection(
    totalDocuments: Int,
    weeklyReadingMinutes: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            label = "Documents",
            value = totalDocuments.toString(),
            icon = Icons.Filled.Description,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "Reading Time",
            value = weeklyReadingMinutes.formatDuration(),
            icon = Icons.Filled.Timer,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "This Week",
            value = "${weeklyReadingMinutes / 7}m/day",
            icon = Icons.Filled.TrendingUp,
            modifier = Modifier.weight(1f)
        )
    }
}
