package com.example.telyshop

import android.app.Activity
import android.content.Context
import android.net.*
import android.os.*
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private class MyWebViewClient() : WebViewClient() {

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            view?.loadUrl("file:///android_asset/error.html")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview.settings.domStorageEnabled = true
        webview.settings.javaScriptEnabled = true
        webview.settings.textZoom = 100
        webview.overScrollMode = View.OVER_SCROLL_NEVER
        webview.webViewClient = MyWebViewClient()
        webview.loadUrl("https://app.telybazaar.com")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()
            cm.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    private var networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            runOnUiThread { kotlin.run {
                webview.loadUrl("https://app.telybazaar.com")
            } }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            runOnUiThread { kotlin.run {
                webview.loadUrl("file:///android_asset/error.html")
            } }
        }
    }

    override fun onBackPressed() {
        if(webview.canGoBack()) {
            webview.goBack()
            return
        }
        super.onBackPressed()
    }
}
