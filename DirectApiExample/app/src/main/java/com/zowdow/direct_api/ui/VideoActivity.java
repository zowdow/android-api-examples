package com.zowdow.direct_api.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zowdow.direct_api.R;

/**
 * Activity with a single youtube-video player.
 */
public class VideoActivity extends AppCompatActivity {
    public static final String EXTRA_VIDEO = "extra-video";

    private WebView videoWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        setupViews();

        String videoUrl = handleIntent();
        videoWebView.loadData(createData(videoUrl), "text/html", "UTF-8");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoWebView.destroy();
    }

    private void setupViews() {
        videoWebView = (WebView) findViewById(R.id.wvVideo);
        videoWebView.setBackgroundColor(Color.BLACK);
        WebSettings settings = videoWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    private String handleIntent() {
        Intent intent = getIntent();
        return intent.getStringExtra(EXTRA_VIDEO);
    }

    private String createData(String videoUrl) {
        return "<iframe src=\"" + videoUrl + "\" width=\"100%\" height=\"90%\" frameborder=\"0\" allowfullscreen></iframe>";
    }
}
