package com.navieer.browser.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.model.SpeedDialItem

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
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(0.8f))

        // Minimalist Navieer Compass Logo
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = "Navieer Logo",
                tint = Color.White,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Navieer",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(6.dp))

        // Servo Engine Badge Chip with rounded pill
        SuggestionChip(
            onClick = { onNavigate("https://servo.org") },
            label = {
                Text(
                    text = "Servo Web Engine • Rust Parallelism",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ),
            border = null
        )

        Spacer(Modifier.height(36.dp))

        // Speed Dial Shortcuts Grid
        Text(
            text = "Atalhos Rápidos",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(defaultItems) { item ->
                val icon = getSpeedDialVector(item.iconName)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onNavigate(item.url) }
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
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
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showAddDialog = true }
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar Atalho",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Adicionar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.weight(1.2f))

        // Add Shortcut Dialog with smooth rounded corners
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                title = {
                    Text(
                        "Novo Atalho",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Nome do site") },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newUrl,
                            onValueChange = { newUrl = it },
                            label = { Text("URL (ex: github.com)") },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank() && newUrl.isNotBlank()) {
                                val finalUrl = if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) newUrl else "https://$newUrl"
                                defaultItems.add(SpeedDialItem(title = newTitle, url = finalUrl, iconName = "language"))
                                newTitle = ""
                                newUrl = ""
                                showAddDialog = false
                            }
                        },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showAddDialog = false },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

private fun getSpeedDialVector(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "servo", "memory" -> Icons.Default.Memory
        "rust", "code" -> Icons.Default.Code
        "github", "hub" -> Icons.Default.Hub
        "wikipedia", "book" -> Icons.Default.MenuBook
        "duckduckgo", "search" -> Icons.Default.TravelExplore
        "news", "article" -> Icons.Default.Article
        else -> Icons.Default.Language
    }
}
