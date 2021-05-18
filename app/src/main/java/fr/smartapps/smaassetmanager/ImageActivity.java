package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import fr.smartapps.lib.SMAAssetManager;

public class ImageActivity extends AppCompatActivity {

    protected SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        assetManager = new SMAAssetManager(this);

        // icons custom
        ImageView imageView1 = (ImageView) findViewById(R.id.image_1);
        ImageView imageView2 = (ImageView) findViewById(R.id.image_2);
        ImageView imageView3 = (ImageView) findViewById(R.id.image_3);

        if (imageView1 != null) {
            imageView1.setBackground(assetManager.getDrawable("assets://tinder.png"));
        }
        if (imageView2 != null) {
            imageView2.setBackground(assetManager.getDrawable("assets://tinder.png").filter("#000000"));
        }
        if (imageView3 != null) {
            imageView3.setBackground(assetManager.getDrawable("assets://tinder.png").alpha(100));
        }
    }
}
