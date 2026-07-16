package com.readsphere.app.presentation.settings

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.readsphere.app.core.theme.AccentColor
import com.readsphere.app.core.theme.ReadingMode
import com.readsphere.app.core.theme.ThemeMode
import com.readsphere.app.core.util.Constants
import com.readsphere.app.domain.model.MarginSize
import com.readsphere.app.domain.model.ReadingPreferences
import com.readsphere.app.domain.model.ScrollMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    preferences: ReadingPreferences,
    onUpdatePreferences: (ReadingPreferences) -> Unit
) {
    var selectedThemeMode by remember { mutableStateOf(preferences.themeMode) }
    var selectedReadingMode by remember { mutableStateOf(preferences.readingMode) }
    var selectedAccentColor by remember { mutableStateOf(preferences.accentColor) }
    var useDynamicColors by remember { mutableStateOf(preferences.useDynamicColors) }
    var fontSize by remember { mutableFloatStateOf(preferences.fontSize.toFloat()) }
    var lineSpacing by remember { mutableFloatStateOf(preferences.lineSpacing) }
    var selectedMargin by remember { mutableStateOf(preferences.marginSize) }
    var selectedScrollMode by remember { mutableStateOf(preferences.scrollMode) }
    var autoNightMode by remember { mutableStateOf(preferences.autoNightMode) }
    var blueLightFilter by remember { mutableStateOf(preferences.blueLightFilter) }
    var fullscreenMode by remember { mutableStateOf(preferences.fullscreenMode) }
    var showAppearanceSettings by remember { mutableStateOf(false) }
    var showReadingSettings by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Appearance Section
            item {
                SettingsSectionHeader(
                    icon = Icons.Outlined.Palette,
                    title = "Appearance",
                    expanded = showAppearanceSettings,
                    onToggle = { showAppearanceSettings = !showAppearanceSettings }
                )
            }

            if (showAppearanceSettings) {
                // Theme mode
                item {
                    SettingsDropdown(
                        label = "Theme",
                        value = selectedThemeMode.label,
                        icon = Icons.Outlined.DarkMode,
                        options = ThemeMode.entries.map { it.label },
                        onOptionSelected = { label ->
                            val mode = ThemeMode.entries.find { it.label == label }
                            mode?.let {
                                selectedThemeMode = it
                                viewModel.updateThemeMode(it)
                            }
                        }
                    )
                }

                // Dynamic colors
                item {
                    SettingsSwitch(
                        title = "Dynamic Colors",
                        subtitle = "Use Material You dynamic colors (Android 12+)",
                        icon = Icons.Outlined.AutoAwesome,
                        checked = useDynamicColors,
                        onCheckedChange = {
                            useDynamicColors = it
                            viewModel.updateDynamicColors(it)
                        }
                    )
                }

                // Accent color
                if (!useDynamicColors) {
                    item {
                        Text(
                            text = "Accent Color",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AccentColor.entries.forEach { color ->
                                val isSelected = selectedAccentColor == color
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    FilledIconButton(
                                        onClick = {
                                            selectedAccentColor = color
                                            viewModel.updateAccentColor(color)
                                        },
                                        modifier = Modifier.size(if (isSelected) 36.dp else 32.dp),
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = color.primary
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = color.label,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Reading mode
                item {
                    SettingsDropdown(
                        label = "Default Reading Mode",
                        value = selectedReadingMode.label,
                        icon = Icons.Outlined.BrightnessMedium,
                        options = ReadingMode.entries.map { it.label },
                        onOptionSelected = { label ->
                            val mode = ReadingMode.entries.find { it.label == label }
                            mode?.let {
                                selectedReadingMode = it
                                viewModel.updateReadingMode(it)
                            }
                        }
                    )
                }

                // Fullscreen mode
                item {
                    SettingsSwitch(
                        title = "Fullscreen Mode",
                        subtitle = "Hide status bar when reading",
                        icon = Icons.Outlined.Fullscreen,
                        checked = fullscreenMode,
                        onCheckedChange = {
                            fullscreenMode = it
                            viewModel.updateFullscreenMode(it)
                        }
                    )
                }
            }

            // Reading Preferences Section
            item {
                SettingsSectionHeader(
                    icon = Icons.Outlined.ChromeReaderMode,
                    title = "Reading Preferences",
                    expanded = showReadingSettings,
                    onToggle = { showReadingSettings = !showReadingSettings }
                )
            }

            if (showReadingSettings) {
                // Font size
                item {
                    SettingsSlider(
                        title = "Font Size",
                        value = fontSize,
                        onValueChange = { fontSize = it },
                        onValueChangeFinished = {
                            viewModel.updateFontSize(fontSize.toInt())
                        },
                        valueRange = 12f..28f,
                        steps = 7,
                        prefix = "${fontSize.toInt()}sp"
                    )
                }

                // Line spacing
                item {
                    SettingsSlider(
                        title = "Line Spacing",
                        value = lineSpacing,
                        onValueChange = { lineSpacing = it },
                        onValueChangeFinished = {
                            viewModel.updateLineSpacing(lineSpacing)
                        },
                        valueRange = 1.0f..2.5f,
                        steps = 5,
                        prefix = "%.1f".format(lineSpacing)
                    )
                }

                // Margin size
                item {
                    SettingsDropdown(
                        label = "Margin Size",
                        value = selectedMargin.label,
                        icon = Icons.Outlined.HorizontalSplit,
                        options = MarginSize.entries.map { it.label },
                        onOptionSelected = { label ->
                            val margin = MarginSize.entries.find { it.label == label }
                            margin?.let {
                                selectedMargin = it
                                viewModel.updateMarginSize(it)
                            }
                        }
                    )
                }

                // Scroll mode
                item {
                    SettingsDropdown(
                        label = "Scroll Mode",
                        value = selectedScrollMode.label,
                        icon = Icons.Outlined.SwipeVertical,
                        options = ScrollMode.entries.map { it.label },
                        onOptionSelected = { label ->
                            val mode = ScrollMode.entries.find { it.label == label }
                            mode?.let {
                                selectedScrollMode = it
                                viewModel.updateScrollMode(it)
                            }
                        }
                    )
                }

                // Auto night mode
                item {
                    SettingsSwitch(
                        title = "Auto Night Mode",
                        subtitle = "Switch to dark mode automatically based on time",
                        icon = Icons.Outlined.NightlightRound,
                        checked = autoNightMode,
                        onCheckedChange = {
                            autoNightMode = it
                            viewModel.updateAutoNightMode(it)
                        }
                    )
                }

                // Blue light filter
                item {
                    SettingsSwitch(
                        title = "Blue Light Filter",
                        subtitle = "Reduce eye strain with warm colors",
                        icon = Icons.Outlined.LightMode,
                        checked = blueLightFilter,
                        onCheckedChange = {
                            blueLightFilter = it
                            viewModel.updateBlueLightFilter(it)
                        }
                    )
                }
            }

            // About Section
            item {
                SettingsSectionHeader(
                    icon = Icons.Outlined.Info,
                    title = "About",
                    expanded = showAbout,
                    onToggle = { showAbout = !showAbout }
                )
            }

            if (showAbout) {
                item {
                    SettingsInfoItem(
                        title = "Version",
                        value = Constants.APP_VERSION
                    )
                }

                item {
                    SettingsInfoItem(
                        title = "Developer",
                        value = "ReadSphere Team"
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ReadSphere is a premium document reader supporting PDF, DOCX, and PPTX files. All processing is done locally on your device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    icon: ImageVector,
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))
    TextButton(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdown(
    label: String,
    value: String,
    icon: ImageVector,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(value)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(option)
                                if (option == value) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    prefix: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = prefix,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            valueRange = valueRange,
            steps = steps
        )
    }
}

@Composable
private fun SettingsInfoItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
