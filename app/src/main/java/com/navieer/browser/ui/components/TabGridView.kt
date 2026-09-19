package com.navieer.browser.ui.components

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
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.model.BrowserTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabGridView(
    tabs: List<BrowserTab>,
    activeTabId: String,
    onTabSelected: (BrowserTab) -> Unit,
    onTabClosed: (BrowserTab) -> Unit,
    onNewTab: (Boolean) -> Unit,
    onCloseAll: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var isPrivateSection by remember { mutableStateOf(false) }

    val filteredTabs = tabs.filter { it.isIncognito == isPrivateSection }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header with Segmented Switcher (Abas Normais vs Privadas)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    val normalCount = tabs.count { !it.isIncognito }
                    val incognitoCount = tabs.count { it.isIncognito }

                    SegmentedButton(
                        selected = !isPrivateSection,
                        onClick = { isPrivateSection = false },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        icon = { Icon(Icons.Default.Tab, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    ) {
                        Text("Normais ($normalCount)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    SegmentedButton(
                        selected = isPrivateSection,
                        onClick = { isPrivateSection = true },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        icon = { Icon(Icons.Outlined.Shield, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    ) {
                        Text("Privadas ($incognitoCount)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }

                IconButton(
                    onClick = { onCloseAll(isPrivateSection) },
                    enabled = filteredTabs.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Fechar todas as abas",
                        tint = if (filteredTabs.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }

            // Grid of Tab Cards
            if (filteredTabs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isPrivateSection) Icons.Outlined.Shield else Icons.Default.Tab,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (isPrivateSection) "Nenhuma aba anônima aberta" else "Nenhuma aba normal aberta",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredTabs, key = { it.id }) { tab ->
                        val isSelected = tab.id == activeTabId

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    onTabSelected(tab)
                                    onDismiss()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (tab.isIncognito) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                // Tab Header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = tab.title,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { onTabClosed(tab) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Fechar aba",
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                // Tab Preview Simulation Area
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f),
                                                    Color(tab.previewColor).copy(alpha = 0.2f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text(
                                            text = if (tab.isIncognito) "🕶️" else "🌐",
                                            fontSize = 24.sp
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = tab.url.removePrefix("https://").removePrefix("http://"),
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Floating Bar: Add New Tab Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        onNewTab(isPrivateSection)
                        onDismiss()
                    },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text(if (isPrivateSection) "Nova Aba Anônima" else "Nova Aba") },
                    containerColor = if (isPrivateSection) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(28.dp)
                )
            }
        }
    }
}
