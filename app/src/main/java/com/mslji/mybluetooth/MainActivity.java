package com.mslji.mybluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public Button btnonbtn,btnoffbtn,btnshowbtn,btnbtnScann,btnbtnenable;
    public BluetoothAdapter myBluetoothAdapter;

    public Intent btnEnablingIntent;
    public int requestCodeForeEnable;

    public ListView lllsitviewll, llscannll;


    public TextView tvscanstatus;

    // Scann
    IntentFilter scanintentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

    public ArrayList<String> stringArrayList = new ArrayList<String>();
    public ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnonbtn = (Button)findViewById(R.id.btnon);
        btnoffbtn = (Button)findViewById(R.id.btnoff);
        btnshowbtn = (Button)findViewById(R.id.btnshow);
        btnbtnScann = (Button)findViewById(R.id.btnScann);


        findViewById(R.id.btscan_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SendActivity.class);
                startActivity(intent);
            }
        });



        tvscanstatus = (TextView)findViewById(R.id.scanstatus);

        btnbtnenable = (Button)findViewById(R.id.btnenable);

        lllsitviewll = (ListView)findViewById(R.id.lsitviewll);


        llscannll = (ListView)findViewById(R.id.scannll);



        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForeEnable = 1;


        bluethoothOnMethod();
        bluethoothOffMethod();
        exeButton();
        exeScannButton();
        exeEnableButton();


        registerReceiver(myReceiver,scanintentFilter);




        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,stringArrayList);
        llscannll.setAdapter(arrayAdapter);
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
                int modeView = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,BluetoothAdapter.ERROR);
                if(modeView==BluetoothAdapter.SCAN_MODE_CONNECTABLE){
                    tvscanstatus.setText("The device is not in discoverable mode but can still receive connection");
                }else if(modeView== BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    tvscanstatus.setText("The device is in discoverable mode");
                }else if(modeView==BluetoothAdapter.SCAN_MODE_NONE){
                    tvscanstatus.setText("The device is not in discoverable mode and can not receive connection");
                }else {
                    tvscanstatus.setText("Error");
                }
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                stringArrayList.add(device.getName());
//                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==requestCodeForeEnable){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "Bluetooth is Enable", Toast.LENGTH_SHORT).show();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth Enabling Cancelled", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void exeEnableButton(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,120);
        startActivity(intent);
    }
    private void exeScannButton(){
        btnbtnScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lllsitviewll.setVisibility(View.GONE);
                llscannll.setVisibility(View.VISIBLE );
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,8);
                startActivity(intent);
                myBluetoothAdapter.startDiscovery();
            }
        });
    }
    private void exeButton(){
        btnshowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lllsitviewll.setVisibility(View.VISIBLE);
                llscannll.setVisibility(View.GONE);
                Set<BluetoothDevice> bt =myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index=0;

                if(bt.size()>0){
                    for(BluetoothDevice device:bt){
                        strings[index]= device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings);
                    lllsitviewll.setAdapter(arrayAdapter);
                }
            }
        });

    }
    private void bluethoothOffMethod(){
        btnoffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isEnabled()){
                    myBluetoothAdapter.disable();
                }
            }
        });
    }
    private void bluethoothOnMethod(){
        btnonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter == null){
                    Toast.makeText(MainActivity.this, "Bluetooth does not support on this Device", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(!myBluetoothAdapter.isEnabled()){
                        startActivityForResult(btnEnablingIntent,requestCodeForeEnable);

                    }
                    
                }
            }
        });
    }
}