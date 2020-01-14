package com.zorro.kotlin.baselibs.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zorro.kotlin.baselibs.R;
import com.zorro.kotlin.baselibs.utils.CommonUtil;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * X5WebView
 */
public class X5WebView extends WebView {
    private ProgressBar progressBar;
    private String name = "android";

    public X5WebView(Context arg0) {
        super(arg0);
        setBackgroundColor(85621);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
        this.getView().setClickable(true);
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSetting.setUserAgentString(webSetting.getUserAgentString() + "android");
        //true为网络图片堵塞可以加速网页加载，在网页全部加载成功后设置为false
        //webSetting.setBlockNetworkImage(false);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计

        //Android webView从Lollipop(5.0)开始webView默认不允许混合模式,https当中不能加载http资源,而当开发的时候如果使用的是https链接,但是链接中的图片又是http的就有可能会出现加载不了图片的现象,这需要开启支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * 设备和X5内核版本信息展示
     */
//    @Override
//    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        boolean ret = super.drawChild(canvas, child, drawingTime);
//        canvas.save();
//        Paint paint = new Paint();
//        paint.setColor(0x7fff0000);
//        paint.setTextSize(24.f);
//        paint.setAntiAlias(true);
//        if (getX5WebViewExtension() != null) {
//            canvas.drawText(this.getContext().getPackageName() + "-pid:"
//                    + android.os.Process.myPid(), 10, 50, paint);
//            canvas.drawText(
//                    "X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
//                    100, paint);
//        } else {
//            canvas.drawText(this.getContext().getPackageName() + "-pid:"
//                    + android.os.Process.myPid(), 10, 50, paint);
//            canvas.drawText("Sys Core", 10, 100, paint);
//        }
//        canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
//        canvas.drawText(Build.MODEL, 10, 200, paint);
//        canvas.restore();
//        return ret;
//    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * 创建进度条
     */
    public void addProgressBar() {
        //创建进度条
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        //设置加载进度条的高度
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, CommonUtil.INSTANCE.dp2px(getContext(), 3), Gravity.TOP));
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.color_progressbar);
        progressBar.setProgressDrawable(drawable);
        //添加进度到WebView
        addView(progressBar);
        addWebViewClient(client);
        addWebChromeClient(chromeClient);
    }

    private TitleListener tittleListener;

    public void setTittleListener(TitleListener tittleListener) {
        this.tittleListener = tittleListener;
    }


    public interface TitleListener {
        void setTitle(String title);
    }

    /**
     * 添加 WebViewClient
     *
     * @param webViewClient
     */
    public void addWebViewClient(WebViewClient webViewClient) {
        if (webViewClient == null) {
            this.setWebViewClient(client);
        } else {
            this.setWebViewClient(webViewClient);
        }
    }

    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (tittleListener != null) {
                tittleListener.setTitle(webView.getTitle());
            }

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }

        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            //super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();//https忽略证书问题
        }

    };

    /**
     * 添加 WebChromeClient
     *
     * @param webChromeClient
     */
    public void addWebChromeClient(WebChromeClient webChromeClient) {
        if (webChromeClient == null) {
            this.setWebChromeClient(chromeClient);
        } else {
            this.setWebChromeClient(webChromeClient);
        }
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            super.onProgressChanged(webView, newProgress);
            if (progressBar != null) {
                progressBar.setProgress(newProgress);
            }
        }
    };

    /**
     * @param androidJavaScript the Java object to inject into this WebView's JavaScript context. Null values are
     *                          ignored.
     * @param name              the name used to expose the object in JavaScript
     *                          name==null default android
     */
    public void bindJavascriptInterface(AndroidJavaScript androidJavaScript, String name) {
        if (TextUtils.isEmpty(name)) {
            addJavascriptInterface(androidJavaScript, "android");
        } else {
            this.name = name;
            addJavascriptInterface(androidJavaScript, name);
        }
    }

    @Override
    public void destroy() {
        setWebViewClient(null);
        removeJavascriptInterface(name);
        stopLoading();
        clearHistory();
        removeAllViews();
        clearCache(true);
        loadUrl("about:blank");
        super.destroy();
    }
}
