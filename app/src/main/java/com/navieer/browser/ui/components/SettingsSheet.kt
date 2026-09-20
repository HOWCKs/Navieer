package com.navieer.browser.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.data.SearchEngine

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
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Configurações",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Seção 1: Mecanismo de Busca
            Text(
                text = "Mecanismo de Busca",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { showSearchEngineDialog = true },
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                ListItem(
                    headlineContent = { Text(currentSearchEngine.title, fontWeight = FontWeight.Medium) },
                    supportingContent = {
                        Text(
                            if (currentSearchEngine == SearchEngine.CUSTOM) customUrlInput else currentSearchEngine.searchUrl,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingContent = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, null) },
                    colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                )
            }

            if (currentSearchEngine == SearchEngine.CUSTOM) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = customUrlInput,
                    onValueChange = {
                        customUrlInput = it
                        onUpdateCustomSearchUrl(it)
                    },
                    label = { Text("URL de Busca (use %s para termo)") },
                    placeholder = { Text("https://exemplo.com/busca?q=%s") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // Seção 2: Aparência e Design System
            Text(
                text = "Aparência (Material 3 Expressive)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text("Dark Mode AMOLED Puro (#000000)", fontWeight = FontWeight.Medium)
                        Text("Preto absoluto para máxima economia de energia em telas OLED/AMOLED.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = isAmoledMode, onCheckedChange = onToggleAmoled)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Seção 3: Privacidade e Segurança
            Text(
                text = "Privacidade e Segurança",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text("Bloqueador de Anúncios e Rastreadores", fontWeight = FontWeight.Medium)
                        Text("Proteção nativa contra scripts de rastreamento, banners invasivos e telemetria.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = isAdBlockEnabled, onCheckedChange = onToggleAdBlock)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Seção 4: Sobre o Navieer e Servo Engine
            Text(
                text = "Sobre",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Navieer Browser v1.0.0", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Motor de renderização: Servo (Rust + Mozilla SpiderMonkey)\n" +
                        "100% livre de Chromium (Blink/V8) e Gecko.\n" +
                        "Renderização via EGL / OpenGL ES 3.0 acelerada por hardware.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }

        // Dialog de seleção de Buscador
        if (showSearchEngineDialog) {
            AlertDialog(
                onDismissRequest = { showSearchEngineDialog = false },
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                title = { Text("Escolha o Buscador Padrão", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SearchEngine.entries.forEach { engine ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        onSelectSearchEngine(engine)
                                        showSearchEngineDialog = false
                                    },
                                shape = RoundedCornerShape(16.dp),
                                color = androidx.compose.ui.graphics.Color.Transparent
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
                                        }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(engine.title, fontWeight = FontWeight.Medium)
                                        if (engine.searchUrl.isNotEmpty()) {
                                            Text(engine.searchUrl, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}
