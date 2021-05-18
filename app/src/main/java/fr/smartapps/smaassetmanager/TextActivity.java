package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import fr.smartapps.lib.SMAAssetManager;

public class TextActivity extends AppCompatActivity {

    protected SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        assetManager = new SMAAssetManager(this);
        //assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_ASSETS);
        assetManager.initMainOBB(1, 46548526);

        TextView textView1 = (TextView) findViewById(R.id.text_view1);
        if (textView1 != null) {
            textView1.setTypeface(assetManager.getTypeFace("assets://font_assets.ttf"));
        }

        TextView textView2 = (TextView) findViewById(R.id.text_view2);
        if (textView2 != null) {
            textView2.setTypeface(assetManager.getTypeFace("external://font_external.ttf"));
        }

        TextView textView3 = (TextView) findViewById(R.id.text_view3);
        if (textView3 != null) {
            textView3.setTypeface(assetManager.getTypeFace("external_private://font_external_private.ttf"));
        }

        TextView textView4 = (TextView) findViewById(R.id.text_view4);
        if (textView4 != null) {
            textView4.setTypeface(assetManager.getTypeFace("obb://font_obb.ttf"));
        }

        TextView textView5 = (TextView) findViewById(R.id.text_view5);
        if (textView5 != null) {
            textView5.setTextColor(assetManager.getStateListColor().focused("#abcdef").pressed("#abcdef").inverse("#abcabc"));
        }
    }
}
