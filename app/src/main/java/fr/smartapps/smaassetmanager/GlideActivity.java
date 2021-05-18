package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import fr.smartapps.lib.SMAAssetManager;
import fr.smartapps.lib.glide.SMAFile;

public class GlideActivity extends AppCompatActivity {

    SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        assetManager = new SMAAssetManager(this);
        assetManager.initMainOBB(1, 46548526);

        ImageView imageAssets = (ImageView) findViewById(R.id.image_1);
        Glide.with(this).load(new SMAFile("assets://pikachu.png", assetManager)).into(imageAssets);

        ImageView imageExternal = (ImageView) findViewById(R.id.image_2);
        Glide.with(this).load(new SMAFile("external_private://pikachu.png", assetManager)).into(imageExternal);

        ImageView imageExternalPrivate = (ImageView) findViewById(R.id.image_3);
        Glide.with(this).load(new SMAFile("external://pikachu.png", assetManager)).into(imageExternalPrivate);

        ImageView imageOBB = (ImageView) findViewById(R.id.image_4);
        Glide.with(this).load(new SMAFile("obb://pikachu.png", assetManager)).into(imageOBB);
    }
}
