package fr.smartapps.lib.glide;

import fr.smartapps.lib.SMAAssetManager;

/**
 * Created by vchann on 02/12/2016.
 */
public class SMAFile {

    public SMAFile(String path, SMAAssetManager assetManager) {
        this.path = path;
        this.assetManager = assetManager;
    }

    final String path;
    final SMAAssetManager assetManager;
}
