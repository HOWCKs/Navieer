package com.navieer.browser.model

import java.util.UUID

data class BrowserTab(
    val id: String = UUID.randomUUID().toString(),
    var title: String = "Nova Aba",
    var url: String = "https://servo.org",
    val isIncognito: Boolean = false,
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
