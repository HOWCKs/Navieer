package com.navieer.browser.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.data.SearchSuggestion
import com.navieer.browser.ui.theme.CyberGamerTokens

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
            // Cyber HUD Floating Capsule
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), clip = false),
                shape = RoundedCornerShape(20.dp),
                color = CyberGamerTokens.TechSurface.copy(alpha = 0.95f),
                border = BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(
                            CyberGamerTokens.NeonCyan.copy(alpha = 0.45f),
                            CyberGamerTokens.NeonViolet.copy(alpha = 0.45f)
                        )
                    )
                ),
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Security Shield / Encryption Status Button
                    IconButton(
                        onClick = onOpenSiteInfo,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    ) {
                        val isHttps = url.startsWith("https://")
                        val isLocal = url.startsWith("navieer://") || url.isEmpty()

                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isLocal) Icons.Default.Info
                                else if (isHttps) Icons.Default.Lock
                                else Icons.Default.Warning,
                                contentDescription = "Informações e Segurança do Site",
                                tint = if (isLocal) CyberGamerTokens.NeonCyan
                                else if (isHttps) CyberGamerTokens.MatrixGreen
                                else CyberGamerTokens.OverheatRed,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    // Reader Mode Tactical Optic Button
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
                                tint = CyberGamerTokens.TextTechCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Tactical URL / Query Input
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
                                "TARGET URL // SEARCH...",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CyberGamerTokens.TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGamerTokens.TextHoloWhite
                        ),
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
                                        tint = CyberGamerTokens.TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = CyberGamerTokens.NeonCyan
                        )
                    )

                    // Reload or Abort Button
                    if (isLoading) {
                        IconButton(
                            onClick = onStop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Parar",
                                tint = CyberGamerTokens.OverheatRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else if (url.isNotEmpty() && !url.startsWith("navieer://")) {
                        IconButton(
                            onClick = onReload,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Recarregar",
                                tint = CyberGamerTokens.NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Tactical Viewport Toggle (Desktop / Mobile Mode)
                    IconButton(
                        onClick = onToggleDesktop,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isDesktopMode) Icons.Outlined.DesktopWindows else Icons.Outlined.PhoneAndroid,
                            contentDescription = "Alternar Modo Desktop/Mobile",
                            tint = if (isDesktopMode) CyberGamerTokens.NeonCyan else CyberGamerTokens.TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Tactical Module/Tab Counter Badge
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onOpenTabs() },
                        shape = RoundedCornerShape(8.dp),
                        color = CyberGamerTokens.TechSurfaceHigh,
                        border = BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = if (tabCount < 10) "0$tabCount" else "$tabCount",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CyberGamerTokens.NeonCyan,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                        )
                    }

                    // Command Center Dropdown Menu
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu de Ações",
                                tint = CyberGamerTokens.TextHoloWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .background(CyberGamerTokens.TechSurface)
                                .border(
                                    BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.35f)),
                                    shape = RoundedCornerShape(18.dp)
                                )
                        ) {
                            DropdownMenuItem(
                                text = { Text("Favoritos", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.TextHoloWhite) },
                                leadingIcon = { Icon(Icons.Default.Bookmark, null, tint = CyberGamerTokens.NeonCyan) },
                                onClick = {
                                    showMenu = false
                                    onOpenBookmarks()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Salvar Favorito", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.TextHoloWhite) },
                                leadingIcon = { Icon(Icons.Default.Star, null, tint = CyberGamerTokens.CyberAmber) },
                                onClick = {
                                    showMenu = false
                                    onAddBookmark()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Histórico de Acesso", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.TextHoloWhite) },
                                leadingIcon = { Icon(Icons.Default.History, null, tint = CyberGamerTokens.ElectricBlue) },
                                onClick = {
                                    showMenu = false
                                    onOpenHistory()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Payloads / Downloads", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.TextHoloWhite) },
                                leadingIcon = { Icon(Icons.Default.Download, null, tint = CyberGamerTokens.MatrixGreen) },
                                onClick = {
                                    showMenu = false
                                    onOpenDownloads()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Telemetria e Segurança", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.TextHoloWhite) },
                                leadingIcon = { Icon(Icons.Outlined.Shield, null, tint = CyberGamerTokens.NeonViolet) },
                                onClick = {
                                    showMenu = false
                                    onOpenSiteInfo()
                                }
                            )
                            HorizontalDivider(color = CyberGamerTokens.TechSurfaceBorder)
                            DropdownMenuItem(
                                text = { Text("Servo Overclock // Flags", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.NeonCyan) },
                                leadingIcon = { Icon(Icons.Default.Flag, null, tint = CyberGamerTokens.NeonCyan) },
                                onClick = {
                                    showMenu = false
                                    onOpenFlags()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Configurações do Sistema", fontFamily = FontFamily.Monospace, color = CyberGamerTokens.TextHoloWhite) },
                                leadingIcon = { Icon(Icons.Default.Settings, null, tint = CyberGamerTokens.TextMuted) },
                                onClick = {
                                    showMenu = false
                                    onOpenSettings()
                                }
                            )
                        }
                    }
                }
            }

            // Cyberpunk Neon Loading Indicator
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 2.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = CyberGamerTokens.NeonCyan,
                    trackColor = CyberGamerTokens.NeonViolet.copy(alpha = 0.2f)
                )
            }

            // Smart Autocomplete Suggestions HUD Card
            if (isEditing && suggestions.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp), clip = false)
                        .border(
                            BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.35f)),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CyberGamerTokens.TechSurfaceHigh.copy(alpha = 0.98f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "// RADAR QUERY PREDICTIONS",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CyberGamerTokens.NeonCyan
                            )
                            Text(
                                text = "ONLINE",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CyberGamerTokens.MatrixGreen
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = CyberGamerTokens.TechSurfaceBorder
                        )

                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(suggestions) { suggestion ->
                                val (tag, tagColor) = when (suggestion.typeIcon) {
                                    "search" -> Pair("SEARCH", CyberGamerTokens.NeonCyan)
                                    "history" -> Pair("HISTORY", CyberGamerTokens.ElectricBlue)
                                    "bookmark" -> Pair("SAVED", CyberGamerTokens.CyberAmber)
                                    else -> Pair("WEB POD", CyberGamerTokens.NeonViolet)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            isEditing = false
                                            textInput = suggestion.targetUrl
                                            focusManager.clearFocus()
                                            onNavigate(suggestion.targetUrl)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = tagColor.copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, tagColor.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = tag,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = tagColor,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Spacer(Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = suggestion.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontFamily = FontFamily.Monospace
                                            ),
                                            color = CyberGamerTokens.TextHoloWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = suggestion.subtitle,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontFamily = FontFamily.Monospace
                                            ),
                                            color = CyberGamerTokens.TextTechCyan,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Carregar",
                                        tint = CyberGamerTokens.NeonCyan,
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
}
