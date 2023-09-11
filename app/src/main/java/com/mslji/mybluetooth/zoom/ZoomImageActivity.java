package com.mslji.mybluetooth.zoom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mslji.mybluetooth.R;

public class ZoomImageActivity extends AppCompatActivity {

    public String imageurl;
    public PhotoView photo_views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);


        photo_views = (PhotoView)findViewById(R.id.photo_view);


        imageurl = getIntent().getStringExtra("url");
        Log.i("checkdataknow","zoom image    =     "+imageurl);
        Glide.with(ZoomImageActivity.this).load(imageurl).into(photo_views);
    }
}