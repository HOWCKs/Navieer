package com.navieer.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlagsSheet(
    isExperimentalServo: Boolean,
    isWebGpu: Boolean,
    isForceDark: Boolean,
    onToggleExperimentalServo: (Boolean) -> Unit,
    onToggleWebGpu: (Boolean) -> Unit,
    onToggleForceDark: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                width = 48.dp,
                height = 5.dp,
                shape = RoundedCornerShape(10.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Servo Flags (navieer://flags)",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(12.dp))

            // Warning Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Recursos experimentais do motor Servo. A ativação pode alterar a estabilidade e o comportamento das páginas.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Flag 1: Modo Experimental Geral do Servo
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text("Modo Experimental Servo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Habilita recursos de ponta em desenvolvimento pelo time do Servo.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = isExperimentalServo, onCheckedChange = onToggleExperimentalServo)
            }
            HorizontalDivider()

            // Flag 2: WebGPU Acceleration
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text("Aceleração WebGPU (wgpu)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Permite computação e gráficos avançados 3D via backend wgpu do Rust.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = isWebGpu, onCheckedChange = onToggleWebGpu)
            }
            HorizontalDivider()

            // Flag 3: Forçar Modo Escuro (CSS Injection)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text("Forçar Modo Escuro em Páginas", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Aplica inversão inteligente e dark stylesheet em todos os sites.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = isForceDark, onCheckedChange = onToggleForceDark)
            }
            HorizontalDivider()

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Concluído")
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}
