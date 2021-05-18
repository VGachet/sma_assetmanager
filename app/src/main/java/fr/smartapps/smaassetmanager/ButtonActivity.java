package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import fr.smartapps.lib.SMAAssetManager;

public class ButtonActivity extends AppCompatActivity {


    protected SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        assetManager = new SMAAssetManager(this);

        /*
        You cannot put transformed drawable in StateListDrawable (ex : drawable.filter() or drawable.alpha() does not work)
         */
        // buttons
        Button buttonView = (Button) findViewById(R.id.button);
        ToggleButton toggleButtonView = (ToggleButton) findViewById(R.id.toggle_button);
        ImageButton imageButtonView = (ImageButton) findViewById(R.id.image_button);

        if (buttonView != null) {
            buttonView.setBackground(assetManager.getStateListDrawable()
                    .focused("assets://tinder_selected.png")
                    .pressed("assets://tinder_selected.png")
                    .inverse("assets://tinder.png"));
        }
        if (toggleButtonView != null) {
            toggleButtonView.setBackground(assetManager.getStateListDrawable()
                    .checked("assets://tinder_selected.png")
                    .inverse("assets://tinder.png"));
        }
        if (imageButtonView != null) {
            imageButtonView.setBackground(assetManager.getStateListDrawable()
                    .focused("assets://tinder_selected.png")
                    .pressed("assets://tinder_selected.png")
                    .inverse("assets://tinder.png"));
        }
    }
}
