package com.navieer.browser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import com.navieer.browser.data.AdBlockRules
import com.navieer.browser.data.BrowserPreferences
import com.navieer.browser.data.SearchEngine
import com.navieer.browser.data.SmartSuggestions
import com.navieer.browser.model.BookmarkItem
import com.navieer.browser.model.BrowserTab
import com.navieer.browser.model.DownloadItem
import com.navieer.browser.model.HistoryItem
import com.navieer.browser.ui.components.*
import com.navieer.browser.ui.theme.NavieerTheme
import kotlinx.coroutines.launch
import org.servo.servoview.Servo
import org.servo.servoview.ServoView

class MainActivity : ComponentActivity(), Servo.Client {
    private lateinit var servoView: ServoView
    private lateinit var browserPreferences: BrowserPreferences

    // Navigation and Page States
    private val canGoBackState = mutableStateOf(false)
    private val canGoForwardState = mutableStateOf(false)
    private val currentUrlState = mutableStateOf("navieer://home")
    private val currentTitleState = mutableStateOf("Nova Aba")
    private val isLoadingState = mutableStateOf(false)
    private val alertMessageState = mutableStateOf<String?>(null)
    private val dynamicSiteColorState = mutableStateOf<Color?>(null)

    // User Gesture States: Omnibox Visibility
    private val isOmniboxVisibleState = mutableStateOf(true)

    // Tab Management
    private val tabs = mutableStateListOf<BrowserTab>()
    private val activeTabIdState = mutableStateOf("")

    // Persistent Collections
    private val historyList = mutableStateListOf<HistoryItem>()
    private val bookmarkList = mutableStateListOf<BookmarkItem>()
    private val downloadList = mutableStateListOf<DownloadItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        browserPreferences = BrowserPreferences(applicationContext)

        val initialUrl = if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
            intent.data.toString()
        } else {
            "navieer://home"
        }

        val firstTab = BrowserTab(url = initialUrl, title = if (initialUrl == "navieer://home") "Nova Aba" else "Servo Engine")
        tabs.add(firstTab)
        activeTabIdState.value = firstTab.id
        currentUrlState.value = initialUrl

        // Initialize Native ServoView (Servo v0.5.0 embedding)
        servoView = ServoView(this).apply {
            setClient(this@MainActivity)
            setServoArgs(
                intent.getStringExtra("servoargs"),
                intent.getStringExtra("servolog"),
                false
            )
            if (initialUrl != "navieer://home") {
                loadUri(initialUrl)
            }
        }

        setContent {
            val isAmoledMode by browserPreferences.amoledModeFlow.collectAsState(initial = true)
            val isAdBlockEnabled by browserPreferences.adBlockEnabledFlow.collectAsState(initial = true)
            val isDesktopMode by browserPreferences.desktopModeFlow.collectAsState(initial = false)
            val isExperimentalServo by browserPreferences.servoExperimentalFlow.collectAsState(initial = false)
            val isWebGpu by browserPreferences.servoWebGpuFlow.collectAsState(initial = false)
            val isForceDark by browserPreferences.forceDarkModeFlow.collectAsState(initial = false)
            val currentSearchEngine by browserPreferences.searchEngineFlow.collectAsState(initial = SearchEngine.DUCKDUCKGO)
            val customSearchUrl by browserPreferences.customSearchUrlFlow.collectAsState(initial = "https://duckduckgo.com/?q=%s")

            NavieerTheme(
                isAmoledMode = isAmoledMode,
                dynamicSiteColor = dynamicSiteColorState.value
            ) {
                MainBrowserScreen(
                    isAmoledMode = isAmoledMode,
                    isAdBlockEnabled = isAdBlockEnabled,
                    isDesktopMode = isDesktopMode,
                    isExperimentalServo = isExperimentalServo,
                    isWebGpu = isWebGpu,
                    isForceDark = isForceDark,
                    currentSearchEngine = currentSearchEngine,
                    customSearchUrl = customSearchUrl
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        servoView.onPause()
    }

    override fun onResume() {
        super.onResume()
        servoView.onResume()
    }

    private fun navigateTo(input: String, searchEngine: SearchEngine, customSearchUrl: String) {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return

        if (trimmed == "navieer://home") {
            currentUrlState.value = "navieer://home"
            currentTitleState.value = "Nova Aba"
            return
        }

        val targetUrl = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("file://") -> trimmed
            trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
            else -> {
                val encoded = android.net.Uri.encode(trimmed)
                if (searchEngine == SearchEngine.CUSTOM && customSearchUrl.contains("%s")) {
                    customSearchUrl.replace("%s", encoded)
                } else {
                    searchEngine.searchUrl.replace("%s", encoded)
                }
            }
        }

        // AdBlock verification
        if (AdBlockRules.isAdOrTracker(targetUrl)) {
            Toast.makeText(this, "Anúncio/Rastreador bloqueado pelo Navieer Shield", Toast.LENGTH_SHORT).show()
            return
        }

        currentUrlState.value = targetUrl
        val activeTab = tabs.find { it.id == activeTabIdState.value }
        activeTab?.url = targetUrl

        servoView.loadUri(targetUrl)
        servoView.requestFocus()
    }

    @Composable
    private fun MainBrowserScreen(
        isAmoledMode: Boolean,
        isAdBlockEnabled: Boolean,
        isDesktopMode: Boolean,
        isExperimentalServo: Boolean,
        isWebGpu: Boolean,
        isForceDark: Boolean,
        currentSearchEngine: SearchEngine,
        customSearchUrl: String
    ) {
        var showTabsSheet by remember { mutableStateOf(false) }
        var showHistorySheet by remember { mutableStateOf(false) }
        var showBookmarksSheet by remember { mutableStateOf(false) }
        var showDownloadsSheet by remember { mutableStateOf(false) }
        var showSettingsSheet by remember { mutableStateOf(false) }
        var showFlagsSheet by remember { mutableStateOf(false) }
        var showSiteInfoSheet by remember { mutableStateOf(false) }
        var isReaderModeActive by remember { mutableStateOf(false) }

        // Smart suggestions state
        var omniboxTypedQuery by remember { mutableStateOf("") }
        val suggestions = remember(omniboxTypedQuery, historyList.size, bookmarkList.size, currentSearchEngine) {
            if (omniboxTypedQuery.isBlank()) emptyList()
            else SmartSuggestions.getSuggestions(
                query = omniboxTypedQuery,
                history = historyList,
                bookmarks = bookmarkList,
                searchEngineName = currentSearchEngine.title,
                searchEngineUrlTemplate = if (currentSearchEngine == SearchEngine.CUSTOM) customSearchUrl else currentSearchEngine.searchUrl
            )
        }

        // Back Handler (allows native Android gesture back without consuming webview touches)
        BackHandler(enabled = canGoBackState.value && currentUrlState.value != "navieer://home") {
            servoView.goBack()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main Web View or Start Page (Native ServoView receives unhindered touch events for scrolling)
            if (currentUrlState.value == "navieer://home") {
                SpeedDialView(
                    onNavigate = { url ->
                        navigateTo(url, currentSearchEngine, customSearchUrl)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AndroidView(
                    factory = { servoView },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Floating Dynamic Omnibox (Material 3 Expressive)
            OmniboxFloatingBar(
                url = if (currentUrlState.value == "navieer://home") "" else currentUrlState.value,
                isLoading = isLoadingState.value,
                tabCount = tabs.size,
                isDesktopMode = isDesktopMode,
                isAdBlockActive = isAdBlockEnabled,
                isVisible = isOmniboxVisibleState.value,
                suggestions = suggestions,
                onQueryChanged = { query -> omniboxTypedQuery = query },
                onNavigate = { input ->
                    omniboxTypedQuery = ""
                    navigateTo(input, currentSearchEngine, customSearchUrl)
                },
                onReload = { servoView.reload() },
                onStop = { servoView.stop() },
                onToggleDesktop = {
                    val newMode = !isDesktopMode
                    lifecycleScope.launch {
                        browserPreferences.setDesktopMode(newMode)
                        servoView.reload()
                        Toast.makeText(this@MainActivity, if (newMode) "Modo Desktop Ativado" else "Modo Mobile Ativado", Toast.LENGTH_SHORT).show()
                    }
                },
                onToggleReaderMode = {
                    isReaderModeActive = !isReaderModeActive
                },
                onOpenSiteInfo = {
                    showSiteInfoSheet = true
                },
                onOpenTabs = { showTabsSheet = true },
                onOpenHistory = { showHistorySheet = true },
                onOpenBookmarks = { showBookmarksSheet = true },
                onOpenDownloads = { showDownloadsSheet = true },
                onOpenSettings = { showSettingsSheet = true },
                onOpenFlags = { showFlagsSheet = true },
                onAddBookmark = {
                    bookmarkList.add(
                        BookmarkItem(
                            title = currentTitleState.value.ifEmpty { currentUrlState.value },
                            url = currentUrlState.value
                        )
                    )
                    Toast.makeText(this@MainActivity, "Favorito adicionado!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            )

            // Site Security & Permissions Modal Sheet
            if (showSiteInfoSheet) {
                SiteInfoSheet(
                    url = currentUrlState.value,
                    title = currentTitleState.value,
                    onEditUrl = { url ->
                        omniboxTypedQuery = url
                    },
                    onClearSiteData = { host ->
                        Toast.makeText(this@MainActivity, "Cookies e dados locais de $host limpos", Toast.LENGTH_SHORT).show()
                    },
                    onDismiss = { showSiteInfoSheet = false }
                )
            }

            // Reader Mode Overlay
            if (isReaderModeActive) {
                ReaderView(
                    title = currentTitleState.value,
                    url = currentUrlState.value,
                    content = "",
                    onClose = { isReaderModeActive = false }
                )
            }

            // JavaScript Alert Dialog
            alertMessageState.value?.let { alertMessage ->
                AlertDialog(
                    onDismissRequest = { alertMessageState.value = null },
                    shape = RoundedCornerShape(28.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    title = { Text("Mensagem da Página", fontWeight = FontWeight.Bold) },
                    text = { Text(alertMessage) },
                    confirmButton = {
                        TextButton(
                            onClick = { alertMessageState.value = null },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            // Tab Switcher Grid Sheet (Grade de Cartões)
            if (showTabsSheet) {
                TabGridView(
                    tabs = tabs,
                    activeTabId = activeTabIdState.value,
                    onTabSelected = { tab ->
                        activeTabIdState.value = tab.id
                        currentUrlState.value = tab.url
                        currentTitleState.value = tab.title
                        if (tab.url != "navieer://home") {
                            servoView.loadUri(tab.url)
                        }
                    },
                    onTabClosed = { tab ->
                        val index = tabs.indexOf(tab)
                        tabs.remove(tab)
                        if (tab.id == activeTabIdState.value) {
                            val nextTab = tabs.getOrNull(index.coerceAtMost(tabs.size - 1))
                            if (nextTab != null) {
                                activeTabIdState.value = nextTab.id
                                currentUrlState.value = nextTab.url
                                currentTitleState.value = nextTab.title
                                if (nextTab.url != "navieer://home") {
                                    servoView.loadUri(nextTab.url)
                                }
                            } else {
                                val fresh = BrowserTab(url = "navieer://home", title = "Nova Aba")
                                tabs.add(fresh)
                                activeTabIdState.value = fresh.id
                                currentUrlState.value = "navieer://home"
                            }
                        }
                    },
                    onNewTab = { isIncognito ->
                        val newTab = BrowserTab(
                            url = "navieer://home",
                            title = if (isIncognito) "Aba Privada" else "Nova Aba",
                            isIncognito = isIncognito,
                            previewColor = if (isIncognito) 0xFFFB7185 else 0xFF6366F1
                        )
                        tabs.add(newTab)
                        activeTabIdState.value = newTab.id
                        currentUrlState.value = "navieer://home"
                        currentTitleState.value = newTab.title
                    },
                    onCloseAll = { isIncognito ->
                        tabs.removeAll { it.isIncognito == isIncognito }
                        if (tabs.isEmpty()) {
                            val fresh = BrowserTab(url = "navieer://home", title = "Nova Aba")
                            tabs.add(fresh)
                            activeTabIdState.value = fresh.id
                            currentUrlState.value = "navieer://home"
                        } else {
                            activeTabIdState.value = tabs.first().id
                            currentUrlState.value = tabs.first().url
                        }
                    },
                    onDismiss = { showTabsSheet = false }
                )
            }

            // History Sheet
            if (showHistorySheet) {
                HistorySheet(
                    historyList = historyList,
                    onSelectUrl = { url ->
                        navigateTo(url, currentSearchEngine, customSearchUrl)
                    },
                    onClearHistory = { historyList.clear() },
                    onDismiss = { showHistorySheet = false }
                )
            }

            // Bookmarks Sheet
            if (showBookmarksSheet) {
                BookmarksSheet(
                    bookmarks = bookmarkList,
                    onSelectUrl = { url ->
                        navigateTo(url, currentSearchEngine, customSearchUrl)
                    },
                    onDeleteBookmark = { bookmarkList.remove(it) },
                    onDismiss = { showBookmarksSheet = false }
                )
            }

            // Downloads Sheet
            if (showDownloadsSheet) {
                DownloadsSheet(
                    downloads = downloadList,
                    onCancelDownload = { downloadList.remove(it) },
                    onClearCompleted = { downloadList.removeAll { it.isCompleted } },
                    onDismiss = { showDownloadsSheet = false }
                )
            }

            // Flags Sheet (navieer://flags)
            if (showFlagsSheet) {
                FlagsSheet(
                    isExperimentalServo = isExperimentalServo,
                    isWebGpu = isWebGpu,
                    isForceDark = isForceDark,
                    onToggleExperimentalServo = { enabled ->
                        lifecycleScope.launch {
                            browserPreferences.setServoExperimental(enabled)
                            servoView.setExperimentalMode(enabled)
                        }
                    },
                    onToggleWebGpu = { enabled ->
                        lifecycleScope.launch { browserPreferences.setServoWebGpu(enabled) }
                    },
                    onToggleForceDark = { enabled ->
                        lifecycleScope.launch { browserPreferences.setForceDarkMode(enabled) }
                    },
                    onDismiss = { showFlagsSheet = false }
                )
            }

            // Settings Sheet
            if (showSettingsSheet) {
                SettingsSheet(
                    currentSearchEngine = currentSearchEngine,
                    customSearchUrl = customSearchUrl,
                    isAmoledMode = isAmoledMode,
                    isAdBlockEnabled = isAdBlockEnabled,
                    onSelectSearchEngine = { engine ->
                        lifecycleScope.launch { browserPreferences.setSearchEngine(engine) }
                    },
                    onUpdateCustomSearchUrl = { url ->
                        lifecycleScope.launch { browserPreferences.setCustomSearchUrl(url) }
                    },
                    onToggleAmoled = { enabled ->
                        lifecycleScope.launch { browserPreferences.setAmoledMode(enabled) }
                    },
                    onToggleAdBlock = { enabled ->
                        lifecycleScope.launch { browserPreferences.setAdBlockEnabled(enabled) }
                    },
                    onDismiss = { showSettingsSheet = false }
                )
            }
        }
    }

    // Servo.Client Callbacks (Servo Engine Native Events)
    override fun onAlert(message: String) {
        alertMessageState.value = message
    }

    override fun onLoadStarted() {
        Log.i("Navieer", "onLoadStarted")
        isLoadingState.value = true
        isOmniboxVisibleState.value = true
    }

    override fun onLoadEnded() {
        Log.i("Navieer", "onLoadEnded: ${currentUrlState.value}")
        isLoadingState.value = false

        if (currentUrlState.value.isNotEmpty() && currentUrlState.value != "navieer://home") {
            val activeTab = tabs.find { it.id == activeTabIdState.value }
            // Only add to history if not in incognito mode
            if (activeTab?.isIncognito != true) {
                historyList.add(
                    HistoryItem(
                        title = currentTitleState.value.ifEmpty { currentUrlState.value },
                        url = currentUrlState.value
                    )
                )
            }
        }
    }

    override fun onTitleChanged(title: String) {
        currentTitleState.value = title
        val activeTab = tabs.find { it.id == activeTabIdState.value }
        activeTab?.title = title
    }

    override fun onUrlChanged(url: String) {
        currentUrlState.value = url
        val activeTab = tabs.find { it.id == activeTabIdState.value }
        activeTab?.url = url
    }

    override fun onHistoryChanged(canGoBack: Boolean, canGoForward: Boolean) {
        canGoBackState.value = canGoBack
        canGoForwardState.value = canGoForward
    }

    override fun onRedrawing(redrawing: Boolean) {
        // Redrawing callback from Servo engine
    }

    override fun onImeShow() {
        getSystemService<InputMethodManager>()
            ?.showSoftInput(servoView, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onImeHide() {
        getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(servoView.windowToken, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onMediaSessionMetadata(title: String, artist: String, album: String) {
        Log.d("Navieer", "Media: $title - $artist ($album)")
    }

    override fun onMediaSessionPlaybackStateChange(state: Int) {
        Log.d("Navieer", "Media Playback state: $state")
    }

    override fun onMediaSessionSetPositionState(duration: Float, position: Float, playbackRate: Float) {
        Log.d("Navieer", "Media Position: $position/$duration @ $playbackRate")
    }
}
