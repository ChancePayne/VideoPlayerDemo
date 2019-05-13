package com.lambdaschool.videoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    VideoView videoView;
    Button controlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlButton = findViewById(R.id.start_stop_button);
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    // stop
                    videoView.stopPlayback();
                    videoView.pause();

                    controlButton.setText(R.string.play);
                } else {
                    // start
                    videoView.start();
                    controlButton.setText(R.string.stop);
                }

            }
        });

        videoView = findViewById(R.id.video_player);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.live_views_of_starman));
        final long startPrep = System.nanoTime();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                videoView.start();
                Log.i(TAG, "Video Prepared in: " + (System.nanoTime() - startPrep) / 1000000);

                controlButton.setText(R.string.play);
                controlButton.setEnabled(true);
            }
        });

    }
}
