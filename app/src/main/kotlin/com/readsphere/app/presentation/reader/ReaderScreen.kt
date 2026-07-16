package com.readsphere.app.presentation.reader

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readsphere.app.core.theme.ReadingColors
import com.readsphere.app.core.theme.ReadingMode
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.FileType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    documentId: String,
    onBack: () -> Unit,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(documentId) {
        viewModel.loadDocument(documentId)
    }

    val bgColor = when (state.readingMode) {
        ReadingMode.Light -> ReadingColors.LightBackground
        ReadingMode.Dark -> ReadingColors.DarkBackground
        ReadingMode.TrueBlack -> ReadingColors.TrueBlackBackground
        ReadingMode.Sepia -> ReadingColors.SepiaBackground
        ReadingMode.HighContrast -> ReadingColors.HighContrastBackground
    }

    val textColor = when (state.readingMode) {
        ReadingMode.Light -> ReadingColors.LightText
        ReadingMode.Dark -> ReadingColors.DarkText
        ReadingMode.TrueBlack -> ReadingColors.TrueBlackText
        ReadingMode.Sepia -> ReadingColors.SepiaText
        ReadingMode.HighContrast -> ReadingColors.HighContrastText
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = textColor)
        }
        return
    }

    if (state.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = textColor.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error ?: "Unknown error",
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onBack) {
                    Text("Go Back")
                }
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .systemBarsPadding()
    ) {
        // Document content based on file type
        when (state.document?.fileType) {
            FileType.PDF -> PdfReaderContent(
                document = state.document!!,
                currentPage = state.currentPage,
                readingMode = state.readingMode,
                onPageChanged = { viewModel.navigateToPage(it) }
            )
            FileType.DOCX -> DocxReaderContent(
                document = state.document!!,
                readingMode = state.readingMode
            )
            FileType.PPTX -> PptxReaderContent(
                document = state.document!!,
                currentPage = state.currentPage,
                readingMode = state.readingMode,
                onSlideChanged = { viewModel.navigateToPage(it) }
            )
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Unsupported file type",
                        color = textColor
                    )
                }
            }
        }

        // Top controls bar
        AnimatedVisibility(
            visible = state.showControls,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it })
        ) {
            ReaderTopBar(
                title = state.document?.title ?: "Document",
                currentPage = state.currentPage,
                totalPages = state.totalPages,
                isFavorite = state.document?.isFavorite ?: false,
                showSearch = state.showSearch,
                onBack = onBack,
                onToggleFavorite = { viewModel.toggleFavorite() },
                onToggleSearch = { viewModel.toggleSearch() },
                onShare = { viewModel.shareDocument() }
            )
        }

        // Bottom controls bar
        AnimatedVisibility(
            visible = state.showControls,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ReaderBottomBar(
                currentPage = state.currentPage,
                totalPages = state.totalPages,
                readingMode = state.readingMode,
                isFullscreen = state.isFullscreen,
                isBookmarked = state.bookmarks.any { it.pageNumber == state.currentPage },
                onPreviousPage = { viewModel.previousPage() },
                onNextPage = { viewModel.nextPage() },
                onPageSelected = { viewModel.navigateToPage(it) },
                onReadingModeChanged = { viewModel.setReadingMode(it) },
                onToggleFullscreen = { viewModel.toggleFullscreen() },
                onBookmark = { viewModel.addBookmark() }
            )
        }

        // Search overlay
        if (state.showSearch) {
            SearchOverlay(
                query = state.searchQuery,
                onQueryChanged = { viewModel.setSearchQuery(it) },
                onClose = { viewModel.toggleSearch() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderTopBar(
    title: String,
    currentPage: Int,
    totalPages: Int,
    isFavorite: Boolean,
    showSearch: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleSearch: () -> Unit,
    onShare: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                Text(
                    text = "Page ${currentPage + 1} of $totalPages",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            if (!showSearch) {
                IconButton(onClick = onToggleSearch) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onShare) {
                Icon(Icons.Filled.Share, contentDescription = "Share")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    )
}

@Composable
private fun ReaderBottomBar(
    currentPage: Int,
    totalPages: Int,
    readingMode: ReadingMode,
    isFullscreen: Boolean,
    isBookmarked: Boolean,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onPageSelected: (Int) -> Unit,
    onReadingModeChanged: (ReadingMode) -> Unit,
    onToggleFullscreen: () -> Unit,
    onBookmark: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Reading mode toggle
            ReadingModeDropdown(
                currentMode = readingMode,
                onModeSelected = onReadingModeChanged
            )

            // Previous page
            IconButton(
                onClick = onPreviousPage,
                enabled = currentPage > 0
            ) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous page")
            }

            // Page indicator
            Text(
                text = "Page ${currentPage + 1} of $totalPages",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )

            // Next page
            IconButton(
                onClick = onNextPage,
                enabled = currentPage < totalPages - 1
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "Next page")
            }

            // Bookmark
            IconButton(onClick = onBookmark) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Fullscreen
            IconButton(onClick = onToggleFullscreen) {
                Icon(
                    imageVector = if (isFullscreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
                    contentDescription = "Toggle fullscreen"
                )
            }
        }
    }
}

@Composable
private fun ReadingModeDropdown(
    currentMode: ReadingMode,
    onModeSelected: (ReadingMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = when (currentMode) {
                    ReadingMode.Light -> Icons.Filled.LightMode
                    ReadingMode.Dark -> Icons.Filled.DarkMode
                    ReadingMode.TrueBlack -> Icons.Filled.ModeNight
                    ReadingMode.Sepia -> Icons.Filled.WbTwilight
                    ReadingMode.HighContrast -> Icons.Filled.Contrast
                },
                contentDescription = "Reading mode"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ReadingMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(mode.label)
                            if (mode == currentMode) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchOverlay(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChanged,
                placeholder = { Text("Search in document…") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null)
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close search")
            }
        }
    }
}

// Placeholder composables for document readers
@Composable
private fun PdfReaderContent(
    document: Document,
    currentPage: Int,
    readingMode: ReadingMode,
    onPageChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.PictureAsPdf,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PDF Reader",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Page ${currentPage + 1}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Swipe up/down to navigate pages",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DocxReaderContent(
    document: Document,
    readingMode: ReadingMode
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Description,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Word Document Reader",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${document.title}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Scroll vertically to read content",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PptxReaderContent(
    document: Document,
    currentPage: Int,
    readingMode: ReadingMode,
    onSlideChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Slideshow,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Presentation Viewer",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Slide ${currentPage + 1} of ${document.pageCount}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Swipe left/right to navigate slides",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
