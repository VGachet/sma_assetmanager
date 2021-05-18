package fr.smartapps.lib.glide;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

/**
 * Created by vchann on 02/12/2016.
 */

public class SMAStreamLoader implements StreamModelLoader<SMAFile> {

    private final Context context;

    public SMAStreamLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(final SMAFile model, int width, int height) {
        return new SMADataFetcher(context, model);
    }

    public static class Factory implements ModelLoaderFactory<SMAFile, InputStream> {

        @Override
        public ModelLoader<SMAFile, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new SMAStreamLoader(context);
        }

        @Override
        public void teardown() {
            // TODO clear static cache of ZipResourceFiles in OBBDataFetcher
        }
    }
}