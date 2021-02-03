package com.avion.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient


class HelpDescFragment : Activity() {

    private var progDailog: ProgressDialog? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_help_desc)

        progDailog = ProgressDialog.show(this, "Loading", "Please wait...", true)

        val mWebView = findViewById<WebView>(R.id.helpdesc_webview)
        mWebView.setInitialScale(1)
        mWebView.settings.loadWithOverviewMode = true
        mWebView.settings.useWideViewPort = true
        mWebView.settings.javaScriptEnabled = false
        mWebView.settings.allowFileAccess = true
        mWebView.settings.allowContentAccess = true
        mWebView.isScrollbarFadingEnabled = false
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                progDailog!!.show()
                Log.d("Nek", "Web start load")
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                progDailog!!.dismiss()
                Log.d("Nek", "Web done load")
            }


        }
        mWebView.loadUrl("https://www.google.com/")

    }

    internal inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            progDailog!!.show()
            Log.d("Nek", "Web start load")
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            progDailog!!.dismiss()
            Log.d("Nek", "Web done load")
        }
    }

}
