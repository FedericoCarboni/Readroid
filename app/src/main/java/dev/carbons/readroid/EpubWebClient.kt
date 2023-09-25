package dev.carbons.readroid

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import dev.carbons.readroid.epub.EpubFile

class EpubWebClient constructor(private val epubFile: EpubFile) : WebViewClient() {
//    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//        println(request)
//        return true
//    }
    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        println(request.url.path)
        println(request.url.scheme)
        if (request.url.scheme != "dev.carbons.readroid.epubfile") return null
        val res = epubFile.getResourceByPath(request.url.path.toString().drop(1)) ?: return null
        println(res)
        return WebResourceResponse(null, null, res)
    }
}