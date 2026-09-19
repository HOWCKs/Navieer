package com.navieer.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ReaderTheme(val bg: Color, val text: Color, val nameLabel: String) {
    LIGHT(Color(0xFFFAFAFA), Color(0xFF1E293B), "Claro"),
    SEPIA(Color(0xFFFBF0D9), Color(0xFF5F4B32), "Sépia"),
    DARK(Color(0xFF1E1E2E), Color(0xFFCDD6F4), "Escuro"),
    AMOLED(Color(0xFF000000), Color(0xFFE2E8F0), "AMOLED")
}

@Composable
fun ReaderView(
    title: String,
    url: String,
    content: String,
    onClose: () -> Unit
) {
    var currentTheme by remember { mutableStateOf(ReaderTheme.SEPIA) }
    var currentFontFamily by remember { mutableStateOf(FontFamily.Serif) }
    var fontSizeSp by remember { mutableStateOf(18) }
    var showCustomizer by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Reader Mode Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Sair do Modo Leitura",
                        tint = currentTheme.text
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showCustomizer = !showCustomizer }) {
                        Icon(
                            Icons.Default.FormatSize,
                            contentDescription = "Personalizar Leitura",
                            tint = currentTheme.text
                        )
                    }
                }
            }

            // Customizer Floating Card
            if (showCustomizer) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Theme Palette Chooser
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ReaderTheme.entries.forEach { theme ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(theme.bg)
                                        .border(
                                            width = if (currentTheme == theme) 2.dp else 1.dp,
                                            color = if (currentTheme == theme) MaterialTheme.colorScheme.primary else Color.Gray,
                                            shape = CircleShape
                                        )
                                        .clickable { currentTheme = theme },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "A",
                                        color = theme.text,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Font size and Font family controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Font Size Buttons
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedIconButton(
                                    onClick = { if (fontSizeSp > 14) fontSizeSp -= 2 },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Text("A-", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(8.dp))
                                Text("$fontSizeSp sp", fontSize = 13.sp)
                                Spacer(Modifier.width(8.dp))
                                OutlinedIconButton(
                                    onClick = { if (fontSizeSp < 28) fontSizeSp += 2 },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Text("A+", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Font Family Switcher
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = currentFontFamily == FontFamily.Serif,
                                    onClick = { currentFontFamily = FontFamily.Serif },
                                    label = { Text("Serif") }
                                )
                                FilterChip(
                                    selected = currentFontFamily == FontFamily.SansSerif,
                                    onClick = { currentFontFamily = FontFamily.SansSerif },
                                    label = { Text("Sans") }
                                )
                            }
                        }
                    }
                }
            }

            // Article Content Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = title.ifEmpty { "Artigo" },
                    fontSize = (fontSizeSp + 6).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFontFamily,
                    color = currentTheme.text,
                    lineHeight = (fontSizeSp + 10).sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = url,
                    fontSize = 11.sp,
                    color = currentTheme.text.copy(alpha = 0.6f),
                    fontFamily = FontFamily.Monospace
                )

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = currentTheme.text.copy(alpha = 0.2f))
                Spacer(Modifier.height(20.dp))

                Text(
                    text = content.ifEmpty {
                        "Modo Leitura Navieer ativado.\n\nEste modo extrai o conteúdo essencial do artigo web, removendo anúncios, scripts invasivos e elementos que distraem da leitura.\n\nVocê pode alterar a tipografia para Serif ou Sans-serif, aumentar o tamanho do texto e alternar entre os temas Sépia, Claro, Escuro e AMOLED nos controles acima."
                    },
                    fontSize = fontSizeSp.sp,
                    fontFamily = currentFontFamily,
                    color = currentTheme.text,
                    lineHeight = (fontSizeSp * 1.6).sp
                )

                Spacer(Modifier.height(60.dp))
            }
        }
    }
}
