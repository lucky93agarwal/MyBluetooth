package com.mslji.mybluetooth.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mslji.mybluetooth.ListActivity;
import com.mslji.mybluetooth.Popup.loginpopup;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.databinding.ActivitySplashBinding;
import com.mslji.mybluetooth.databinding.FragmentSplashBinding;
import com.mslji.mybluetooth.first.FirstPageActivity;
import com.mslji.mybluetooth.Popup.firstlogo;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class SplashActivity extends AppCompatActivity {


    private static firstlogo addpopup;
    FragmentSplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSplashBinding.inflate(getLayoutInflater());
        addpopup = new firstlogo(SplashActivity.this);
        View view = binding.getRoot();
        setContentView(view);

        binding.getstarttv.setOnClickListener(v -> {
//            Intent intent = new Intent(SplashActivity.this, ListActivity.class);
//            startActivity(intent);
//            finish();
            addpopup.addpopup();
        });




    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        SplashActivity.this.finish();
        System.exit(0);
        ActivityCompat.finishAffinity(this);
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }
}