package com.navieer.browser.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Shield
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

@Composable
fun OmniboxFloatingBar(
    url: String,
    isLoading: Boolean,
    tabCount: Int,
    isDesktopMode: Boolean,
    isAdBlockActive: Boolean,
    isVisible: Boolean,
    onNavigate: (String) -> Unit,
    onReload: () -> Unit,
    onStop: () -> Unit,
    onToggleDesktop: () -> Unit,
    onToggleReaderMode: () -> Unit,
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
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            // Pill Shape Floating Container (M3 Expressive)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), clip = false),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Security or Shield Icon
                    IconButton(
                        onClick = onToggleReaderMode,
                        modifier = Modifier.size(36.dp)
                    ) {
                        val isHttps = url.startsWith("https://")
                        Icon(
                            imageVector = if (isHttps) Icons.Default.Lock else Icons.Default.Public,
                            contentDescription = "Segurança",
                            tint = if (isHttps) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Reader Mode Button (Direct in Omnibox)
                    IconButton(
                        onClick = onToggleReaderMode,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Article,
                            contentDescription = "Modo Leitura",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // URL Input / Omnibox Text
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = {
                            textInput = it
                            isEditing = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .padding(horizontal = 2.dp),
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    // Reload or Stop Button
                    if (isLoading) {
                        IconButton(onClick = onStop, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Parar", modifier = Modifier.size(18.dp))
                        }
                    } else {
                        IconButton(onClick = onReload, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Refresh, contentDescription = "Recarregar", modifier = Modifier.size(18.dp))
                        }
                    }

                    // 1-Tap Quick Desktop Mode Toggle
                    IconButton(
                        onClick = onToggleDesktop,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isDesktopMode) Icons.Outlined.DesktopWindows else Icons.Outlined.PhoneAndroid,
                            contentDescription = "Alternar Modo Desktop/Mobile",
                            tint = if (isDesktopMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Tab Count Button Badge
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
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
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
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
                        .padding(horizontal = 24.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
