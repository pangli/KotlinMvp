package com.zorro.kotlin.baselibs.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;


/**
 * Created by Zorro on 2018/8/27.
 * 备注：原生WebView控制
 */

public class WebViewController {
    private WeakReference<Context> contextWeakReference;
    private Context mContext;
    private FrameLayout webViewConsole;
    private WebView webView;
    private WebSettings webSettings;
    //the name used to expose the object in JavaScript
    private String name = "android";
    private String url;

    public synchronized WebView getWebView() {
        return webView;
    }

    public WebViewController(Context context, FrameLayout webViewConsole) {
        this.contextWeakReference = new WeakReference<>(context);
        this.mContext = context;
        this.webViewConsole = webViewConsole;
        webView = new WebView(mContext);
        initWidget();
    }

    private void initWidget() {
        webViewConsole.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        initWebViewSettings();
        setWebViewClient(null);
        onResume();
    }

    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url) && webView != null) {
            this.url = url;
            webView.loadUrl(url);
        }
    }

    /**
     * 刷新
     */
    public void reload() {
        if (webView != null) {
            webView.reload();
        }
    }

    /**
     * @param androidJavaScript the Java object to inject into this WebView's JavaScript context. Null values are
     *                          ignored.
     * @param name              the name used to expose the object in JavaScript
     *                          name==null default android
     */
    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(AndroidJavaScript androidJavaScript, String name) {
        if (webView != null) {
            if (TextUtils.isEmpty(name)) {
                webView.addJavascriptInterface(androidJavaScript, "android");
            } else {
                this.name = name;
                webView.addJavascriptInterface(androidJavaScript, name);
            }
        }
    }

    public void setUserAgent(String userAgent) {
        if (webSettings != null) {
            webSettings.setUserAgentString(userAgent);
        }
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        if (webView != null) {
            if (webViewClient == null) {
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed();//https接受证书
                    }
                });
            } else {
                webView.setWebViewClient(webViewClient);
            }
        }

    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        if (webView != null && webChromeClient != null) {
            webView.setWebChromeClient(webChromeClient);
        }
    }

    /**
     * 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
     */
    public void goBack() {
        if (webView != null) {
            webView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { // 表示按返回键
                            // 时的操作
                            webView.goBack(); // 后退
                            // webview.goForward();//前进
                            return true; // 已处理
                        }
                    }
                    return false;
                }
            });
        }
    }

    public void synchronousWebCookies(String cookies) {
        if (!TextUtils.isEmpty(url))
            if (!TextUtils.isEmpty(cookies)) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.createInstance(mContext);
                }
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();
                cookieManager.setCookie(url, cookies);//为url设置cookie
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                        @Override
                        public void run() {
                            CookieManager.getInstance().flush();
                        }
                    });
                }
            }
    }


    public void onResume() {
        if (webView != null) {
            //恢复pauseTimers状态
            webView.resumeTimers();
            //激活WebView为活跃状态，能正常执行网页的响应
            webView.onResume();
        }
    }

    public void onPause() {
        if (webView != null) {
            //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
            webView.pauseTimers();
            //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
            //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
            webView.onPause();
        }
    }

    public void onDestroy() {
        if (webViewConsole != null) {
            webViewConsole.removeAllViews();
        }
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.removeJavascriptInterface(name);
            webView.stopLoading();
            if (webSettings != null) {
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                webSettings.setJavaScriptEnabled(false);
            }
            webView.clearView();
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.freeMemory();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                //this is causing the segfault occasionally below 4.2
                webView.destroy();
            }
            webView = null;
        }
    }

    private void initWebViewSettings() {
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置可访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(true); //隐藏原生的缩放控件
        //设置编码格式
        webSettings.setDefaultTextEncodingName("UTF-8");
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setSupportMultipleWindows(true);
        // Application Caches 缓存
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        //设置缓存模式
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        //webSettings.setDatabaseEnabled(true);//开启 database storage API 功能
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //设置  Application Caches 缓存目录
        // webSettings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        //WebView中5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        webSettings.setUserAgentString(webSettings.getUserAgentString() + "android");
    }
}
