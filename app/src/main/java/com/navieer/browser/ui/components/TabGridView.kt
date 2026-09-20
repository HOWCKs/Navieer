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
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.model.BrowserTab
import com.navieer.browser.ui.theme.CyberGamerTokens

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
        containerColor = CyberGamerTokens.TechSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // HUD Header with Segmented Switcher (Active Modules vs Stealth Ops)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
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
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = CyberGamerTokens.NeonCyan.copy(alpha = 0.2f),
                            activeContentColor = CyberGamerTokens.NeonCyan,
                            inactiveContainerColor = CyberGamerTokens.TechSurfaceHigh,
                            inactiveContentColor = CyberGamerTokens.TextMuted,
                            activeBorderColor = CyberGamerTokens.NeonCyan,
                            inactiveBorderColor = CyberGamerTokens.TechSurfaceBorder
                        ),
                        icon = { Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    ) {
                        Text(
                            "MODULES ($normalCount)",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    SegmentedButton(
                        selected = isPrivateSection,
                        onClick = { isPrivateSection = true },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = CyberGamerTokens.NeonViolet.copy(alpha = 0.2f),
                            activeContentColor = CyberGamerTokens.NeonViolet,
                            inactiveContainerColor = CyberGamerTokens.TechSurfaceHigh,
                            inactiveContentColor = CyberGamerTokens.TextMuted,
                            activeBorderColor = CyberGamerTokens.NeonViolet,
                            inactiveBorderColor = CyberGamerTokens.TechSurfaceBorder
                        ),
                        icon = { Icon(Icons.Outlined.Shield, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    ) {
                        Text(
                            "STEALTH ($incognitoCount)",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Flush/Purge all tabs in section
                IconButton(
                    onClick = { onCloseAll(isPrivateSection) },
                    enabled = filteredTabs.isNotEmpty(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CyberGamerTokens.TechSurfaceHigh)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Fechar todas as abas",
                        tint = if (filteredTabs.isNotEmpty()) CyberGamerTokens.OverheatRed else CyberGamerTokens.TextDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Grid of Cyber Tab Modules
            if (filteredTabs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isPrivateSection) Icons.Outlined.Shield else Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = CyberGamerTokens.NeonCyan.copy(alpha = 0.3f)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = if (isPrivateSection) "// NENHUM MÓDULO STEALTH ATIVO" else "// NENHUM MÓDULO PADRÃO ABERTO",
                            color = CyberGamerTokens.TextMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
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
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(168.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) {
                                        if (tab.isIncognito) CyberGamerTokens.NeonViolet else CyberGamerTokens.NeonCyan
                                    } else {
                                        CyberGamerTokens.TechSurfaceBorder
                                    },
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .clickable {
                                    onTabSelected(tab)
                                    onDismiss()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (tab.isIncognito) CyberGamerTokens.TechSurfaceHighest else CyberGamerTokens.TechSurfaceHigh
                            )
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                // Module Header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 4.dp, top = 8.dp, bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) CyberGamerTokens.MatrixGreen else CyberGamerTokens.TextDark
                                                )
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = tab.title,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = CyberGamerTokens.TextHoloWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    IconButton(
                                        onClick = { onTabClosed(tab) },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Encerrar módulo",
                                            tint = CyberGamerTokens.OverheatRed,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                // Holographic Module Preview Area
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    CyberGamerTokens.ObsidianDark,
                                                    CyberGamerTokens.TechSurface
                                                )
                                            )
                                        )
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                CyberGamerTokens.NeonCyan.copy(alpha = 0.15f)
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (tab.isIncognito) Icons.Outlined.Shield else Icons.Default.Info,
                                            contentDescription = null,
                                            tint = if (tab.isIncognito) CyberGamerTokens.NeonViolet else CyberGamerTokens.NeonCyan,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = tab.url.removePrefix("https://").removePrefix("http://"),
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = CyberGamerTokens.TextTechCyan,
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

            // Bottom Floating Bar: Deploy New Tab Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onNewTab(isPrivateSection)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPrivateSection) CyberGamerTokens.NeonViolet else CyberGamerTokens.NeonCyan,
                        contentColor = CyberGamerTokens.VoidBlack
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isPrivateSection) "+ DEPLOY STEALTH MODULE" else "+ DEPLOY NEW MODULE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
