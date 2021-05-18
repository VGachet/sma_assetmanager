package fr.smartapps.lib.webview;

/**
 *
 */
public interface SMAWebViewListener {

    public void onUrlLoadProgress(int progress, int totalProgress);

    public void onUrlCall(String url);
}
