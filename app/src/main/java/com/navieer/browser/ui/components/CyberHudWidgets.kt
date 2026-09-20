package com.navieer.browser.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navieer.browser.ui.theme.CyberGamerTokens

@Composable
fun HudStatusDot(
    color: Color = CyberGamerTokens.MatrixGreen,
    size: Int = 8
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun HudTelemetryChip(
    text: String,
    leadingDotColor: Color? = CyberGamerTokens.NeonCyan,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = CyberGamerTokens.TechSurface.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, CyberGamerTokens.NeonCyan.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingDotColor != null) {
                HudStatusDot(color = leadingDotColor, size = 6)
            }
            Text(
                text = text,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = CyberGamerTokens.TextTechCyan,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun CyberSectionHeader(
    title: String,
    tag: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(14.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(CyberGamerTokens.NeonCyan, CyberGamerTokens.NeonViolet)
                        )
                    )
            )
            Text(
                text = title.uppercase(),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = CyberGamerTokens.TextHoloWhite,
                letterSpacing = 1.sp
            )
        }

        if (tag != null) {
            Text(
                text = tag,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = CyberGamerTokens.NeonCyan,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
