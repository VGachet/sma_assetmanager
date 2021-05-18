package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import fr.smartapps.lib.SMAAssetManager;

public class DrawableActivity extends AppCompatActivity {

    protected SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        assetManager = new SMAAssetManager(this);
        assetManager.initMainOBB(1, 46548526);
        assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_OBB);

        ImageView imageView4 = (ImageView) findViewById(R.id.image_4);
        ImageView imageView5 = (ImageView) findViewById(R.id.image_5);
        ImageView imageView6 = (ImageView) findViewById(R.id.image_6);

        if (imageView4 != null) {
            imageView4.setBackground(assetManager.getDrawable("pikachu.png"));
        }
        if (imageView5 != null) {
            imageView5.setBackground(assetManager.getDrawable("external_private://pikachu.png"));
        }
        if (imageView6 != null) {
            imageView6.setBackground(assetManager.getDrawable("external://pikachu.png"));
        }
    }
}
