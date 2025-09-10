package com.example.jetpack_compose_webview_navigaiton.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun AdvancedWebViewScreen(
    modifier: Modifier = Modifier
) {
    var url by remember { mutableStateOf("https://kenh14.vn/") }
    var urlToLoad by remember { mutableStateOf(url) }
    var webView: WebView? by remember { mutableStateOf(null) }
    var loadingProgress by remember { mutableFloatStateOf(0.0f) }
    var canGoBack by remember { mutableStateOf(false) }
    var lastLoadedUrl by remember { mutableStateOf("") }

    // This BackHandler is used to handle the back button press
    BackHandler(
        enabled = canGoBack
    ) {
        webView?.goBack()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Compose WebView") },
                actions = {
                    Button(onClick = { urlToLoad = url }) {
                        Text("Go")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Enter URL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            if (loadingProgress < 1.0f) {
                LinearProgressIndicator(
                    progress = loadingProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        // Update canGoBack state when page finishes loading
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(
                                view: WebView?,
                                url: String?
                            ) {
                                super.onPageFinished(view, url)
                                canGoBack = view?.canGoBack() ?: false
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            // Update loading progress
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                loadingProgress = newProgress / 100.0f
                            }
                        }
                        // Assign webView to state for external access
                        webView = this
                        loadUrl(urlToLoad)
                        lastLoadedUrl = urlToLoad
                    }
                },
                update = { webView ->
                    if (lastLoadedUrl != urlToLoad) {
                        webView.loadUrl(urlToLoad)
                        lastLoadedUrl = urlToLoad
                    }
                }
            )

            // Properly clean up WebView to avoid memory leaks
            androidx.compose.runtime.DisposableEffect(Unit) {
                onDispose {
                    webView?.destroy()
                    webView = null
                }
            }
        }
    }
}