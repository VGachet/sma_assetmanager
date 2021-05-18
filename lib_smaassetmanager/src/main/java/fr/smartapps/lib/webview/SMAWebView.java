package fr.smartapps.lib.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.InputStream;

import fr.smartapps.lib.SMAAssetManager;

/**
 *
 */
public class SMAWebView extends WebView {

    /*
    Attributes
     */
    private String TAG = "TemplateWebView";
    protected Context context;
    protected boolean loadingSpinner = false;
    protected SMAWebViewListener webViewListener;
    protected SMAAssetManager assetManager;


    /*
    Constructor
     */
    public SMAWebView(Context context) {
        super(context);
        this.context = context;
    }

    public SMAWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SMAWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SMAWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    /*
    Methods
    */
    protected void init() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setDomStorageEnabled(true);

        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

        if (Build.VERSION.SDK_INT <= 18)
            CookieSyncManager.createInstance(context);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.setAcceptFileSchemeCookies(true);

        /*
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        */

        setWebChromeClient(new WebChromeClient());
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (webViewListener != null) {
                    webViewListener.onUrlCall(url);
                }
                return true;
            }

            /*
            Working with OBB
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (assetManager.getDefaultStorageType() == SMAAssetManager.STORAGE_TYPE_OBB && !url.startsWith("http")) {
                    Log.e(TAG, "load webresource : " + url);
                    url = url.replace(assetManager.getAssetsSuffix(), "");
                    InputStream inputStream = assetManager.getInputStream(url);
                    if (url.endsWith(".css")) {
                        return new WebResourceResponse("text/css", "UTF-8", inputStream);
                    }
                    else if (url.endsWith(".m4a")) {
                        return new WebResourceResponse("audio/m4a", "UTF-8", inputStream);
                    }
                    else if (url.endsWith(".mp4")) {
                        return new WebResourceResponse("video/mp4", "UTF-8", inputStream);
                    }
                    else {
                        return new WebResourceResponse("text/plain", "UTF-8", inputStream);
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (context != null) {
                    animate().alpha(1).setDuration(300);
                }
            }
        });

        if (loadingSpinner) {
            setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (webViewListener != null)
                        webViewListener.onUrlLoadProgress(newProgress, 100);
                }
            });
        }
    }

    public void loadTemplate(String baseDirectory, String templateHTML, SMAAssetManager assetManager, SMAWebViewListener webViewListener) {
        init();
        this.webViewListener = webViewListener;
        this.assetManager = assetManager;

        // filter naming strategy
        if (baseDirectory.startsWith(assetManager.STRATEGY_SUFFIX_ASSETS)) {
            baseDirectory.replace(assetManager.STRATEGY_SUFFIX_ASSETS, assetManager.getAssetsSuffix());
        }
        if (baseDirectory.startsWith(assetManager.STRATEGY_SUFFIX_OBB)) {
            baseDirectory.replace(assetManager.STRATEGY_SUFFIX_OBB, assetManager.getAssetsSuffix());
        }
        if (baseDirectory.startsWith(assetManager.STRATEGY_SUFFIX_EXTERNAL)) {
            baseDirectory.replace(assetManager.STRATEGY_SUFFIX_EXTERNAL, assetManager.getExternalPublicStorageSuffix());
        }
        if (baseDirectory.startsWith(assetManager.STRATEGY_SUFFIX_EXTERNAL_PRIVATE)) {
            baseDirectory.replace(assetManager.STRATEGY_SUFFIX_EXTERNAL_PRIVATE, assetManager.getExternalPrivateStorageSuffix());
        }
        else {
            if (assetManager.getDefaultStorageType() == assetManager.STORAGE_TYPE_ASSETS) {
                baseDirectory = assetManager.getAssetsSuffix() + assetManager.getExtensionDirectory() + baseDirectory;
            }
            else if (assetManager.getDefaultStorageType() == assetManager.STORAGE_TYPE_OBB) {
                baseDirectory = assetManager.getAssetsSuffix() + assetManager.getExtensionDirectory() + baseDirectory;
            }
            else if (assetManager.getDefaultStorageType() == assetManager.STORAGE_TYPE_EXTERNAL) {
                baseDirectory = "file://" + assetManager.getExternalPublicStorageSuffix() + assetManager.getExtensionDirectory() + baseDirectory;
            }
            else if (assetManager.getDefaultStorageType() == assetManager.STORAGE_TYPE_EXTERNAL_PRIVATE) {
                baseDirectory = "file://" + assetManager.getExternalPrivateStorageSuffix() + assetManager.getExtensionDirectory() + baseDirectory;
            }

        }

        // load template
        Log.e(TAG, "loadDataWithBaseUrl : " + baseDirectory);
        loadDataWithBaseURL(baseDirectory, templateHTML, "text/html", "UTF-8", null);
    }

    public void loadTemplate(String templateHTML, SMAAssetManager assetManager, SMAWebViewListener webViewListener) {
        this.assetManager = assetManager;
        loadTemplate("", templateHTML, assetManager, webViewListener);
    }

    public void loadPath(String url, SMAAssetManager assetManager, SMAWebViewListener webViewListener) {
        this.webViewListener = webViewListener;
        this.assetManager = assetManager;
        if (url.startsWith("http")) {
            loadingSpinner = true;
            init();
            super.loadUrl(url);
        }
        else {
            if(!url.endsWith(".html")) {
                if(!url.endsWith("/"))
                    url += "/";
                url += "index.html";
            }

            init();

            // filter naming strategy
            if (url.startsWith(assetManager.STRATEGY_SUFFIX_ASSETS)) {
                url = url.replace(assetManager.STRATEGY_SUFFIX_ASSETS, "");
            }
            if (url.startsWith(assetManager.STRATEGY_SUFFIX_OBB)) {
                url = url.replace(assetManager.STRATEGY_SUFFIX_OBB, "");
            }
            if (url.startsWith(assetManager.STRATEGY_SUFFIX_EXTERNAL)) {
                url = url.replace(assetManager.STRATEGY_SUFFIX_EXTERNAL, "");
            }
            if (url.startsWith(assetManager.STRATEGY_SUFFIX_EXTERNAL_PRIVATE)) {
                url = url.replace(assetManager.STRATEGY_SUFFIX_EXTERNAL_PRIVATE, "");
            }

            // filter naming default storage
            if (url.startsWith(assetManager.getAssetsSuffix()) || url.startsWith("file://" + assetManager.getExternalPublicStorageSuffix()) || url.startsWith("file://" + assetManager.getExternalPrivateStorageSuffix())) {
                Log.e(TAG, "loadUrl : " + url);
                super.loadUrl(url);
                return;
            }

            // load url
            switch (assetManager.getDefaultStorageType()) {
                case SMAAssetManager.STORAGE_TYPE_NANCY_ASSETS:
                    Log.d(TAG, "loadUrl : " + assetManager.getAssetsSuffix() + assetManager.getExtensionDirectory() + url);
                    super.loadUrl(assetManager.getAssetsSuffix() + url);
                    break;
                case SMAAssetManager.STORAGE_TYPE_ASSETS:
                    Log.e(TAG, "loadUrl : " + assetManager.getAssetsSuffix() + assetManager.getExtensionDirectory() + url);
                    super.loadUrl(assetManager.getAssetsSuffix() + assetManager.getExtensionDirectory() + url);
                    break;
                case SMAAssetManager.STORAGE_TYPE_OBB:
                    File file = new File(url);
                    Log.e(TAG, "loadUrl : " + assetManager.getAssetsSuffix() + assetManager.getExtensionDirectory() + file.getParent() + "/");
                    loadTemplate(assetManager.getAssetsSuffix()
                            + (url.startsWith(assetManager.getExtensionDirectory()) ? assetManager.getExtensionDirectory() : "")
                            + file.getParent()
                            + "/",
                            assetManager.getString(url),
                            assetManager,
                            webViewListener);
                    break;
                case SMAAssetManager.STORAGE_TYPE_EXTERNAL:
                    Log.e(TAG, "loadUrl : " + "file://" + assetManager.getExternalPublicStorageSuffix() + assetManager.getExtensionDirectory() + url);
                    super.loadUrl("file://" + assetManager.getExternalPublicStorageSuffix() + assetManager.getExtensionDirectory() + url);
                    break;
                case SMAAssetManager.STORAGE_TYPE_EXTERNAL_PRIVATE:
                    Log.e(TAG, "loadUrl : " + "file://" + assetManager.getExternalPrivateStorageSuffix() + assetManager.getExtensionDirectory() + url);
                    super.loadUrl("file://" + assetManager.getExternalPrivateStorageSuffix() + assetManager.getExtensionDirectory() + url);
                    break;
                case SMAAssetManager.STORAGE_TYPE_UNDEFINED:
                    Log.e(TAG, "loadUrl : " + url);
                    super.loadUrl(url);
                    break;
            }
        }
    }
}
