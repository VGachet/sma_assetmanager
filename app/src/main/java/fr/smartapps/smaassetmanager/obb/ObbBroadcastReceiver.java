package fr.smartapps.smaassetmanager.obb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

//import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
//import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * The BroadcastReceiver will start the download service if the files need to be downloaded.
 */
public class ObbBroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*try {
            DownloaderClientMarshaller.startDownloadServiceIfRequired(context, intent, DownloaderService.class);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}
