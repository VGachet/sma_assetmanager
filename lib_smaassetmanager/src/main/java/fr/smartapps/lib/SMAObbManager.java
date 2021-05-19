package fr.smartapps.lib;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import fr.smartapps.lib.zip.APKExpansionSupport;
import fr.smartapps.lib.zip.ZipResourceFile;

/**
 * Created by vchann on 12/08/2016.
 */
public class SMAObbManager {

    private String TAG = "SMAObbManager";
    protected int OBB_MAIN_VERSION = 0;
    protected int OBB_PATCH_VERSION = 0;
    protected int OBB_MAIN_FILE_SIZE = 0;
    protected int OBB_PATCH_FILE_SIZE = 0;
    protected Context context;
    static protected ZipResourceFile expansionFile = null;

    public SMAObbManager(Context context) {
        this.context = context;
    }

    public void initMainOBB(int version, int fileSize) {
        OBB_MAIN_VERSION = version;
        OBB_MAIN_FILE_SIZE = fileSize;
    }

    public void initPatchOBB(int version, int fileSize) {
        OBB_PATCH_VERSION = version;
        OBB_PATCH_FILE_SIZE = fileSize;
    }

    public boolean isMainOBBInitialized() {
        if (OBB_MAIN_VERSION == 0 || OBB_MAIN_FILE_SIZE == 0) {
            return false;
        }
        return true;
    }

    public boolean isPatchOBBInitialized() {
        if (OBB_PATCH_VERSION == 0 || OBB_PATCH_FILE_SIZE == 0) {
            return false;
        }
        return true;
    }

    public ZipResourceFile getExpansionFile() {
        if (expansionFile == null) {
            try {
                expansionFile = APKExpansionSupport.getAPKExpansionZipFile(context,
                        OBB_MAIN_VERSION,
                        OBB_MAIN_FILE_SIZE);
                Log.e(TAG, "Expansion File OBB found");
            } catch (IOException e) {
                Log.e(TAG, "Failed to find Expansion File OBB", e);
            }
            return expansionFile;
        }
        else {
            return expansionFile;
        }
    }
}
