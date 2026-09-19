package com.navieer.browser.model

import java.util.UUID

data class BrowserTab(
    val id: String = UUID.randomUUID().toString(),
    var title: String = "Nova Aba",
    var url: String = "navieer://home",
    val isIncognito: Boolean = false,
    val previewColor: Long = 0xFF6366F1,
    val createdAt: Long = System.currentTimeMillis()
)

data class HistoryItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class BookmarkItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val url: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class SpeedDialItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val url: String,
    val iconEmoji: String = "🌐"
)

data class DownloadItem(
    val id: Long = System.currentTimeMillis(),
    val fileName: String,
    val url: String,
    val fileSize: String = "Desconhecido",
    val progress: Float = 1.0f,
    val isCompleted: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
