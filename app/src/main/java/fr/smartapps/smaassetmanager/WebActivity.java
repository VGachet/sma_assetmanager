package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fr.smartapps.lib.SMAAssetManager;
import fr.smartapps.lib.webview.SMAWebView;
import fr.smartapps.lib.webview.SMAWebViewListener;

public class WebActivity extends AppCompatActivity {

    private String TAG = "WebActivity";
    protected SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        assetManager = new SMAAssetManager(this);
        assetManager.initMainOBB(1, 46548526);

        Button button_assets = (Button) findViewById(R.id.button_assets);
        button_assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMAWebView webView = (SMAWebView) findViewById(R.id.webview);
                assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_ASSETS);
                webView.loadTemplate("html/", assetManager.getString("html/index.html"), assetManager, new SMAWebViewListener() {
                    @Override
                    public void onUrlLoadProgress(int progress, int totalProgress) {
                        Log.e(TAG, "onUrlLoadProgress : " + progress + " / " + totalProgress);
                    }

                    @Override
                    public void onUrlCall(String url) {
                        Log.e(TAG, "onUrlCall : " + url);
                    }
                });
            }
        });
        Button button_public = (Button) findViewById(R.id.button_external_public);
        button_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMAWebView webView = (SMAWebView) findViewById(R.id.webview);
                assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_EXTERNAL);
                webView.loadTemplate("html/", assetManager.getString("html/index.html"), assetManager, new SMAWebViewListener() {
                    @Override
                    public void onUrlLoadProgress(int progress, int totalProgress) {
                        Log.e(TAG, "onUrlLoadProgress : " + progress + " / " + totalProgress);
                    }

                    @Override
                    public void onUrlCall(String url) {
                        Log.e(TAG, "onUrlCall : " + url);
                    }
                });
            }
        });
        Button button_private = (Button) findViewById(R.id.button_external_private);
        button_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMAWebView webView = (SMAWebView) findViewById(R.id.webview);
                assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_EXTERNAL_PRIVATE);
                webView.loadTemplate("html/", assetManager.getString("html/index.html"), assetManager, new SMAWebViewListener() {
                    @Override
                    public void onUrlLoadProgress(int progress, int totalProgress) {
                        Log.e(TAG, "onUrlLoadProgress : " + progress + " / " + totalProgress);
                    }

                    @Override
                    public void onUrlCall(String url) {
                        Log.e(TAG, "onUrlCall : " + url);
                    }
                });
            }
        });
        Button button_obb = (Button) findViewById(R.id.button_obb);
        button_obb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMAWebView webView = (SMAWebView) findViewById(R.id.webview);
                assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_OBB);
                webView.loadTemplate("html_obb/", assetManager.getString("assets://html/index.html"), assetManager, new SMAWebViewListener() {
                    @Override
                    public void onUrlLoadProgress(int progress, int totalProgress) {
                        Log.e(TAG, "onUrlLoadProgress : " + progress + " / " + totalProgress);
                    }

                    @Override
                    public void onUrlCall(String url) {
                        Log.e(TAG, "onUrlCall : " + url);
                    }
                });
            }
        });



    }
}
