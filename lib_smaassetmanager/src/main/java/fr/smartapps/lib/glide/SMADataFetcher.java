package fr.smartapps.lib.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;

import fr.smartapps.lib.SMAAssetManager;

/**
 * Created by vchann on 02/12/2016.
 */

class SMADataFetcher implements DataFetcher<InputStream> {

    private final Context context;
    private SMAAssetManager assetManager;
    private final String path;
    private InputStream obbFileStream;

    public SMADataFetcher(Context context, SMAFile model) {
        // explode model fields so that they can't be modified (finals in OBBFile are optional)
        this(context, model.assetManager, model.path);
    }

    public SMADataFetcher(Context context, SMAAssetManager assetManager, String path) {
        this.context = context;
        this.assetManager = assetManager;
        this.path = path;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        return obbFileStream = assetManager.getInputStream(path);
    }

    @Override
    public void cleanup() {
        try {
            if (obbFileStream != null) {
                obbFileStream.close();
            }
        } catch (IOException e) {
            Log.w("OBBDataFetcher", "Cannot clean up after stream", e);
        }
    }

    @Override
    public String getId() {
        return "AssetManager loading @" + path;
    }

    @Override
    public void cancel() {
        // do nothing
    }
}