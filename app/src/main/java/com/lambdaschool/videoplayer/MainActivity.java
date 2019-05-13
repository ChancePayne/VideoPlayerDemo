package com.lambdaschool.videoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    VideoView videoView;
    Button controlButton;
    SeekBar videoSeekBar;
    Runnable progressListenerRunnable;

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
                    videoView.pause();
                    controlButton.setText(R.string.play);
                } else {
                    // start
                    videoView.start();
                    controlButton.setText(R.string.stop);
                    new Thread(progressListenerRunnable).start();
                }

            }
        });

        videoView = findViewById(R.id.video_player);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pexels_videos_4631));
        final long startPrep = System.nanoTime();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                videoView.start();
                Log.i(TAG, "Video Prepared in: " + (System.nanoTime() - startPrep) / 1000000);

                videoSeekBar.setMax(mp.getDuration());

                controlButton.setText(R.string.play);
                controlButton.setEnabled(true);
            }
        });

        videoSeekBar = findViewById(R.id.video_seekbar);
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*if(fromUser) {
                    videoView.seekTo(progress);
                }*/

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                videoView.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());
                videoView.start();
                new Thread(progressListenerRunnable).start();
            }
        });

        progressListenerRunnable = new Runnable() {
            @Override
            public void run() {
                while (videoView.isPlaying()) {
                    final int currentPosition = videoView.getCurrentPosition();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoSeekBar.setProgress(currentPosition);
                        }
                    });
                    try {
                        Thread.sleep(videoView.getDuration() / videoView.getWidth());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


    }
}
