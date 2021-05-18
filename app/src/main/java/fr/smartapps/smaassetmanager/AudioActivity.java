package fr.smartapps.smaassetmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import fr.smartapps.lib.SMAAssetManager;
import fr.smartapps.lib.audio.SMAAudioPlayer;
import fr.smartapps.lib.audio.SMAAudioPlayerListener;
import fr.smartapps.lib.SMADrawable;

public class AudioActivity extends AppCompatActivity {

    public SMAAssetManager assetManager;
    public SMAAudioPlayer audioPlayer1;
    public SMAAudioPlayer audioPlayer2;
    public SMAAudioPlayer audioPlayer3;
    public SMAAudioPlayer audioPlayer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        assetManager = new SMAAssetManager(this);
        assetManager.initMainOBB(1, 46548526);

        audioPlayer1 = initAudioPlayerFromAssets(R.id.seek_bar1, R.id.toggle_button1, "assets://random_music.m4a");
        audioPlayer2 = initAudioPlayerFromAssets(R.id.seek_bar2, R.id.toggle_button2, "external://random_music.m4a");
        audioPlayer3 = initAudioPlayerFromAssets(R.id.seek_bar3, R.id.toggle_button3, "external_private://random_music.m4a");
        audioPlayer4 = initAudioPlayerFromAssets(R.id.seek_bar4, R.id.toggle_button4, "obb://random_music.m4a");
    }

    protected SMAAudioPlayer initAudioPlayerFromAssets(int resourceSeekBar, int resourceToggleButton, String url) {
        // audio player
        final AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(resourceSeekBar);
        final SMAAudioPlayer audioPlayer = assetManager.getAudioPlayer(url, this.getApplicationContext(), new SMAAudioPlayerListener() {
            @Override
            public void onSongProgress(int progress, int totalProgress) {
                if (seekBar != null) {
                    seekBar.setMax(totalProgress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onSongFinish(int totalProgress) {

            }
        });

        // seek bar
        if (seekBar != null) {
            seekBar.setProgressDrawable(assetManager.getLayerDrawable().backgroundProgressColor("#cccccc").progressColor("#abcdef").getLayer());
            SMADrawable thumb = assetManager.getDrawable("assets://scrubber_control.png").filter("#abcdef").density(5);
            seekBar.setThumb(thumb);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    audioPlayer.seekTo(seekBar.getProgress());
                }
            });
        }

        // toggle button play / pause
        ToggleButton toggleButton = (ToggleButton) findViewById(resourceToggleButton);
        if (toggleButton != null) {
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        audioPlayer.start();
                    } else {
                        audioPlayer.pause();
                    }
                }
            });
        }

        return audioPlayer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer1.stop();
        audioPlayer1.release();

        audioPlayer2.stop();
        audioPlayer2.release();

        audioPlayer3.stop();
        audioPlayer3.release();

        audioPlayer4.stop();
        audioPlayer4.release();
    }
}
