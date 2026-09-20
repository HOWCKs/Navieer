package com.navieer.browser.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.model.SpeedDialItem
import com.navieer.browser.ui.theme.CyberGamerTokens

@Composable
fun SpeedDialView(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }

    val defaultItems = remember {
        mutableStateListOf(
            SpeedDialItem(title = "Servo", url = "https://servo.org", iconName = "servo"),
            SpeedDialItem(title = "Rust", url = "https://www.rust-lang.org", iconName = "rust"),
            SpeedDialItem(title = "GitHub", url = "https://github.com", iconName = "github"),
            SpeedDialItem(title = "Wikipedia", url = "https://www.wikipedia.org", iconName = "wikipedia"),
            SpeedDialItem(title = "DuckDuckGo", url = "https://duckduckgo.com", iconName = "duckduckgo"),
            SpeedDialItem(title = "Hacker News", url = "https://news.ycombinator.com", iconName = "news")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(0.7f))

        // Cyber Core Hero Icon with Dual Neon Ring
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            CyberGamerTokens.TechSurfaceHighest,
                            CyberGamerTokens.ObsidianDark
                        )
                    )
                )
                .border(
                    BorderStroke(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                CyberGamerTokens.NeonCyan,
                                CyberGamerTokens.NeonViolet,
                                CyberGamerTokens.NeonCyan
                            )
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = "Navieer Cyber Core",
                tint = CyberGamerTokens.NeonCyan,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        // Brand Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "NAVIEER",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                ),
                color = CyberGamerTokens.TextHoloWhite
            )
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = CyberGamerTokens.NeonCyan.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "PRO",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CyberGamerTokens.NeonCyan,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // Subtitle / Architecture Specs
        Text(
            text = "SERVO RUST RUNTIME • 0% CHROMIUM • HIGH FPS",
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            color = CyberGamerTokens.TextTechCyan,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(18.dp))

        // Live Telemetry HUD Bar
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HudTelemetryChip(text = "SERVO: ONLINE", leadingDotColor = CyberGamerTokens.MatrixGreen)
            HudTelemetryChip(text = "GPU: EGL 3.0", leadingDotColor = CyberGamerTokens.NeonCyan)
            HudTelemetryChip(text = "SHIELD: 100%", leadingDotColor = CyberGamerTokens.NeonViolet)
        }

        Spacer(Modifier.height(32.dp))

        // Tactical Pods Grid Section Header
        CyberSectionHeader(
            title = "Tactical Launch Pods",
            tag = "[ QUICK ACCESS ]",
            modifier = Modifier.padding(bottom = 14.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(defaultItems) { item ->
                val icon = getSpeedDialVector(item.iconName)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onNavigate(item.url) }
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        CyberGamerTokens.TechSurfaceHigh,
                                        CyberGamerTokens.TechSurface
                                    )
                                )
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    CyberGamerTokens.NeonCyan.copy(alpha = 0.35f)
                                ),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = item.title,
                            tint = CyberGamerTokens.NeonCyan,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        color = CyberGamerTokens.TextHoloWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { showAddDialog = true }
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(CyberGamerTokens.TechSurface.copy(alpha = 0.5f))
                            .border(
                                BorderStroke(
                                    1.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            CyberGamerTokens.NeonViolet.copy(alpha = 0.5f),
                                            CyberGamerTokens.NeonCyan.copy(alpha = 0.5f)
                                        )
                                    )
                                ),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar Atalho",
                            tint = CyberGamerTokens.NeonViolet,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "NOVO POD",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        color = CyberGamerTokens.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.weight(1.1f))

        // Add Shortcut Dialog with Cyberpunk Terminal styling
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                shape = RoundedCornerShape(22.dp),
                containerColor = CyberGamerTokens.TechSurface,
                modifier = Modifier.border(
                    BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(22.dp)
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = CyberGamerTokens.NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "CONFIGURAR NOVO POD",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = CyberGamerTokens.TextHoloWhite
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Identificador do Site", fontFamily = FontFamily.Monospace) },
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberGamerTokens.NeonCyan,
                                unfocusedBorderColor = CyberGamerTokens.NeonCyan.copy(alpha = 0.3f),
                                focusedLabelColor = CyberGamerTokens.NeonCyan,
                                unfocusedLabelColor = CyberGamerTokens.TextMuted
                            )
                        )
                        OutlinedTextField(
                            value = newUrl,
                            onValueChange = { newUrl = it },
                            label = { Text("URL / Host Alvo", fontFamily = FontFamily.Monospace) },
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberGamerTokens.NeonCyan,
                                unfocusedBorderColor = CyberGamerTokens.NeonCyan.copy(alpha = 0.3f),
                                focusedLabelColor = CyberGamerTokens.NeonCyan,
                                unfocusedLabelColor = CyberGamerTokens.TextMuted
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank() && newUrl.isNotBlank()) {
                                val finalUrl = if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) newUrl else "https://$newUrl"
                                defaultItems.add(SpeedDialItem(title = newTitle, url = finalUrl, iconName = "home"))
                                newTitle = ""
                                newUrl = ""
                                showAddDialog = false
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberGamerTokens.NeonCyan,
                            contentColor = CyberGamerTokens.VoidBlack
                        )
                    ) {
                        Text("ATIVAR POD", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showAddDialog = false },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("CANCELAR", color = CyberGamerTokens.TextMuted, fontFamily = FontFamily.Monospace)
                    }
                }
            )
        }
    }
}

private fun getSpeedDialVector(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "servo", "memory" -> Icons.Default.Settings
        "rust", "code" -> Icons.Default.Star
        "github", "hub" -> Icons.Default.Favorite
        "wikipedia", "book" -> Icons.Default.Info
        "duckduckgo", "search" -> Icons.Default.Search
        "news", "article" -> Icons.Default.List
        else -> Icons.Default.Home
    }
}
