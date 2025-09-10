package com.example.jetpack_compose_webview_navigaiton.webview

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView



@Composable
fun MyWebViewHeader(headerUrl: String){
    val webView = remember { mutableStateOf<WebView?>(null) }

    // Dùng Box để có thể đặt các thành phần chồng lên nhau nếu cần (ví dụ: nút back overlay)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Chiều cao của header, có thể điều chỉnh
            .background(MaterialTheme.colorScheme.primaryContainer) // Màu nền của header
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    // Có thể thêm WebViewClient nếu bạn cần xử lý điều hướng bên trong header
                    // Tuy nhiên, thường thì header sẽ là một trang tĩnh hoặc ít tương tác
                    // Nếu người dùng click vào link trong header và bạn muốn mở browser ngoài,
                    // bạn sẽ cần shouldOverrideUrlLoading trả về true và xử lý Intent.
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            // Mở các link trong header bằng trình duyệt ngoài
                            val intent = android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                request?.url
                            )
                            context.startActivity(intent)
                            return true // Đã xử lý, WebView không tải nữa
                        }
                    }
                    loadUrl(headerUrl)
                    webView.value = this // Lưu instance của WebView để có thể truy cập nếu cần
                }
            },
            update = {
                // Có thể cập nhật URL nếu headerUrl thay đổi
                // Nếu headerUrl là cố định, phần này không cần làm gì
                // if (it.url != headerUrl) { it.loadUrl(headerUrl) }
            }
        )
    }
}

@Composable
fun SettingsMenuItem(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                icon()
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}



@Composable
fun SettingsScreenWebViewHeader(
    headerUrl: String = "file:///android_asset/test.html" ,// Ví dụ: URL đến file HTML cục bộ
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Phần Header (WebView)
        MyWebViewHeader(headerUrl = headerUrl)

        // Các mục native cho Tài khoản
        Text(
            text = "アカウント", // Tài khoản
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        SettingsMenuItem(
            icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account Management") },
            title = "アカウント管理", // Quản lý tài khoản
            description = "名前、メールアドレス、パスワード等", // Tên, địa chỉ email, mật khẩu, v.v.
            onClick = { println("Account Management clicked") }
        )
        Divider(modifier = Modifier.padding(horizontal = 16.dp))

        // Các mục native cho Trợ giúp
        Text(
            text = "ヘルプ", // Trợ giúp
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        SettingsMenuItem(
            icon = { /* Không có icon trong hình, có thể bỏ trống hoặc thêm icon custom */ },
            title = "ヘルプ・お問い合わせ", // Trợ giúp và Liên hệ
            description = "", // Không có mô tả trong hình
            onClick = { println("Help & Contact clicked") }
        )
        Divider(modifier = Modifier.padding(horizontal = 16.dp))

        // Có thể thêm các mục native khác ở đây
    }
}