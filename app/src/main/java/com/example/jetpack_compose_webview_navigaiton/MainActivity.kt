package com.example.jetpack_compose_webview_navigaiton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpack_compose_webview_navigaiton.ui.theme.Jetpack_compose_webview_navigaitonTheme
import com.example.jetpack_compose_webview_navigaiton.webview.AdvancedWebViewScreen
import com.example.jetpack_compose_webview_navigaiton.webview.MyWebViewScreen
import com.example.jetpack_compose_webview_navigaiton.webview.SettingsScreenWebViewHeader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jetpack_compose_webview_navigaitonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //  AdvancedWebViewScreen(modifier = Modifier.padding(innerPadding))
//                    MyWebViewScreen(
//                        url = "https://developer.android.com/",
//                        modifier = Modifier.padding(innerPadding)
//                    )

                    SettingsScreenWebViewHeader(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
