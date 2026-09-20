package com.navieer.browser.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.ui.theme.CyberGamerTokens
import java.net.URI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteInfoSheet(
    url: String,
    title: String,
    onEditUrl: (String) -> Unit,
    onClearSiteData: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val host = remember(url) {
        try {
            val uri = URI(url)
            uri.host ?: url
        } catch (e: Exception) {
            url
        }
    }
    val isHttps = url.startsWith("https://")
    val isLocal = url.startsWith("navieer://") || url.startsWith("about:") || url.startsWith("file://")

    // Per-site permissions states
    var locationGranted by remember { mutableStateOf(false) }
    var cameraGranted by remember { mutableStateOf(false) }
    var micGranted by remember { mutableStateOf(false) }
    var notificationsGranted by remember { mutableStateOf(false) }
    var shieldEnabled by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = CyberGamerTokens.TechSurface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Defense Terminal Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (isLocal) CyberGamerTokens.NeonCyan.copy(alpha = 0.15f)
                            else if (isHttps) CyberGamerTokens.MatrixGreen.copy(alpha = 0.15f)
                            else CyberGamerTokens.OverheatRed.copy(alpha = 0.15f)
                        )
                        .border(
                            BorderStroke(
                                1.5.dp,
                                if (isLocal) CyberGamerTokens.NeonCyan
                                else if (isHttps) CyberGamerTokens.MatrixGreen
                                else CyberGamerTokens.OverheatRed
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocal) Icons.Default.Info
                        else if (isHttps) Icons.Default.Lock
                        else Icons.Default.Warning,
                        contentDescription = "Status de Criptografia",
                        tint = if (isLocal) CyberGamerTokens.NeonCyan
                        else if (isHttps) CyberGamerTokens.MatrixGreen
                        else CyberGamerTokens.OverheatRed,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isLocal) "NAVIEER // CORE SYSTEM" else host.uppercase(),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CyberGamerTokens.TextHoloWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (isLocal) "// INTERNAL SERVO RUNTIME"
                        else if (isHttps) "// TLS ENCRYPTED TUNNEL [SECURE]"
                        else "// UNENCRYPTED CLEAR-TEXT [WARNING]",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = if (isHttps) CyberGamerTokens.MatrixGreen else if (isLocal) CyberGamerTokens.NeonCyan else CyberGamerTokens.OverheatRed
                    )
                }
            }

            // Target URL HUD Panel with Tactical Actions
            Card(
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                colors = CardDefaults.cardColors(
                    containerColor = CyberGamerTokens.TechSurfaceHigh
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "// ACTIVE TARGET PAYLOAD",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = CyberGamerTokens.NeonCyan
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = url,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = CyberGamerTokens.TextHoloWhite,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("URL", url)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Payload copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CyberGamerTokens.NeonCyan
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("COPIAR", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, url)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Transmitir URL"))
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, CyberGamerTokens.NeonViolet.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CyberGamerTokens.NeonViolet
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("TRANSMITIR", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onEditUrl(url)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberGamerTokens.NeonCyan,
                                contentColor = CyberGamerTokens.VoidBlack
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("EDITAR", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Cookies & Storage Defense Section
            Card(
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                colors = CardDefaults.cardColors(
                    containerColor = CyberGamerTokens.TechSurfaceHigh
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = CyberGamerTokens.NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CACHE & TELEMETRIA DE SESSÃO",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = CyberGamerTokens.TextHoloWhite
                            )
                            Text(
                                text = "Registros de autenticação e cookies locais armazenados",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = CyberGamerTokens.TextTechCyan
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            onClearSiteData(host)
                            Toast.makeText(context, "Cookies e telemetria de $host foram expurgados!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CyberGamerTokens.OverheatRed.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CyberGamerTokens.OverheatRed
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "EXPURGAR COOKIES E DADOS DESTE HOST",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Tactical Permissions Terminal
            Card(
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CyberGamerTokens.TechSurfaceBorder),
                colors = CardDefaults.cardColors(
                    containerColor = CyberGamerTokens.TechSurfaceHigh
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "// PERMISSION MATRIX PROTOCOLS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = CyberGamerTokens.NeonCyan
                    )
                    Spacer(Modifier.height(12.dp))

                    // AdBlock Shield
                    TacticalPermissionRow(
                        icon = Icons.Outlined.Shield,
                        title = "ESCUDO NAVIEER (AD-BLOCK)",
                        subtitle = if (shieldEnabled) "DEFESA ATIVA: RASTREADORES BLOQUEADOS" else "ESCUDO DESATIVADO",
                        checked = shieldEnabled,
                        activeColor = CyberGamerTokens.NeonCyan,
                        onCheckedChange = { shieldEnabled = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = CyberGamerTokens.TechSurfaceBorder
                    )

                    // Location
                    TacticalPermissionRow(
                        icon = Icons.Default.LocationOn,
                        title = "GEOLOCALIZAÇÃO GPS",
                        subtitle = if (locationGranted) "AUTORIZADO // COORDENADAS ATIVAS" else "BLOQUEADO // MODO OFFLINE",
                        checked = locationGranted,
                        activeColor = CyberGamerTokens.MatrixGreen,
                        onCheckedChange = { locationGranted = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = CyberGamerTokens.TechSurfaceBorder
                    )

                    // Camera
                    TacticalPermissionRow(
                        icon = Icons.Default.CheckCircle,
                        title = "SENSOR ÓPTICO / CÂMERA",
                        subtitle = if (cameraGranted) "STREAM AUTORIZADO" else "ACESSO RESTRITO",
                        checked = cameraGranted,
                        activeColor = CyberGamerTokens.NeonViolet,
                        onCheckedChange = { cameraGranted = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = CyberGamerTokens.TechSurfaceBorder
                    )

                    // Microphone
                    TacticalPermissionRow(
                        icon = Icons.Default.Phone,
                        title = "CANAL DE ÁUDIO / MIC",
                        subtitle = if (micGranted) "STREAM DE VOZ ATIVO" else "MICROFONE SILENCIADO",
                        checked = micGranted,
                        activeColor = CyberGamerTokens.ElectricBlue,
                        onCheckedChange = { micGranted = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = CyberGamerTokens.TechSurfaceBorder
                    )

                    // Notifications
                    TacticalPermissionRow(
                        icon = Icons.Default.Notifications,
                        title = "NOTIFICAÇÕES PUSH",
                        subtitle = if (notificationsGranted) "RADAR DE ALERTAS ATIVO" else "ALERTAS SILENCIADOS",
                        checked = notificationsGranted,
                        activeColor = CyberGamerTokens.CyberAmber,
                        onCheckedChange = { notificationsGranted = it }
                    )
                }
            }

            Spacer(Modifier.height(36.dp))
        }
    }
}

@Composable
private fun TacticalPermissionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (checked) activeColor.copy(alpha = 0.15f) else CyberGamerTokens.TechSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (checked) activeColor else CyberGamerTokens.TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = CyberGamerTokens.TextHoloWhite
            )
            Text(
                text = subtitle,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = if (checked) activeColor else CyberGamerTokens.TextTechCyan
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = activeColor,
                checkedTrackColor = activeColor.copy(alpha = 0.3f),
                uncheckedThumbColor = CyberGamerTokens.TextMuted,
                uncheckedTrackColor = CyberGamerTokens.TechSurface
            )
        )
    }
}
