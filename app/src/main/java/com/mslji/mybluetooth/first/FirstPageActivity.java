package com.mslji.mybluetooth.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mslji.mybluetooth.DashBoardActivity;
import com.mslji.mybluetooth.Database.FirstTableData;
import com.mslji.mybluetooth.Database.MyDbHandler;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.Database.Temp;
import com.mslji.mybluetooth.ListActivity;
import com.mslji.mybluetooth.Model.GetAllData;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.SendActivity;
import com.mslji.mybluetooth.SendNewDataActivity;
import com.mslji.mybluetooth.SplashScreen.SplashActivity;
import com.mslji.mybluetooth.TestingActivity;
import com.mslji.mybluetooth.databinding.ActivityFirstPageBinding;
import com.mslji.mybluetooth.history.HistoryActivity;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FirstPageActivity extends AppCompatActivity {

    public AppCompatButton btnone,btntwo,btnsenddata;
    public MyDbHandler myDbHandler;
    ActivityFirstPageBinding binding;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        sessionManager = new SessionManager(this);

//        try {
//            SharedPreferences sharedPreferenceslucky = getSharedPreferences("datanewlucky",MODE_PRIVATE);
////            Gson gson = new Gson();
//            String datanewnew = sharedPreferenceslucky.getString("datakey","777");
////            Type type = new TypeToken<List<String>>() {
////            }.getType();
////            List<String> arrPackageData = gson.fromJson(datanewnew, type);
//            Log.d("llloijhgfxcggfcv","row data row = = "+datanewnew);
//            Log.d("llloijhgfxcggfcv","row Size = = "+datanewnew.length());
//        }catch (Exception e){
//            e.printStackTrace();
//        }





        sessionManager.setCheck(true);
//        SharedPreferences sharedPreferences =getSharedPreferences("lucky",MODE_PRIVATE);
//        SharedPreferences.Editor edit =sharedPreferences.edit();
//        edit.putBoolean("check",true);
//        edit.apply();


        binding.signouttv.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPageActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        });

        binding.showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, TestingActivity.class);
                startActivity(intent);
            }
        });




//        myDbHandler = new MyDbHandler(getApplicationContext(),"userbd",null,1);
//
//        Temp.setMyDbHandler(myDbHandler);
//        ArrayList<GetAllData> arrayList = myDbHandler.viewAllDataUser();
//        if(arrayList.size() >0){
//            int size = arrayList.size() -1;
//            Log.d("llloijhgfxcggfcv","row data row = = "+arrayList.size());
//            Log.d("llloijhgfxcggfcv","row data row data = = "+arrayList.get(size).getData());
//        }


//        Login login = new Login();
//        login.execute();




        binding.btnonell.setOnClickListener(v -> {
            sessionManager.setGotodata("send");
//            Intent intent = new Intent(this, SendActivity.class);
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, sessionManager.getDAdd());
//            intent.putExtra("id",sessionManager.getDName());
//            startActivity(intent);

            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
//            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//
//            startActivity(intent);
        });
        binding.btnonetv.setOnClickListener(v -> {
            sessionManager.setGotodata("send");
//            Intent intent = new Intent(this, SendActivity.class);
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, sessionManager.getDAdd());
//            intent.putExtra("id",sessionManager.getDName());
//            startActivity(intent);

            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
            startActivity(intent);
            finish();

//            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//
//            startActivity(intent);
        });
        binding.btnonetwotv.setOnClickListener(v -> {
            sessionManager.setGotodata("send");
//            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//
//            startActivity(intent);
//            Intent intent = new Intent(this, SendActivity.class);
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, sessionManager.getDAdd());
//            intent.putExtra("id",sessionManager.getDName());
//            startActivity(intent);


            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        });

//        binding.btnonebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sessionManager.setGotodata("sendtwo");
//                Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//
//                startActivity(intent);
//            }
//        });

        binding.onell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });
        binding.onetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });
        binding.onetwotv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });

        binding.twoll.setOnClickListener(v -> {
            sessionManager.setGotodata("sendtwo");

//            Intent intent = new Intent(this, SendNewDataActivity.class);
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, sessionManager.getDAdd());
//            intent.putExtra("id", sessionManager.getAppId());
//
//            startActivity(intent);


            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
            startActivity(intent);
            finish();



//            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//            startActivity(intent);
        });

        binding.twotv.setOnClickListener(v -> {
            sessionManager.setGotodata("sendtwo");
//            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//            startActivity(intent);
//            Intent intent = new Intent(this, SendNewDataActivity.class);
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, sessionManager.getDAdd());
//            intent.putExtra("id", sessionManager.getAppId());
//
//            startActivity(intent);

            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        });

        binding.twotwotv.setOnClickListener(v -> {
            sessionManager.setGotodata("sendtwo");
//            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
//            startActivity(intent);
//            Intent intent = new Intent(this, SendNewDataActivity.class);
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
//            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, sessionManager.getDAdd());
//            intent.putExtra("id", sessionManager.getAppId());
//
//            startActivity(intent);



            Intent intent = new Intent(FirstPageActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        });






        binding.historyll.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPageActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        binding.historytv.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPageActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        binding.historytwotv.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPageActivity.this, HistoryActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FirstPageActivity.this, FirstPageActivity.class);
        startActivity(intent);
        finish();
    }
}