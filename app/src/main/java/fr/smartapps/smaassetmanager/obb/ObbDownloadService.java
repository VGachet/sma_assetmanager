package fr.smartapps.smaassetmanager.obb;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * The DownloaderService handles the downloading of expansion files from the Play Store and informs about the the progress to subscribing activities.
 * We will take a look at how to configure the activity to begin downloads in a moment.
 *
 * You must update the BASE64_PUBLIC_KEY value to be the public key belonging to your publisher account.
 */
public class ObbDownloadService extends DownloaderService {

    String PUBLIC_KEY = "";

    // You must use the public key belonging to your publisher account
    // You should also modify this salt
    public static final byte[] SALT = new byte[] { 127, -41, 97, 28, -83, -66,
            -76, 1, 45, 72, -109, -78, 12, 49, 34, -20, -115, 107, -58, -122,
            -23, 115, -37, 19, -52, -126, -126, 4, 99, 57, };

    @Override
    public String getPublicKey() {
        return PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return ObbBroadcastReceiver.class.getName();
    }
}
