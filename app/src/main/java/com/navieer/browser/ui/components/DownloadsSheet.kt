package com.navieer.browser.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.model.DownloadItem
import com.navieer.browser.ui.theme.CyberGamerTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsSheet(
    downloads: List<DownloadItem>,
    onCancelDownload: (DownloadItem) -> Unit,
    onClearCompleted: () -> Unit,
    onDismiss: () -> Unit
) {
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "// DATA PACKET TRANSFERS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = CyberGamerTokens.MatrixGreen
                    )
                    Text(
                        text = "DOWNLOADS / PAYLOADS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CyberGamerTokens.TextHoloWhite
                    )
                }

                if (downloads.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onClearCompleted,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CyberGamerTokens.NeonCyan
                        )
                    ) {
                        Text(
                            "LIMPAR CONCLUÍDOS",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (downloads.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = CyberGamerTokens.TextDark
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "// NENHUM PACOTE DE DADOS EM TRANSFERÊNCIA",
                            color = CyberGamerTokens.TextMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(downloads, key = { it.id }) { item ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CyberGamerTokens.TechSurfaceHigh,
                            border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (item.isCompleted) CyberGamerTokens.MatrixGreen.copy(alpha = 0.15f)
                                            else CyberGamerTokens.NeonCyan.copy(alpha = 0.15f)
                                        )
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                if (item.isCompleted) CyberGamerTokens.MatrixGreen.copy(alpha = 0.4f)
                                                else CyberGamerTokens.NeonCyan.copy(alpha = 0.4f)
                                            ),
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.Download,
                                        contentDescription = null,
                                        tint = if (item.isCompleted) CyberGamerTokens.MatrixGreen else CyberGamerTokens.NeonCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.fileName,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = CyberGamerTokens.TextHoloWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "${item.fileSize} // ${if (item.isCompleted) "DOWNLOAD COMPLETO" else "TRANSFERINDO: ${(item.progress * 100).toInt()}%"}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = if (item.isCompleted) CyberGamerTokens.MatrixGreen else CyberGamerTokens.NeonCyan
                                    )

                                    if (!item.isCompleted) {
                                        LinearProgressIndicator(
                                            progress = { item.progress },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 6.dp)
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp)),
                                            color = CyberGamerTokens.NeonCyan,
                                            trackColor = CyberGamerTokens.TechSurface
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { onCancelDownload(item) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Cancelar",
                                        tint = CyberGamerTokens.OverheatRed,
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
