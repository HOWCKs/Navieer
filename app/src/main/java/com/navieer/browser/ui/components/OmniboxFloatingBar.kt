package com.navieer.browser.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.data.SearchSuggestion

@Composable
fun OmniboxFloatingBar(
    url: String,
    isLoading: Boolean,
    tabCount: Int,
    isDesktopMode: Boolean,
    isAdBlockActive: Boolean,
    isVisible: Boolean,
    suggestions: List<SearchSuggestion>,
    onQueryChanged: (String) -> Unit,
    onNavigate: (String) -> Unit,
    onReload: () -> Unit,
    onStop: () -> Unit,
    onToggleDesktop: () -> Unit,
    onToggleReaderMode: () -> Unit,
    onOpenSiteInfo: () -> Unit,
    onOpenTabs: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onOpenDownloads: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenFlags: () -> Unit,
    onAddBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf(url) }
    var showMenu by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(url) {
        if (!isEditing) {
            textInput = url
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(250)) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(250)) + fadeOut(),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            // Pill Shape Floating Container (M3 Expressive)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(29.dp), clip = false),
                shape = RoundedCornerShape(29.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Security Lock Icon -> Triggers Site Security & Permissions Modal Sheet
                    IconButton(
                        onClick = onOpenSiteInfo,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    ) {
                        val isHttps = url.startsWith("https://")
                        val isLocal = url.startsWith("navieer://") || url.isEmpty()
                        Icon(
                            imageVector = if (isLocal) Icons.Default.Explore
                            else if (isHttps) Icons.Default.Lock
                            else Icons.Default.LockOpen,
                            contentDescription = "Informações e Segurança do Site",
                            tint = if (isLocal) MaterialTheme.colorScheme.primary
                            else if (isHttps) Color(0xFF16A34A)
                            else Color(0xFFDC2626),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Reader Mode Button (Direct in Omnibox)
                    if (url.isNotBlank() && !url.startsWith("navieer://")) {
                        IconButton(
                            onClick = onToggleReaderMode,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Article,
                                contentDescription = "Modo Leitura",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    // URL Input / Omnibox Text with Smart Autocomplete Trigger
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = {
                            textInput = it
                            isEditing = true
                            onQueryChanged(it)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .padding(horizontal = 4.dp),
                        singleLine = true,
                        placeholder = {
                            Text(
                                "Pesquisar ou digitar URL",
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                isEditing = false
                                focusManager.clearFocus()
                                onNavigate(textInput)
                            }
                        ),
                        trailingIcon = {
                            if (isEditing && textInput.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        textInput = ""
                                        onQueryChanged("")
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpar texto",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    // Reload or Stop Button
                    if (isLoading) {
                        IconButton(
                            onClick = onStop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Parar", modifier = Modifier.size(18.dp))
                        }
                    } else if (url.isNotEmpty() && !url.startsWith("navieer://")) {
                        IconButton(
                            onClick = onReload,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Recarregar", modifier = Modifier.size(18.dp))
                        }
                    }

                    // Desktop / Mobile Mode Toggle
                    IconButton(
                        onClick = onToggleDesktop,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isDesktopMode) Icons.Outlined.DesktopWindows else Icons.Outlined.PhoneAndroid,
                            contentDescription = "Alternar Modo Desktop/Mobile",
                            tint = if (isDesktopMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Tab Count Button Badge with smooth squircle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onOpenTabs() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$tabCount",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // More Options Dropdown
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Favoritos") },
                                leadingIcon = { Icon(Icons.Default.Bookmark, null) },
                                onClick = {
                                    showMenu = false
                                    onOpenBookmarks()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Adicionar aos Favoritos") },
                                leadingIcon = { Icon(Icons.Default.Star, null) },
                                onClick = {
                                    showMenu = false
                                    onAddBookmark()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Histórico") },
                                leadingIcon = { Icon(Icons.Default.History, null) },
                                onClick = {
                                    showMenu = false
                                    onOpenHistory()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Downloads") },
                                leadingIcon = { Icon(Icons.Default.Download, null) },
                                onClick = {
                                    showMenu = false
                                    onOpenDownloads()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Segurança do Site") },
                                leadingIcon = { Icon(Icons.Default.Shield, null) },
                                onClick = {
                                    showMenu = false
                                    onOpenSiteInfo()
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Servo Flags (navieer://flags)") },
                                leadingIcon = { Icon(Icons.Default.Flag, null) },
                                onClick = {
                                    showMenu = false
                                    onOpenFlags()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Configurações") },
                                leadingIcon = { Icon(Icons.Default.Settings, null) },
                                onClick = {
                                    showMenu = false
                                    onOpenSettings()
                                }
                            )
                        }
                    }
                }
            }

            // Animated Loading Progress Bar
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 2.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Smart Autocomplete Suggestions Dropdown Card
            if (isEditing && suggestions.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        items(suggestions) { suggestion ->
                            val icon = when (suggestion.typeIcon) {
                                "search" -> Icons.Default.Search
                                "history" -> Icons.Default.History
                                "bookmark" -> Icons.Default.Bookmark
                                else -> Icons.Default.Language
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        isEditing = false
                                        textInput = suggestion.targetUrl
                                        focusManager.clearFocus()
                                        onNavigate(suggestion.targetUrl)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = suggestion.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = suggestion.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.NorthWest,
                                    contentDescription = "Preencher",
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
