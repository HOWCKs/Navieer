package com.navieer.browser.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.ui.theme.CyberGamerTokens

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
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = "// ENGINE OVERCLOCKING & EXPERIMENTS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = CyberGamerTokens.NeonCyan
                )
                Text(
                    text = "SERVO FLAGS (navieer://flags)",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CyberGamerTokens.TextHoloWhite
                )
            }

            // Hazard Warning Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberGamerTokens.OverheatRed.copy(alpha = 0.15f)),
                border = BorderStroke(1.dp, CyberGamerTokens.OverheatRed.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = CyberGamerTokens.OverheatRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "ATENÇÃO: Módulos experimentais do runtime Servo (Rust / wgpu). Podem afetar o rendimento e estabilidade gráfica.",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CyberGamerTokens.TextHoloWhite
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Flag 1: Modo Experimental Geral do Servo
            TacticalFlagRow(
                title = "MODO EXPERIMENTAL SERVO",
                subtitle = "Habilita threads e pipelines de renderização de ponta do Servo Engine.",
                checked = isExperimentalServo,
                onCheckedChange = onToggleExperimentalServo
            )
            HorizontalDivider(color = CyberGamerTokens.TechSurfaceBorder)

            // Flag 2: WebGPU Acceleration
            TacticalFlagRow(
                title = "ACELERAÇÃO WEBGPU (wgpu)",
                subtitle = "Permite pipeline 3D e compute shaders acelerados por hardware via Rust wgpu.",
                checked = isWebGpu,
                onCheckedChange = onToggleWebGpu
            )
            HorizontalDivider(color = CyberGamerTokens.TechSurfaceBorder)

            // Flag 3: Forçar Modo Escuro (CSS Injection)
            TacticalFlagRow(
                title = "INJEÇÃO FORÇADA DE DARK THEME",
                subtitle = "Injeta paleta escura de alto contraste em sites com fundo claro.",
                checked = isForceDark,
                onCheckedChange = onToggleForceDark
            )
            HorizontalDivider(color = CyberGamerTokens.TechSurfaceBorder)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberGamerTokens.NeonCyan,
                    contentColor = CyberGamerTokens.VoidBlack
                )
            ) {
                Text(
                    "CONFIRMAR E RETORNAR",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun TacticalFlagRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = CyberGamerTokens.TextHoloWhite
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = CyberGamerTokens.TextTechCyan
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CyberGamerTokens.NeonCyan,
                checkedTrackColor = CyberGamerTokens.NeonCyan.copy(alpha = 0.35f),
                uncheckedThumbColor = CyberGamerTokens.TextMuted,
                uncheckedTrackColor = CyberGamerTokens.TechSurface
            )
        )
    }
}
