package com.navieer.browser.data

import com.navieer.browser.model.BookmarkItem
import com.navieer.browser.model.HistoryItem

data class SearchSuggestion(
    val title: String,
    val subtitle: String,
    val targetUrl: String,
    val isSearchQuery: Boolean = false,
    val typeIcon: String = "web" // "web", "history", "bookmark", "search"
)

object SmartSuggestions {
    private val popularWebsites = listOf(
        Pair("Arena AI", "https://arena.ai"),
        Pair("Servo Engine", "https://servo.org"),
        Pair("Rust Programming", "https://www.rust-lang.org"),
        Pair("GitHub", "https://github.com"),
        Pair("DuckDuckGo", "https://duckduckgo.com"),
        Pair("Wikipedia", "https://www.wikipedia.org"),
        Pair("Google", "https://www.google.com"),
        Pair("YouTube", "https://www.youtube.com"),
        Pair("Reddit", "https://www.reddit.com"),
        Pair("Stack Overflow", "https://stackoverflow.com"),
        Pair("MDN Web Docs", "https://developer.mozilla.org"),
        Pair("Hacker News", "https://news.ycombinator.com"),
        Pair("Twitter / X", "https://x.com"),
        Pair("Amazon", "https://www.amazon.com"),
        Pair("ChatGPT", "https://chatgpt.com"),
        Pair("LinkedIn", "https://www.linkedin.com"),
        Pair("Spotify Web", "https://open.spotify.com"),
        Pair("Netflix", "https://www.netflix.com"),
        Pair("Twitch", "https://www.twitch.tv"),
        Pair("Discord", "https://discord.com")
    )

    fun getSuggestions(
        query: String,
        history: List<HistoryItem>,
        bookmarks: List<BookmarkItem>,
        searchEngineName: String,
        searchEngineUrlTemplate: String
    ): List<SearchSuggestion> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()

        val results = mutableListOf<SearchSuggestion>()

        // 1. Direct Search suggestion
        val searchTargetUrl = if (searchEngineUrlTemplate.contains("%s")) {
            searchEngineUrlTemplate.replace("%s", android.net.Uri.encode(trimmed))
        } else {
            "https://duckduckgo.com/?q=${android.net.Uri.encode(trimmed)}"
        }

        results.add(
            SearchSuggestion(
                title = "Pesquisar \"$trimmed\"",
                subtitle = searchEngineName,
                targetUrl = searchTargetUrl,
                isSearchQuery = true,
                typeIcon = "search"
            )
        )

        // 2. Matching Popular websites
        val matchingWebsites = popularWebsites.filter { (name, url) ->
            name.contains(trimmed, ignoreCase = true) ||
            url.contains(trimmed, ignoreCase = true)
        }.take(3)

        matchingWebsites.forEach { (name, url) ->
            results.add(
                SearchSuggestion(
                    title = name,
                    subtitle = url.removePrefix("https://").removePrefix("http://"),
                    targetUrl = url,
                    typeIcon = "web"
                )
            )
        }

        // 3. Matching Bookmarks
        val matchingBookmarks = bookmarks.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
            it.url.contains(trimmed, ignoreCase = true)
        }.take(2)

        matchingBookmarks.forEach { bm ->
            if (results.none { it.targetUrl == bm.url }) {
                results.add(
                    SearchSuggestion(
                        title = bm.title,
                        subtitle = bm.url,
                        targetUrl = bm.url,
                        typeIcon = "bookmark"
                    )
                )
            }
        }

        // 4. Matching History
        val matchingHistory = history.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
            it.url.contains(trimmed, ignoreCase = true)
        }.distinctBy { it.url }.take(3)

        matchingHistory.forEach { item ->
            if (results.none { it.targetUrl == item.url }) {
                results.add(
                    SearchSuggestion(
                        title = item.title,
                        subtitle = item.url,
                        targetUrl = item.url,
                        typeIcon = "history"
                    )
                )
            }
        }

        return results.take(6)
    }
}
