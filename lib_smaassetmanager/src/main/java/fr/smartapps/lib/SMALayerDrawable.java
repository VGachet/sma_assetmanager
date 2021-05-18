package fr.smartapps.lib;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;

/**
 * Created by vchann on 19/08/2016.
 */
public class SMALayerDrawable {

    protected LayerDrawable layerDrawable;
    protected SMAAssetManager assetManager;
    protected String PROGRESS_COLOR = "#ffffff";
    protected String BACKGROUND_PROGRESS_COLOR = "#aaaaaa";

    public SMALayerDrawable(SMAAssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public SMALayerDrawable progressColor(String color) {
        this.PROGRESS_COLOR = color;
        return this;
    }

    public SMALayerDrawable backgroundProgressColor(String color) {
        this.BACKGROUND_PROGRESS_COLOR = color;
        return this;
    }

    public LayerDrawable getLayer() {
        ClipDrawable clipDrawable = new ClipDrawable(assetManager.getColorDrawable(PROGRESS_COLOR), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        Drawable progressDrawable = assetManager.getColorDrawable(BACKGROUND_PROGRESS_COLOR);
        InsetDrawable insetDrawable = new InsetDrawable(progressDrawable, 5, 20, 5, 20);
        return layerDrawable = new LayerDrawable(new Drawable[]{insetDrawable, clipDrawable});
    }
}
