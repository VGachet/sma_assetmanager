package fr.smartapps.smaassetmanager.obb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.google.android.vending.expansion.downloader.impl.DownloaderService;

import java.util.Set;

/**
 * Abstract activity to extends if you wanna check for OBB.
 * <p>
 * If the OBB exist
 * => log "Expansion files delivered"
 * else
 * => log "Try download expansion file"
 * => process start to download OBB
 */
public abstract class ObbDownloadActivity extends Activity implements IDownloaderClient {

    private String TAG = "ObbDownloadActivity";

    // obb
    static int obb_size = 0;
    static int obb_version = 2;

    protected static final int DIALOG_DOWNLOAD_EXPANSION_FILES = 102;
    protected static final int DIALOG_DOWNLOAD_EXPANSION_FILES_ERROR = 103;
    protected static final String DIALOG_ARG_DOWNLOADER_STATE = "downloader_state";

    ProgressDialog mDownloadExpansionFilesDialog = null;
    private IStub mExpansionFilesDownloaderClientStub;
    private IDownloaderService mExpansionFilesRemoteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        // OBB config
        obb_size = getResources().getInteger(R.integer.config_expansion_file_main_size);
        obb_version = getResources().getInteger(R.integer.config_expansion_file_main_version);

        // Check the presence of expansion files
        if (!expansionFilesDelivered()) {
            Log.d(TAG, "Try download Expansion files");
            tryDownloadExpansionFiles();
        }
        else {
            Log.d(TAG, "Expansion files delivered");
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mExpansionFilesDownloaderClientStub) {
            mExpansionFilesDownloaderClientStub.connect(this);
        }

/*        if (null != mDownloadExpansionFilesDialog && mDownloadExpansionFilesDialog.isShowing() && expansionFilesDelivered()) {
            dismissDialog(DIALOG_DOWNLOAD_EXPANSION_FILES);
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mExpansionFilesDownloaderClientStub) {
            mExpansionFilesDownloaderClientStub.disconnect(this);
        }
    }

    @Override
    public void onServiceConnected(Messenger m) {
        mExpansionFilesRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
        mExpansionFilesRemoteService.onClientUpdated(mExpansionFilesDownloaderClientStub.getMessenger());
    }

    @Override
    public void onDownloadStateChanged(int newState) {
        Bundle args;
        switch (newState) {

            case IDownloaderClient.STATE_COMPLETED:
                if (null != mDownloadExpansionFilesDialog) {
                    dismissDialog(DIALOG_DOWNLOAD_EXPANSION_FILES);
                    mDownloadExpansionFilesDialog = null;
                }
                return;
            case IDownloaderClient.STATE_PAUSED_NETWORK_UNAVAILABLE:
            case IDownloaderClient.STATE_PAUSED_BY_REQUEST:
            case IDownloaderClient.STATE_PAUSED_WIFI_DISABLED_NEED_CELLULAR_PERMISSION:
            case IDownloaderClient.STATE_PAUSED_NEED_CELLULAR_PERMISSION:
            case IDownloaderClient.STATE_PAUSED_ROAMING:
            case IDownloaderClient.STATE_PAUSED_NETWORK_SETUP_FAILURE:
            case IDownloaderClient.STATE_PAUSED_SDCARD_UNAVAILABLE:
            case IDownloaderClient.STATE_FAILED_UNLICENSED:
            case IDownloaderClient.STATE_FAILED_FETCHING_URL:
            case IDownloaderClient.STATE_FAILED_SDCARD_FULL:
            case IDownloaderClient.STATE_FAILED_CANCELED:
            case IDownloaderClient.STATE_FAILED:
                args = new Bundle();
                args.putInt(DIALOG_ARG_DOWNLOADER_STATE, newState);
                showDialog(DIALOG_DOWNLOAD_EXPANSION_FILES_ERROR, args);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDownloadProgress(DownloadProgressInfo progress) {
        if (null != mDownloadExpansionFilesDialog) {
            mDownloadExpansionFilesDialog.setMax((int) (progress.mOverallTotal >> 8));
            mDownloadExpansionFilesDialog.setProgress((int) (progress.mOverallProgress >> 8));
        }

    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Dialog dialog;
        AlertDialog.Builder builder;
        switch (id) {
            case DIALOG_DOWNLOAD_EXPANSION_FILES:
                mDownloadExpansionFilesDialog = new ProgressDialog(this);
                mDownloadExpansionFilesDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mDownloadExpansionFilesDialog.setMessage("Loading");
                mDownloadExpansionFilesDialog.setCancelable(false);
                dialog = mDownloadExpansionFilesDialog;
                break;
            default:
                dialog = super.onCreateDialog(id, args);
        }
        return dialog;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPrepareDialog(int id, final Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DIALOG_DOWNLOAD_EXPANSION_FILES:
                mDownloadExpansionFilesDialog.setProgressNumberFormat("");
                mDownloadExpansionFilesDialog.setProgress(0);
                break;
        }
    }

/*    boolean expansionFilesDelivered() {
        String mainFilename = getResources().getBoolean(R.bool.config_expansion_file_needs_main) ? Helpers.getExpansionAPKFileName(this, true, getResources().getInteger(R.integer.config_expansion_file_main_version)) : null;
        String patchFilename = getResources().getBoolean(R.bool.config_expansion_file_needs_patch) ? Helpers.getExpansionAPKFileName(this, false, getResources().getInteger(R.integer.config_expansion_file_patch_version)) : null;

        if (null != mainFilename && !Helpers.doesFileExist(this, mainFilename, getResources().getInteger(R.integer.config_expansion_file_main_size), false)) {
            return false;
        }

        if (null != patchFilename && !Helpers.doesFileExist(this, patchFilename, getResources().getInteger(R.integer.config_expansion_file_patch_size), false)) {
            return false;
        }
        return true;
    }*/

    private void tryDownloadExpansionFiles() {

        Intent launchIntent = getIntent();
        Intent intentToLaunchThisActivityFromNotification;

        // If we have been launched in any other fashion than with an action
        if (Intent.ACTION_MAIN.equals(launchIntent.getAction()) || null == launchIntent.getAction() || 0 == launchIntent.getAction().length()) {
            intentToLaunchThisActivityFromNotification = new Intent(this, getClass());
        }
        else {
            intentToLaunchThisActivityFromNotification = new Intent(launchIntent.getAction());
        }
        intentToLaunchThisActivityFromNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        forwardCategories(intentToLaunchThisActivityFromNotification);

        // Build PendingIntent used to open this activity from Notification
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentToLaunchThisActivityFromNotification, PendingIntent.FLAG_UPDATE_CURRENT);

        // Request to start the download
        int startResult;
        try {
            startResult = DownloaderService.startDownloadServiceIfRequired(this, pendingIntent, getPackageName(), ObbDownloadService.class.getName());
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot find our own package", e);
            finish();
            return;
        }

        if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
            if (null == mDownloadExpansionFilesDialog || !mDownloadExpansionFilesDialog.isShowing())

            {
                showDialog(DIALOG_DOWNLOAD_EXPANSION_FILES);
            }
            mExpansionFilesDownloaderClientStub = DownloaderClientMarshaller.CreateStub(this, ObbDownloadService.class);
        }
        else {
            Log.d(TAG, "No expansion file download required");
        }

    }

    protected void forwardCategories(Intent intent) {
        Set<String> categories = getIntent().getCategories();
        if (null != categories) {
            for (String category : categories) {
                intent.addCategory(category);
            }
        }
    }

}
