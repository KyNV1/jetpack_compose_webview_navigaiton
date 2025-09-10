package com.example.jetpack_compose_webview_navigaiton.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebViewScreen(url: String, modifier: Modifier = Modifier) {
    // Trạng thái để theo dõi tiến độ tải trang
    var loadingProgress by remember { mutableStateOf(0) }
    // Trạng thái để theo dõi URL hiện tại (có thể dùng để hiển thị trên thanh địa chỉ)
    var currentUrl by remember { mutableStateOf(url) }
    // Trạng thái để theo dõi tiêu đề trang
    var pageTitle by remember { mutableStateOf("") }
    // Trạng thái để theo dõi trạng thái tải (đang tải hay đã xong)
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        // Hiển thị thanh tiến độ nếu đang tải
        if (isLoading) {
            LinearProgressIndicator(
                progress = loadingProgress / 100f,
                modifier = Modifier.fillMaxSize()
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
                    // Bật JavaScript
                    settings.javaScriptEnabled = true
                    // Cấu hình các thiết lập khác cho WebView
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.setSupportZoom(true) // Cho phép zoom
                    settings.builtInZoomControls = true // Hiển thị điều khiển zoom
                    settings.displayZoomControls = false // Ẩn điều khiển zoom trên màn hình

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            // Trả về false để WebView tự xử lý việc tải URL
                            // Trả về true nếu bạn muốn xử lý URL đó bằng cách khác (ví dụ: mở trình duyệt bên ngoài)
                            currentUrl = request?.url.toString()
                            Log.d("frank", "shouldOverrideUrlLoading: url = $currentUrl")
                            return false
                        }

                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            loadingProgress = 0
                            pageTitle = view?.title ?: ""
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            loadingProgress = 100
                            pageTitle = view?.title ?: ""
                        }
                    }

                    webChromeClient = object : android.webkit.WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            loadingProgress = newProgress
                        }

                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            pageTitle = title ?: ""
                        }
                    }

                    // Tải URL ban đầu
                    loadUrl(url)
                }
            },
            update = { webView ->
                // Đoạn này sẽ được gọi lại nếu các giá trị state bên ngoài thay đổi
                // Ví dụ, nếu bạn truyền một URL khác vào MyWebViewScreen
                // Tuy nhiên, nếu URL không đổi, bạn có thể không cần gọi loadUrl lại ở đây
                // webView.loadUrl(url)
            }
        )
    }
}