package fr.smartapps.lib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import fr.smartapps.lib.audio.SMAAudioPlayer;
import fr.smartapps.lib.audio.SMAAudioPlayerListener;

/**
 *
 */
public class SMAAssetManager {

    /*
    Attributes
     */
    private String TAG = "SMAAssetManager";
    protected Context context;
    protected SMAObbManager obbManager;

    static public final int STORAGE_TYPE_UNDEFINED = 0;
    static public final int STORAGE_TYPE_ASSETS = 1;
    static public final int STORAGE_TYPE_EXTERNAL = 2;
    static public final int STORAGE_TYPE_EXTERNAL_PRIVATE = 3;
    static public final int STORAGE_TYPE_OBB = 4;
    static public final int STORAGE_TYPE_NANCY_ASSETS = 5;
    protected int STORAGE_DEFAULT_VALUE = 1;
    protected String EXTENSION_BASE_DIRECTORY = "";

    final static public String STRATEGY_SUFFIX_ASSETS = "assets://";
    final static public String STRATEGY_SUFFIX_EXTERNAL = "external://";
    final static public String STRATEGY_SUFFIX_EXTERNAL_PRIVATE = "external_private://";
    final static public String STRATEGY_SUFFIX_OBB = "obb://";

    /*
    Constructor
     */
    public SMAAssetManager(Context context) {
        this.context = context;
        obbManager = new SMAObbManager(context);
    }

    /*
    Public basics methods
     */
    public int getStorageType(String url) {
        if (url == null)
            return STORAGE_TYPE_UNDEFINED;

        if (url.startsWith(STRATEGY_SUFFIX_ASSETS))
            return STORAGE_TYPE_ASSETS;

        if (url.startsWith(STRATEGY_SUFFIX_EXTERNAL))
            return STORAGE_TYPE_EXTERNAL;

        if (url.startsWith(STRATEGY_SUFFIX_EXTERNAL_PRIVATE))
            return STORAGE_TYPE_EXTERNAL_PRIVATE;

        if (url.startsWith(STRATEGY_SUFFIX_OBB))
            return STORAGE_TYPE_OBB;

        return STORAGE_TYPE_UNDEFINED;
    }

    public String getExternalPublicStorageSuffix() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    public String getExternalPrivateStorageSuffix() {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.applicationInfo.dataDir + "/";
        }
        catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public String getAssetsSuffix() {
        return "file:///android_asset/";
    }

    public String getObbSuffix() {
        return "";
    }

    public String getAbsoluteUrl(String url) {
        switch (getStorageType(url)) {

            case STORAGE_TYPE_ASSETS:
                url = url.replace(STRATEGY_SUFFIX_ASSETS, "");
                return getAssetsSuffix() + url;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL, "");
                return getExternalPublicStorageSuffix() + url;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL_PRIVATE, "");
                return getExternalPrivateStorageSuffix() + url;

            case STORAGE_TYPE_OBB:
                url = url.replace(STRATEGY_SUFFIX_OBB, "");
                return getObbSuffix() + url;

            default:
                return url;
        }
    }

    /*
    Extension directory
     */
    public void setExtensionDirectory(String extension) {
        this.EXTENSION_BASE_DIRECTORY = extension;
    }

    public String getExtensionDirectory() {
        if (EXTENSION_BASE_DIRECTORY == null || EXTENSION_BASE_DIRECTORY.equals("")) {
            return "";
        }
        else if (EXTENSION_BASE_DIRECTORY.endsWith("/")) {
            return EXTENSION_BASE_DIRECTORY;
        }
        else {
            return EXTENSION_BASE_DIRECTORY + "/";
        }
    }

    /*
    Public practical methods
     */
    public SMADrawable getDrawable(String url) {
        return new SMADrawable(getInputStream(url));
    }

    public Drawable getColorDrawable(String color) {
        if (color == null || !color.contains("#")) {
            return null;
        }
        return new ColorDrawable(Color.parseColor(color));
    }

    public String getString(String url) {
        try {
            InputStream inputStream = getInputStream(url);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } catch (IOException e) {
            Log.e(TAG, "error : " + e.getMessage());
        }
        return null;

    }

    public SMAStateListDrawable getStateListDrawable() {
        return new SMAStateListDrawable(this);
    }

    public SMAStateListColor getStateListColor() {
        return new SMAStateListColor();
    }

    public SMALayerDrawable getLayerDrawable() {
        return new SMALayerDrawable(this);
    }

    public SMAAudioPlayer getAudioPlayer(String url, Context context, SMAAudioPlayerListener audioPlayerListener) {
        return new SMAAudioPlayer(context, this, url, audioPlayerListener);
    }

    /*
    Public practical basics methods
     */
    public Typeface getTypeFace(String url) {
        switch (getStorageType(url)) {
            case STORAGE_TYPE_ASSETS:
                url = url.replace(STRATEGY_SUFFIX_ASSETS, "");
                try {
                    return Typeface.createFromAsset(context.getAssets(), url);
                } catch (Exception e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL, "");
                File file = new File(getExternalPublicStorageSuffix() + url);
                if (file.exists()) {
                    try {
                        return Typeface.createFromFile(file);
                    } catch (Exception e) {
                        Log.e(TAG, "Fail to get " + url + " from external public storage");
                    }
                }
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL_PRIVATE, "");
                File privateFile = new File(getExternalPrivateStorageSuffix() + url);
                if (privateFile.exists()) {
                    try {
                        return Typeface.createFromFile(privateFile);
                    } catch (Exception e) {
                        Log.e(TAG, "Fail to get " + url + " from external private storage");
                    }
                }
                break;

            case STORAGE_TYPE_OBB:
                // work around for Typeface from OBB ...
                InputStream inputStream = getInputStream(url);
                Typeface typeFace = null;

                url = url.replace(STRATEGY_SUFFIX_OBB, "");
                String outPath = getExternalPrivateStorageSuffix() + "/" + url;

                try
                {
                    byte[] buffer = new byte[inputStream.available()];
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

                    int l = 0;
                    while((l = inputStream.read(buffer)) > 0)
                        bos.write(buffer, 0, l);

                    bos.close();

                    typeFace = Typeface.createFromFile(outPath);

                    // clean up
                    new File(outPath).delete();
                    return typeFace;
                }
                catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from OBB storage");
                }
                break;

            default:
                /*url = url.replace(STRATEGY_SUFFIX_ASSETS, "");
                try {
                    return Typeface.createFromAsset(context.getAssets(), url);
                } catch (Exception e) {
                    Log.e(TAG, "Fail to get " + url + " from default assets");
                }*/
                switch (STORAGE_DEFAULT_VALUE) {
                    case STORAGE_TYPE_ASSETS:
                        return getTypeFace(STRATEGY_SUFFIX_ASSETS + getExtensionDirectory() + url);
                    case STORAGE_TYPE_OBB:
                        return getTypeFace(STRATEGY_SUFFIX_OBB + getExtensionDirectory() + url);
                    case STORAGE_TYPE_EXTERNAL:
                        return getTypeFace(STRATEGY_SUFFIX_EXTERNAL + getExtensionDirectory() + url);
                    case STORAGE_TYPE_EXTERNAL_PRIVATE:
                        return getTypeFace(STRATEGY_SUFFIX_EXTERNAL_PRIVATE + getExtensionDirectory() + url);

                }
                break;
        }
        return null;
    }

    public AssetFileDescriptor getAssetFileDescriptor(String url) {
        if (url == null || url.equals(""))
            return null;

        switch (getStorageType(url)) {

            case STORAGE_TYPE_ASSETS:
                try {
                    url = url.replace(STRATEGY_SUFFIX_ASSETS, "");
                    return context.getAssets().openFd(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL, "");
                File file = new File(getExternalPublicStorageSuffix() + url);
                if (file.exists())
                    try {
                        Uri path = Uri.fromFile(file);
                        return context.getContentResolver().openAssetFileDescriptor(path, "rw");
                    } catch (FileNotFoundException e) {
                        Log.e("AssetManager", "Fail to get " + url + " from external public storage");
                    }
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL_PRIVATE, "");
                File privateFile = new File(getExternalPrivateStorageSuffix() + url);
                if (privateFile.exists())
                    try {
                        Uri path = Uri.fromFile(privateFile);
                        return context.getContentResolver().openAssetFileDescriptor(path, "rw");
                    } catch (FileNotFoundException e) {
                        Log.e("AssetManager", "Fail to get " + url + " from external private storage");
                    }
                break;

            case STORAGE_TYPE_OBB:
                url = url.replace(STRATEGY_SUFFIX_OBB, "");
                if (obbManager != null && obbManager.getExpansionFile() != null) {
                    AssetFileDescriptor result = obbManager.getExpansionFile().getAssetFileDescriptor(url);
                    if (result == null) {
                        Log.e(TAG, "Fail to get assetFileDescriptor at " + url + " from OBB");
                    }
                    return result;
                }
                else {
                    Log.e(TAG, "ExpansionFile hasn't been initialized");
                }
                break;

            default:
                /*try {
                    return context.getAssets().openFd(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from default assets");
                }*/
                switch (STORAGE_DEFAULT_VALUE) {
                    case STORAGE_TYPE_ASSETS:
                        return getAssetFileDescriptor(STRATEGY_SUFFIX_ASSETS + getExtensionDirectory() + url);
                    case STORAGE_TYPE_OBB:
                        return getAssetFileDescriptor(STRATEGY_SUFFIX_OBB + getExtensionDirectory() + url);
                    case STORAGE_TYPE_EXTERNAL:
                        return getAssetFileDescriptor(STRATEGY_SUFFIX_EXTERNAL + getExtensionDirectory() + url);
                    case STORAGE_TYPE_EXTERNAL_PRIVATE:
                        return getAssetFileDescriptor(STRATEGY_SUFFIX_EXTERNAL_PRIVATE + getExtensionDirectory() + url);
                }
                break;
        }

        return null;
    }

    public InputStream getInputStream(String url) {
        if (url == null) {
            return null;
        }

        switch (getStorageType(url)) {

            case STORAGE_TYPE_ASSETS:
                url = url.replace(STRATEGY_SUFFIX_ASSETS, "");
                try {
                    return context.getAssets().open(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL, "");
                File file = new File(getExternalPublicStorageSuffix() + url);
                if (file.exists()) {
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Fail to get " + url + " from external public storage");
                    }
                }
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL_PRIVATE, "");
                File privateFile = new File(getExternalPrivateStorageSuffix() + url);
                if (privateFile.exists()) {
                    try {
                        return new FileInputStream(privateFile);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Fail to get " + url + " from external private storage");
                    }
                }
                Log.e(TAG, "The file : " + url + " does not exists in external private storage");
                //Look for drawable in assets (STORAGE_TYPE_ASSETS)
                String[] assetUrl = url.split("/");
                url = assetUrl[assetUrl.length-1];
                try {
                    return context.getAssets().open(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_OBB:
                url = url.replace(STRATEGY_SUFFIX_OBB, "");
                if (obbManager != null && obbManager.getExpansionFile() != null) {
                    try {
                        InputStream result = obbManager.getExpansionFile().getInputStream(url);
                        if (result == null) {
                            Log.e(TAG, "Fail to get " + url + " from OBB storage");
                        }
                        return result;
                    } catch (IOException e) {
                        Log.e(TAG, "Fail to get " + url + " from OBB storage");
                    }
                }
                else {
                    Log.e(TAG, "ExpansionFile hasn't been initialized - cannot get : " + url);
                }
                break;

            default:
                switch (STORAGE_DEFAULT_VALUE) {
                    case STORAGE_TYPE_ASSETS:
                        return getInputStream(STRATEGY_SUFFIX_ASSETS + getExtensionDirectory() + url);
                    case STORAGE_TYPE_OBB:
                        return getInputStream(STRATEGY_SUFFIX_OBB + getExtensionDirectory() + url);
                    case STORAGE_TYPE_EXTERNAL:
                        return getInputStream(STRATEGY_SUFFIX_EXTERNAL + getExtensionDirectory() + url);
                    case STORAGE_TYPE_EXTERNAL_PRIVATE:
                        return getInputStream(STRATEGY_SUFFIX_EXTERNAL_PRIVATE + getExtensionDirectory() + url);
                }
                break;
        }
        return null;
    }

    /*
    Public tools methods
    private method == not finished
     */
    private boolean doesFileExist(String url) {
        if (url == null) {
            return false;
        }

        switch (getStorageType(url)) {
            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL, "");
                File externalPublicFile = new File(getExternalPublicStorageSuffix() + url);
                Log.e(TAG, externalPublicFile.getAbsolutePath() + " exist " + externalPublicFile.exists());
                return externalPublicFile.exists();

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(STRATEGY_SUFFIX_EXTERNAL_PRIVATE, "");
                File externalPrivateFile = new File(getExternalPrivateStorageSuffix() + url);
                Log.e(TAG, externalPrivateFile.getAbsolutePath() + " exist " + externalPrivateFile.exists());
                return externalPrivateFile.exists();

            case STORAGE_TYPE_ASSETS:
                url = url.replace(STRATEGY_SUFFIX_ASSETS, "");
                try {
                    return Arrays.asList(context.getResources().getAssets().list("")).contains(url);
                } catch (IOException e) {
                    return false;
                }

            case STORAGE_TYPE_OBB:
                url = url.replace(STRATEGY_SUFFIX_OBB, "");
                // ??? TODO Finish
                return true;

            default:
                return true;
        }
    }

    public void initMainOBB(int mainVersion, int mainSize) {
        obbManager.initMainOBB(mainVersion, mainSize);
    }

    public void initPatchOBB(int patchVersion, int patchSize) {
        obbManager.initPatchOBB(patchVersion, patchSize);
    }

    public void setDefaultStorageType(int storageType) {
        STORAGE_DEFAULT_VALUE = storageType;
    }

    public int getDefaultStorageType() {
        return STORAGE_DEFAULT_VALUE;
    }

    /*
    File Tools : copy file TODO delete file/folder - copy folder
     */
    public void copyFile(String urlSource, String urlDestination) {
        switch (getStorageType(urlDestination)) {
            case STORAGE_TYPE_ASSETS:
                Log.e(TAG, "cannot copy in assets : " + urlDestination);
                return;
            case STORAGE_TYPE_OBB:
                Log.e(TAG, "cannot copy in obb : " + urlDestination);
                return;
        }

        InputStream inputStream = getInputStream(urlSource);
        if (inputStream == null) {
            Log.e(TAG, "Cannot find " + urlSource);
            return;
        }
        else {
            Log.e(TAG, "Copy from " + urlSource);
        }
        copyStreamToFile(inputStream, new File(getAbsoluteUrl(urlDestination)));
    }

    public boolean copyStreamToFile(InputStream inputStream, File outputFile) {
        try {
            FileUtils.copyInputStreamToFile(inputStream, outputFile);
            Log.e(TAG, "copy to path : " + outputFile.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    TODO to test
     */
    public void copyDirectory(String directorySource, String directoryDestination) {
        switch (getStorageType(directoryDestination)) {
            case STORAGE_TYPE_ASSETS:
                Log.e(TAG, "cannot copy in assets : " + directoryDestination);
                return;

            case STORAGE_TYPE_OBB:
                Log.e(TAG, "cannot copy in obb : " + directoryDestination);
                return;
        }

        switch (getStorageType(directorySource)) {
            case STORAGE_TYPE_ASSETS:
                copyFromDirectoryAssets(getAbsoluteUrl(directorySource), getAbsoluteUrl(directoryDestination));
                return;

            case STORAGE_TYPE_EXTERNAL:
                copyDirectory(new File(getAbsoluteUrl(directorySource)), new File(getAbsoluteUrl(directoryDestination)));
                return;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                copyDirectory(new File(getAbsoluteUrl(directorySource)), new File(getAbsoluteUrl(directoryDestination)));
                return;
        }
        return;
    }

    protected void copyFromDirectoryAssets(String directorySource, String directoryDestination) {
        try {
            directoryDestination = getAbsoluteUrl(directoryDestination);
            directorySource = directorySource.replace(getAssetsSuffix(), "");
            AssetManager assetManager = context.getAssets();
            String[] files = assetManager.list(directorySource);

            for (String filename : files) {
                // folder
                if (assetManager.list(filename) != null && assetManager.list(filename).length != 0) {
                    File outputFolder = new File(directoryDestination);
                    outputFolder.mkdirs();
                    copyFromDirectoryAssets((directorySource.equals("")) ? filename : directorySource + "/" + filename, directoryDestination);
                }

                // file
                else {
                    copyFile((directorySource.equals("")) ? filename : directorySource + "/" + filename, directoryDestination + "/" + filename);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean copyDirectory(File inputFile, File outputFile) {
        try {
            FileUtils.copyDirectory(inputFile, outputFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
