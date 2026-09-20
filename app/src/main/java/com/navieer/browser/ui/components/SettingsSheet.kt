package com.navieer.browser.ui.components

import androidx.compose.foundation.BorderStroke
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
import com.navieer.browser.data.SearchEngine
import com.navieer.browser.ui.theme.CyberGamerTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    currentSearchEngine: SearchEngine,
    customSearchUrl: String,
    isAmoledMode: Boolean,
    isAdBlockEnabled: Boolean,
    onSelectSearchEngine: (SearchEngine) -> Unit,
    onUpdateCustomSearchUrl: (String) -> Unit,
    onToggleAmoled: (Boolean) -> Unit,
    onToggleAdBlock: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var customUrlInput by remember { mutableStateOf(customSearchUrl) }
    var showSearchEngineDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState)
        ) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = "// SYSTEM PREFERENCES & ENGINE CONFIG",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = CyberGamerTokens.NeonCyan
                )
                Text(
                    text = "CONFIGURAÇÕES DO SISTEMA",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CyberGamerTokens.TextHoloWhite
                )
            }

            // Seção 1: Mecanismo de Busca
            CyberSectionHeader("// SEARCH ENGINE PROVIDER")

            Spacer(Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder), RoundedCornerShape(16.dp))
                    .clickable { showSearchEngineDialog = true },
                shape = RoundedCornerShape(16.dp),
                color = CyberGamerTokens.TechSurfaceHigh
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyberGamerTokens.NeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Search, null, tint = CyberGamerTokens.NeonCyan, modifier = Modifier.size(18.dp))
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            currentSearchEngine.title,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CyberGamerTokens.TextHoloWhite
                        )
                        Text(
                            if (currentSearchEngine == SearchEngine.CUSTOM) customUrlInput else currentSearchEngine.searchUrl,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGamerTokens.TextTechCyan
                        )
                    }

                    Icon(Icons.Default.KeyboardArrowRight, null, tint = CyberGamerTokens.TextMuted)
                }
            }

            if (currentSearchEngine == SearchEngine.CUSTOM) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = customUrlInput,
                    onValueChange = {
                        customUrlInput = it
                        onUpdateCustomSearchUrl(it)
                    },
                    label = { Text("URL de Busca (use %s para termo)", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                    placeholder = { Text("https://exemplo.com/busca?q=%s", fontFamily = FontFamily.Monospace) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = CyberGamerTokens.TextHoloWhite
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberGamerTokens.TechSurfaceHigh,
                        unfocusedContainerColor = CyberGamerTokens.TechSurfaceHigh,
                        focusedBorderColor = CyberGamerTokens.NeonCyan,
                        unfocusedBorderColor = CyberGamerTokens.TechSurfaceBorder,
                        cursorColor = CyberGamerTokens.NeonCyan
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            // Seção 2: Aparência e Display
            CyberSectionHeader("// DISPLAY & HOLOGRAPHIC MATRIX")

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                colors = CardDefaults.cardColors(containerColor = CyberGamerTokens.TechSurfaceHigh),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text(
                            "AMOLED PURO (0% BACKLIGHT)",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = CyberGamerTokens.TextHoloWhite
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Fundo preto absoluto (#000000) otimizado para economia de bateria e contraste.",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGamerTokens.TextTechCyan
                        )
                    }
                    Switch(
                        checked = isAmoledMode,
                        onCheckedChange = onToggleAmoled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberGamerTokens.NeonCyan,
                            checkedTrackColor = CyberGamerTokens.NeonCyan.copy(alpha = 0.35f),
                            uncheckedThumbColor = CyberGamerTokens.TextMuted,
                            uncheckedTrackColor = CyberGamerTokens.TechSurface
                        )
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Seção 3: Defesa e Privacidade
            CyberSectionHeader("// DEFENSE & TELEMETRY SHIELD")

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                colors = CardDefaults.cardColors(containerColor = CyberGamerTokens.TechSurfaceHigh),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text(
                            "BLOQUEIO ATIVO DE ANÚNCIOS & TRACKERS",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = CyberGamerTokens.TextHoloWhite
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Proteção nativa contra scripts espiões, mineradores e anúncios com alto consumo.",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGamerTokens.TextTechCyan
                        )
                    }
                    Switch(
                        checked = isAdBlockEnabled,
                        onCheckedChange = onToggleAdBlock,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberGamerTokens.MatrixGreen,
                            checkedTrackColor = CyberGamerTokens.MatrixGreen.copy(alpha = 0.35f),
                            uncheckedThumbColor = CyberGamerTokens.TextMuted,
                            uncheckedTrackColor = CyberGamerTokens.TechSurface
                        )
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Seção 4: Hardware & Runtime Specs
            CyberSectionHeader("// RUST SERVO CORE SPECIFICATIONS")

            Spacer(Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CyberGamerTokens.TechSurfaceHigh),
                border = BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "NAVIEER CYBER EDITION v1.0.0",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = CyberGamerTokens.NeonCyan
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "• Engine: Rust Servo Browser Project\n" +
                        "• JS Engine: Mozilla SpiderMonkey\n" +
                        "• Graphics Backend: EGL / OpenGL ES 3.0 / wgpu WebGPU\n" +
                        "• Architecture: 100% Free of Chromium (Blink/V8) & Gecko\n" +
                        "• Design: Cyber-Gamer HUD Matrix System",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CyberGamerTokens.TextTechCyan,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(36.dp))
        }

        // Dialog de seleção de Buscador
        if (showSearchEngineDialog) {
            AlertDialog(
                onDismissRequest = { showSearchEngineDialog = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = CyberGamerTokens.TechSurface,
                title = {
                    Text(
                        "// ESCOLHA O BUSCADOR PADRÃO",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CyberGamerTokens.NeonCyan
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SearchEngine.entries.forEach { engine ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        onSelectSearchEngine(engine)
                                        showSearchEngineDialog = false
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (currentSearchEngine == engine) CyberGamerTokens.TechSurfaceHigh else Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = currentSearchEngine == engine,
                                        onClick = {
                                            onSelectSearchEngine(engine)
                                            showSearchEngineDialog = false
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberGamerTokens.NeonCyan,
                                            unselectedColor = CyberGamerTokens.TextMuted
                                        )
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            engine.title,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = CyberGamerTokens.TextHoloWhite
                                        )
                                        if (engine.searchUrl.isNotEmpty()) {
                                            Text(
                                                engine.searchUrl,
                                                fontSize = 10.sp,
                                                fontFamily = FontFamily.Monospace,
                                                color = CyberGamerTokens.TextTechCyan
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showSearchEngineDialog = false },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "FECHAR",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CyberGamerTokens.NeonCyan
                        )
                    }
                }
            )
        }
    }
}
