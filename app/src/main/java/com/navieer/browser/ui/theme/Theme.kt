package com.navieer.browser.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

/**
 * NAVIEER CYBER-GAMER DESIGN SYSTEM
 * High-performance, immersive, dark obsidian, neon cyber accents (Cyan, Violet, Matrix Green)
 */

object CyberGamerTokens {
    // Primary Core Palette
    val NeonCyan = Color(0xFF00F2FE)       // Precision, Speed
    val ElectricBlue = Color(0xFF38BDF8)   // High-tech flow
    val NeonViolet = Color(0xFFA855F7)     // Esports prestige, performance
    val NeonPurple = Color(0xFF7C3AED)     // Cyber depth
    val MatrixGreen = Color(0xFF10B981)    // Shield active, 60 FPS online
    val OverheatRed = Color(0xFFEF4444)    // Threat blocked, close, terminate
    val CyberAmber = Color(0xFFF59E0B)     // Warning, overclock alert

    // Dark Surfaces (Deep Obsidian & Tech Slate)
    val VoidBlack = Color(0xFF030712)      // Pure deep space
    val ObsidianDark = Color(0xFF070B14)   // Background primary
    val TechSurface = Color(0xFF0C1220)    // Secondary panel surface
    val TechSurfaceHigh = Color(0xFF131C31)// Elevated cards
    val TechSurfaceHighest = Color(0xFF1C2844) // Highlighted modules
    val TechSurfaceBorder = Color(0xFF1E293B) // Base border

    // Text & Information Hierarchy
    val TextHoloWhite = Color(0xFFF8FAFC)
    val TextTechCyan = Color(0xFFBAE6FD)
    val TextMuted = Color(0xFF94A3B8)
    val TextDark = Color(0xFF64748B)

    // Gradients
    val CyberAccentGradient = Brush.horizontalGradient(
        listOf(NeonCyan, NeonViolet)
    )

    val CyberShieldGradient = Brush.horizontalGradient(
        listOf(NeonViolet, NeonCyan)
    )

    val CyberCardGradient = Brush.verticalGradient(
        listOf(TechSurfaceHigh, TechSurface)
    )

    val CyberBorderStroke = BorderStroke(
        width = 1.dp,
        brush = Brush.horizontalGradient(
            listOf(NeonCyan.copy(alpha = 0.35f), NeonViolet.copy(alpha = 0.35f))
        )
    )

    val CyberBorderSubtle = BorderStroke(
        width = 1.dp,
        color = NeonCyan.copy(alpha = 0.18f)
    )
}

// Master Gamer Dark Color Scheme (Cyberpunk / Esports / HUD)
val GamerDarkColorScheme = darkColorScheme(
    primary = CyberGamerTokens.NeonCyan,
    onPrimary = CyberGamerTokens.VoidBlack,
    primaryContainer = Color(0xFF0E3A52),
    onPrimaryContainer = Color(0xFFCFFAFE),

    secondary = CyberGamerTokens.NeonViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4C1D95),
    onSecondaryContainer = Color(0xFFF3E8FF),

    tertiary = CyberGamerTokens.MatrixGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF064E3B),
    onTertiaryContainer = Color(0xFFD1FAE5),

    background = CyberGamerTokens.ObsidianDark,
    onBackground = CyberGamerTokens.TextHoloWhite,

    surface = CyberGamerTokens.ObsidianDark,
    onSurface = CyberGamerTokens.TextHoloWhite,
    surfaceVariant = CyberGamerTokens.TechSurface,
    onSurfaceVariant = CyberGamerTokens.TextMuted,

    surfaceContainerLowest = CyberGamerTokens.VoidBlack,
    surfaceContainerLow = CyberGamerTokens.ObsidianDark,
    surfaceContainer = CyberGamerTokens.TechSurface,
    surfaceContainerHigh = CyberGamerTokens.TechSurfaceHigh,
    surfaceContainerHighest = CyberGamerTokens.TechSurfaceHighest,

    outline = CyberGamerTokens.NeonCyan.copy(alpha = 0.25f),
    outlineVariant = CyberGamerTokens.TechSurfaceBorder,

    error = CyberGamerTokens.OverheatRed,
    onError = Color.White,
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2)
)

// AMOLED Zero-Power Gamer Scheme
val GamerAmoledColorScheme = GamerDarkColorScheme.copy(
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = Color(0xFF05080E),
    surfaceContainer = Color(0xFF0B101D),
    surfaceContainerHigh = Color(0xFF111827),
    surfaceContainerHighest = Color(0xFF182238),
    outline = CyberGamerTokens.NeonCyan.copy(alpha = 0.3f),
    outlineVariant = Color(0xFF1E293B)
)

// High-Tech Cyber Gamer Shapes
val GamerShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun NavieerTheme(
    darkTheme: Boolean = true, // Gamer UI defaults to dark
    isAmoledMode: Boolean = true,
    dynamicColor: Boolean = false, // Keep high-tech gamer identity consistent
    dynamicSiteColor: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isAmoledMode) GamerAmoledColorScheme else GamerDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val statusBarColor = dynamicSiteColor ?: colorScheme.background
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = GamerShapes,
        content = content
    )
}
