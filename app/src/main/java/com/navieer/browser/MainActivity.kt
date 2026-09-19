package com.navieer.browser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import com.navieer.browser.model.BookmarkItem
import com.navieer.browser.model.BrowserTab
import com.navieer.browser.model.HistoryItem
import com.navieer.browser.ui.theme.NavieerTheme
import org.servo.servoview.Servo
import org.servo.servoview.ServoNavigator
import org.servo.servoview.ServoView

class MainActivity : ComponentActivity(), Servo.Client {
    private lateinit var servoView: ServoView
    private val navigator = ServoNavigator()

    private val currentUrlState = mutableStateOf("https://servo.org")
    private val currentTitleState = mutableStateOf("Servo - Web Engine")
    private val isLoadingState = mutableStateOf(false)
    private val alertMessageState = mutableStateOf<String?>(null)

    private val tabs = mutableStateListOf<BrowserTab>()
    private val activeTabIdState = mutableStateOf("")

    private val historyList = mutableStateListOf<HistoryItem>()
    private val bookmarkList = mutableStateListOf<BookmarkItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val experimentalMode = prefs.getBoolean("experimental_mode", false)

        val initialUrl = if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
            intent.data.toString()
        } else {
            "https://servo.org"
        }

        val firstTab = BrowserTab(url = initialUrl, title = "Servo Engine")
        tabs.add(firstTab)
        activeTabIdState.value = firstTab.id

        servoView = ServoView(
            context = this,
            client = this,
            servoArgs = intent.getStringExtra("servoargs"),
            servoLog = intent.getStringExtra("servolog"),
            experimentalMode = experimentalMode,
            initialUri = initialUrl,
            navigator = navigator,
        )

        setContent {
            NavieerTheme {
                MainBrowserScreen()
            }
        }
    }

    private fun navigateTo(input: String) {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return

        val url = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("file://") -> trimmed
            trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
            else -> "https://duckduckgo.com/?q=${android.net.Uri.encode(trimmed)}"
        }

        currentUrlState.value = url
        servoView.loadUri(url)
        servoView.requestFocus()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainBrowserScreen() {
        var urlInput by remember { mutableStateOf(currentUrlState.value) }
        var showTabsSheet by remember { mutableStateOf(false) }
        var showHistoryDialog by remember { mutableStateOf(false) }
        var showBookmarksDialog by remember { mutableStateOf(false) }
        var showSettingsDialog by remember { mutableStateOf(false) }
        var showMenu by remember { mutableStateOf(false) }

        val focusManager = LocalFocusManager.current

        LaunchedEffect(currentUrlState.value) {
            urlInput = currentUrlState.value
        }

        BackHandler(enabled = navigator.canGoBackState.value) {
            servoView.goBack()
        }

        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .statusBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button
                        IconButton(
                            onClick = { servoView.goBack() },
                            enabled = navigator.canGoBackState.value,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar",
                                tint = if (navigator.canGoBackState.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }

                        // Forward Button
                        IconButton(
                            onClick = { servoView.goForward() },
                            enabled = navigator.canGoForwardState.value,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Avançar",
                                tint = if (navigator.canGoForwardState.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }

                        // URL / Omnibox Input
                        OutlinedTextField(
                            value = urlInput,
                            onValueChange = { urlInput = it },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .padding(horizontal = 4.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp),
                            placeholder = {
                                Text(
                                    text = "Pesquisar ou digitar URL",
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                val isSecure = currentUrlState.value.startsWith("https://")
                                Icon(
                                    imageVector = if (isSecure) Icons.Default.Lock else Icons.Default.Public,
                                    contentDescription = if (isSecure) "Seguro" else "Web",
                                    tint = if (isSecure) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            trailingIcon = {
                                if (isLoadingState.value) {
                                    IconButton(onClick = { servoView.stop() }, modifier = Modifier.size(24.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Parar",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { servoView.reload() }, modifier = Modifier.size(24.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Recarregar",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Go
                            ),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    focusManager.clearFocus()
                                    navigateTo(urlInput)
                                }
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        // Tabs Button with Counter Badge
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showTabsSheet = true }
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "${tabs.size}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Menu Overflow Button
                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Nova Aba") },
                                    leadingIcon = { Icon(Icons.Default.Add, null) },
                                    onClick = {
                                        showMenu = false
                                        val newTab = BrowserTab(url = "https://servo.org", title = "Nova Aba")
                                        tabs.add(newTab)
                                        activeTabIdState.value = newTab.id
                                        servoView.loadUri("https://servo.org")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Adicionar aos Favoritos") },
                                    leadingIcon = { Icon(Icons.Default.Star, null) },
                                    onClick = {
                                        showMenu = false
                                        bookmarkList.add(
                                            BookmarkItem(
                                                title = currentTitleState.value.ifEmpty { currentUrlState.value },
                                                url = currentUrlState.value
                                            )
                                        )
                                        Toast.makeText(this@MainActivity, "Favorito salvo!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Favoritos") },
                                    leadingIcon = { Icon(Icons.Default.Bookmark, null) },
                                    onClick = {
                                        showMenu = false
                                        showBookmarksDialog = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Histórico") },
                                    leadingIcon = { Icon(Icons.Default.History, null) },
                                    onClick = {
                                        showMenu = false
                                        showHistoryDialog = true
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Configurações") },
                                    leadingIcon = { Icon(Icons.Default.Settings, null) },
                                    onClick = {
                                        showMenu = false
                                        showSettingsDialog = true
                                    }
                                )
                            }
                        }
                    }

                    // Loading Progress Indicator
                    if (isLoadingState.value) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Servo native SurfaceView rendering container
                Servo(
                    servoView = servoView,
                    modifier = Modifier.fillMaxSize()
                )

                // Alert Dialog from web page
                alertMessageState.value?.let { alertMessage ->
                    AlertDialog(
                        onDismissRequest = { alertMessageState.value = null },
                        title = { Text("Mensagem da Página") },
                        text = { Text(alertMessage) },
                        confirmButton = {
                            TextButton(onClick = { alertMessageState.value = null }) {
                                Text("OK")
                            }
                        }
                    )
                }

                // Tabs Modal Bottom Sheet
                if (showTabsSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showTabsSheet = false }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Abas Abertas (${tabs.size})",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = {
                                        val newTab = BrowserTab(url = "https://servo.org", title = "Nova Aba")
                                        tabs.add(newTab)
                                        activeTabIdState.value = newTab.id
                                        servoView.loadUri("https://servo.org")
                                        showTabsSheet = false
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Nova Aba")
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(tabs) { tab ->
                                    val isSelected = tab.id == activeTabIdState.value
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                activeTabIdState.value = tab.id
                                                servoView.loadUri(tab.url)
                                                showTabsSheet = false
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = tab.title,
                                                    fontWeight = FontWeight.SemiBold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = tab.url,
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            if (tabs.size > 1) {
                                                IconButton(
                                                    onClick = {
                                                        val index = tabs.indexOf(tab)
                                                        tabs.remove(tab)
                                                        if (isSelected) {
                                                            val nextTab = tabs.getOrNull(index.coerceAtMost(tabs.size - 1))
                                                            if (nextTab != null) {
                                                                activeTabIdState.value = nextTab.id
                                                                servoView.loadUri(nextTab.url)
                                                            }
                                                        }
                                                    }
                                                ) {
                                                    Icon(Icons.Default.Close, contentDescription = "Fechar Aba")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // History Dialog
                if (showHistoryDialog) {
                    AlertDialog(
                        onDismissRequest = { showHistoryDialog = false },
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Histórico")
                                if (historyList.isNotEmpty()) {
                                    TextButton(onClick = { historyList.clear() }) {
                                        Text("Limpar", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        },
                        text = {
                            if (historyList.isEmpty()) {
                                Text("Nenhum histórico recente registrado.")
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(historyList.reversed()) { item ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    showHistoryDialog = false
                                                    navigateTo(item.url)
                                                }
                                                .padding(vertical = 6.dp)
                                        ) {
                                            Text(item.title, fontWeight = FontWeight.Medium, maxLines = 1)
                                            Text(
                                                item.url,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        HorizontalDivider()
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showHistoryDialog = false }) {
                                Text("Fechar")
                            }
                        }
                    )
                }

                // Bookmarks Dialog
                if (showBookmarksDialog) {
                    AlertDialog(
                        onDismissRequest = { showBookmarksDialog = false },
                        title = { Text("Favoritos") },
                        text = {
                            if (bookmarkList.isEmpty()) {
                                Text("Nenhum favorito salvo ainda.")
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(bookmarkList) { bm ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    showBookmarksDialog = false
                                                    navigateTo(bm.url)
                                                }
                                                .padding(vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(bm.title, fontWeight = FontWeight.Medium, maxLines = 1)
                                                Text(
                                                    bm.url,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            IconButton(
                                                onClick = { bookmarkList.remove(bm) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Remover", modifier = Modifier.size(16.dp))
                                            }
                                        }
                                        HorizontalDivider()
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showBookmarksDialog = false }) {
                                Text("Fechar")
                            }
                        }
                    )
                }

                // Settings Dialog
                if (showSettingsDialog) {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    var experimental by remember { mutableStateOf(prefs.getBoolean("experimental_mode", false)) }

                    AlertDialog(
                        onDismissRequest = { showSettingsDialog = false },
                        title = { Text("Configurações Navieer") },
                        text = {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Modo Experimental Servo", fontWeight = FontWeight.Medium)
                                        Text("Ativa flags e renderização experimental", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Switch(
                                        checked = experimental,
                                        onCheckedChange = {
                                            experimental = it
                                            prefs.edit().putBoolean("experimental_mode", it).apply()
                                            servoView.setExperimentalMode(it)
                                        }
                                    )
                                }

                                Spacer(Modifier.height(16.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(16.dp))

                                Text("Sobre o Motor Servo", fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Navieer utiliza o motor Servo (escrito em Rust com SpiderMonkey para JS, sem Chromium/Gecko) para máximo desempenho e segurança de memória.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showSettingsDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }

    // Servo.Client Callbacks
    override fun onAlert(message: String) {
        alertMessageState.value = message
    }

    override fun onLoadStarted() {
        Log.i("Navieer", "onLoadStarted")
        isLoadingState.value = true
    }

    override fun onLoadEnded() {
        Log.i("Navieer", "onLoadEnded: ${currentUrlState.value}")
        isLoadingState.value = false
        if (currentUrlState.value.isNotEmpty()) {
            historyList.add(
                HistoryItem(
                    title = currentTitleState.value.ifEmpty { currentUrlState.value },
                    url = currentUrlState.value
                )
            )
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
