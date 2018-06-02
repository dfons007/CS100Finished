package com.CS100MessagingApp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlay extends AppCompatActivity {
    String videoURI = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        Intent intent = getIntent();
        videoURI = intent.getStringExtra("videoURI");
        if (videoURI.isEmpty()){
            Toast.makeText(VideoPlay.this,"False to get VideoURI",Toast.LENGTH_LONG).show();
            startActivity(new Intent(VideoPlay.this, Chat.class));
        }else {
            Toast.makeText(VideoPlay.this,"Get VideoURI Successfully",Toast.LENGTH_SHORT).show();
        }

        Uri uri = Uri.parse(videoURI);
        VideoView videoView = (VideoView)this.findViewById(R.id.videoView);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
        });
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener( new MyVideoOnCompletionListener());
        videoView.setVideoURI(uri);
        videoView.start();
    }

    class MyVideoOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( VideoPlay.this, "Play to the END", Toast.LENGTH_SHORT).show();
        }
    }
}
