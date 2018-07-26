package top.wupanhao.www.rss_client_kotlin

import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.*
import kotlinx.android.synthetic.main.web_layout.*

/**
 * Created by hao on 2018/7/25.
 */
class WebActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_layout)
        webview.settings.javaScriptEnabled = true
//        webview.settings.builtInZoomControls = true
//        webview.settings.supportZoom()
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true
        webview.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.path);
                return true;
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
            }
        }
        val url = intent.getStringExtra("url")
        if(url!= null && url.isNotEmpty())
            Log.d("WebActivity",url);//在logcat中可以得到显示结果
            webview.loadUrl(url)
    }

    override fun onBackPressed() {
        //判断WebView是否可返回
        if (webview.canGoBack()) {
            //返回上一个页
            webview.goBack()
            return
        }
        super.onBackPressed()
    }
}