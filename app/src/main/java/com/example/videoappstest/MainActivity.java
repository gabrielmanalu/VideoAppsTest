package com.example.videoappstest;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private Button mButton, start;
    private MediaController mMediaController;
    private SeekBar volume, progress;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private Timer mTimer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoView);
        mButton = findViewById(R.id.button);
        start = findViewById(R.id.button3);
        volume = findViewById(R.id.volumeSeekBar);
        progress = findViewById(R.id.seekBar2);
        mMediaPlayer = MediaPlayer.create(this, R.raw.music);
        mMediaController = new MediaController(MainActivity.this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        volume.setMax(maxVolume);
        volume.setProgress(currentVolume);
        progress.setMax(mMediaPlayer.getDuration());

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                   // Toast.makeText(MainActivity.this, "Volume = " + progress, Toast.LENGTH_SHORT ).show();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.start();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setVideoURI(uri);
                mVideoView.setMediaController(mMediaController);
                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlaying){
                    mMediaPlayer.start();
                    mTimer = new Timer();
                    mTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            progress.setProgress(mMediaPlayer.getCurrentPosition());
                        }
                    }, 0, 1000);
                    isPlaying = true;
                } else{
                    mMediaPlayer.pause();
                    mTimer.cancel();
                    isPlaying = false;
                }

            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mTimer.cancel();
                progress.setProgress(0);
            }
        });



    }
}