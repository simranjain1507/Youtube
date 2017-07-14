package com.example.android.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    final String DEVELOPER_KEY = "YOUR_KEY_HERE";
    public String log = "";
    String url = "https://www.youtube.com/watch?v=7V6FGF1ffgQ";
    String[] parts = url.split("=");
    String code = parts[1];
    private YouTubePlayerFragment youTubePlayerFragment;
    private MyPlayBackEventListener myPlayBackEventListener;
    private MyPlayerStateChanegListener myPlayerStateChanegListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeview);
        youTubePlayerFragment.initialize(DEVELOPER_KEY, this);
        myPlayBackEventListener = new MyPlayBackEventListener();
        myPlayerStateChanegListener = new MyPlayerStateChanegListener();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(myPlayerStateChanegListener);
        youTubePlayer.setPlaybackEventListener(myPlayBackEventListener);
        if (!b) {
            youTubePlayer.loadVideo(code);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing the youtubePlayer");
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {

            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeview);
    }


    class MyPlayerStateChanegListener implements YouTubePlayer.PlayerStateChangeListener {

        private void updateLog(String prompt) {

            log += "MyPlayback" + "\n" + prompt + "\n";
            Log.e("Log: ", log);
        }

        @Override
        public void onLoading() {
            updateLog("onLoaing()");
        }

        @Override
        public void onLoaded(String s) {
            updateLog("onLoaded()");
        }

        @Override
        public void onAdStarted() {
            updateLog("onAdStarted()");
        }

        @Override
        public void onVideoStarted() {
            updateLog("onVideoStarted()");
        }

        @Override
        public void onVideoEnded() {
            updateLog("onVideoEnded()");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            updateLog("onError(): " + errorReason.toString());
        }
    }

    class MyPlayBackEventListener implements YouTubePlayer.PlaybackEventListener {
        private void updateLog(String prompt) {

            log += "MyPlayback" + "\n" + prompt + "\n";
            Log.e("Log: ", log);
        }

        @Override
        public void onPlaying() {
            updateLog("onPlaying()");
        }

        @Override
        public void onPaused() {
            updateLog("onPaused()");
        }

        @Override
        public void onStopped() {
            updateLog("onStopped()");
        }

        @Override
        public void onBuffering(boolean b) {
            updateLog("onBuffering(): " + String.valueOf(b));
        }

        @Override
        public void onSeekTo(int i) {
            updateLog("onSeekTo(): " + String.valueOf(i));
        }
    }
}
