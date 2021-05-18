package fr.smartapps.smaassetmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fr.smartapps.lib.SMAAssetManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AUDIO
        Button button1 = (Button) findViewById(R.id.button1);
        if (button1 != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AudioActivity.class);
                    startActivity(intent);
                }
            });
        }

        // IMAGE
        Button button2 = (Button) findViewById(R.id.button2);
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    startActivity(intent);
                }
            });
        }

        // BUTTON
        Button button3 = (Button) findViewById(R.id.button3);
        if (button3 != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ButtonActivity.class);
                    startActivity(intent);
                }
            });
        }

        // DRAWABLE
        Button button4 = (Button) findViewById(R.id.button4);
        if (button4 != null) {
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DrawableActivity.class);
                    startActivity(intent);
                }
            });
        }

        // TEXT
        Button button5 = (Button) findViewById(R.id.button5);
        if (button5 != null) {
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, TextActivity.class);
                    startActivity(intent);
                }
            });
        }

        // TEXT
        Button button6 = (Button) findViewById(R.id.button6);
        if (button6 != null) {
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, WebActivity.class);
                    startActivity(intent);
                }
            });
        }

        // PICASSO
        Button button7 = (Button) findViewById(R.id.button7);
        if (button7 != null) {
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, GlideActivity.class);
                    startActivity(intent);
                }
            });
        }

        initCopy();
    }

    protected void initCopy() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                SMAAssetManager assetManager = new SMAAssetManager(getApplicationContext());
                // drawable activity
                assetManager.copyFile("assets://pikachu.png", "external://pikachu.png");
                assetManager.copyFile("assets://pikachu.png", "external_private://pikachu.png");

                // audio activity
                assetManager.copyFile("assets://random_music.m4a", "external://random_music.m4a");
                assetManager.copyFile("assets://random_music.m4a", "external_private://random_music.m4a");

                // text activity
                assetManager.copyFile("assets://font_external.ttf", "external://font_external.ttf");
                assetManager.copyFile("assets://font_external_private.ttf", "external_private://font_external_private.ttf");

                // copy obb
                assetManager.copyFile("assets://main.1.fr.smartapps.smaassetmanager.obb", "external://Android/obb/fr.smartapps.smaassetmanager/main.1.fr.smartapps.smaassetmanager.obb");

                // copy html directory
                assetManager.copyDirectory("assets://html", "external://html");
                assetManager.copyDirectory("assets://html", "external_private://html");
            }
        };
        task.run();
    }
}
