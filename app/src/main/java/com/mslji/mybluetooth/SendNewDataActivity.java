package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mslji.mybluetooth.Database.RefUrl;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.Model.GetAllData;
import com.mslji.mybluetooth.first.FirstPageActivity;
import com.mslji.mybluetooth.history.HistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SendNewDataActivity extends AppCompatActivity {
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    private static final String MLDP_CONTROL_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a0003ff";
    public static final String MLDP_DATA_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a000301";
    private static final String MLDP_PRIVATE_SERVICE = "00035b03-58e6-07dd-021a-08123a000300";
    public SharedPreferences sharedPreferencesid;
    public ImageView ivcheckiv;
    public ProgressBar progressBar;

    /* access modifiers changed from: private */
    public static final String TAG = SendNewDataActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferenceslucky;
    public SharedPreferences.Editor editnew;
    public BluetoothService mBluetoothLeService;
    private Button mClearIncomingButton;
    private final View.OnClickListener mClearIncomingButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            SendNewDataActivity.this.mIncomingText.setText((CharSequence) null);
        }
    };
    String reserver = "";
    private Button mClearOutgoingButton;
    private final View.OnClickListener mClearOutgoingButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            SendNewDataActivity.this.mOutgoingText.setText((CharSequence) null);
        }
    };
    /* access modifiers changed from: private */
    public boolean mConnected = false;
    /* access modifiers changed from: private */
    public TextView mConnectionState;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic mDataMDLP;
    /* access modifiers changed from: private */
    public String mDeviceAddress;
    private String mDeviceName;

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(SendNewDataActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_CONNECTED");

                boolean unused = SendNewDataActivity.this.mConnected = true;
                SendNewDataActivity.this.updateConnectionState(R.string.connected);
                SendNewDataActivity.this.invalidateOptionsMenu();

            } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(SendNewDataActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_DISCONNECTED");

                boolean unused2 = SendNewDataActivity.this.mConnected = false;
                SendNewDataActivity.this.updateConnectionState(R.string.disconnected);
                SendNewDataActivity.this.invalidateOptionsMenu();
                SendNewDataActivity.this.clearUI();
            } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(SendNewDataActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_SERVICES_DISCOVERED");
//                Log.d("WalletLUcky","3");
                SendNewDataActivity.this.findMldpGattService(SendNewDataActivity.this.mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(SendNewDataActivity.TAG, " BroadcastReceiver.onReceive ACTION_DATA_AVAILABLE");
//                Log.d("WalletLUcky","4");

                Log.d("WalletLUckyLucky","appendData == "+intent.getStringExtra(BluetoothService.EXTRA_DATA));
                appendData(intent.getStringExtra(BluetoothService.EXTRA_DATA));
            } else if (BluetoothService.ACTION_DATA_WRITTEN.equals(action)) {
                Log.d(SendNewDataActivity.TAG, " BroadcastReceiver.onReceive ACTION_DATA_WRITTEN");
//                Log.d("WalletLUcky","5");
            }
        }
    };



    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:

                    break;
                case STATE_CONNECTING:

                    break;
                case STATE_CONNECTED:

                    break;
                case STATE_CONNECTION_FAILED:

                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
//                    msg_box.setText(tempMsg);
                    mIncomingText.append(tempMsg);
                    break;
            }
            return true;
        }
    });
    /* access modifiers changed from: private */
    public TextView mIncomingText;
    /* access modifiers changed from: private */
    public EditText mOutgoingText;
    private final TextWatcher mOutgoingTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            if (count > before) {
//
                Log.d("WalletLuckyYUY","cs = "+cs.subSequence(start + before, start + count).toString());
//                mDataMDLP.setValue(cs.subSequence(start + before, start + count).toString());
//                mBluetoothLeService.writeCharacteristic(mDataMDLP);
            }
        }

        public void afterTextChanged(Editable edtbl) {
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BluetoothService unused = SendNewDataActivity.this.mBluetoothLeService = ((BluetoothService.LocalBinder) service).getService();
            if (!SendNewDataActivity.this.mBluetoothLeService.initialize()) {
                Log.e(SendNewDataActivity.TAG, "Unable to initialize Bluetooth");
                SendNewDataActivity.this.finish();
            }
            SendNewDataActivity.this.mBluetoothLeService.connect(SendNewDataActivity.this.mDeviceAddress);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            BluetoothService unused = SendNewDataActivity.this.mBluetoothLeService = null;
        }
    };
//    public MyDbHandler myDbHandler;

    public AppCompatButton historyButton;
    SessionManager sessionManager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_data);
        Intent intent = getIntent();
        sessionManager = new SessionManager(this);
        sharedPreferencesid = getSharedPreferences("gotodata",MODE_PRIVATE);
//        myDbHandler = new MyDbHandler(getApplicationContext(),"userbd",null,1);
//
//        Temp.setMyDbHandler(myDbHandler);
//
//        myDbHandler.deleteAlldata();

        this.mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        this.mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        ((TextView) findViewById(R.id.deviceAddress)).setText(this.mDeviceName);
        mConnectionState = (TextView) findViewById(R.id.connectionState);
        mIncomingText = (TextView) findViewById(R.id.incomingText);
        mIncomingText.setMovementMethod(new ScrollingMovementMethod());
        mOutgoingText = (EditText) findViewById(R.id.outgoingText);
        mOutgoingText.setMovementMethod(new ScrollingMovementMethod());
        mOutgoingText.addTextChangedListener(this.mOutgoingTextWatcher);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        ivcheckiv = (ImageView) findViewById(R.id.checkiv);
        ivcheckiv.setVisibility(View.GONE);
//        mOutgoingText.setText("AT");
        mClearOutgoingButton = (Button) findViewById(R.id.clearOutgoingButton);
        mClearOutgoingButton.setOnClickListener(this.mClearOutgoingButtonListener);
        mClearIncomingButton = (Button) findViewById(R.id.clearIncomingButton);
        mClearIncomingButton.setOnClickListener(this.mClearIncomingButtonListener);
//        getActionBar().setTitle(this.mDeviceName);
//        getActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.atbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(internet_connect){
                            ((EditText) findViewById(R.id.outgoingText)).setText("ATD\n");
                        }else{
                            ((EditText) findViewById(R.id.outgoingText)).setText("ATR\n");
                        }

                    }
                }, 2000);
            }
        });
        findViewById(R.id.atdn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ((EditText) findViewById(R.id.outgoingText)).setText("AT\n");
                    }
                }, 2000);
            }
        });
        sharedPreferenceslucky = getSharedPreferences("datanewlucky",MODE_PRIVATE);
        editnew = sharedPreferenceslucky.edit();
        findViewById(R.id.atokdn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        ((EditText) findViewById(R.id.outgoingText)).setText("ATDOK\r\n");
                    }
                }, 2000);
            }
        });
        bindService(new Intent(this, BluetoothService.class), this.mServiceConnection, newIntent);
        findViewById(R.id.historybtnnew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SendNewDataActivity.this, HistoryActivity.class);
                startActivity(intent1);
            }
        });
        findViewById(R.id.datanew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WalletLuckyYUY","between ="+String.valueOf(senddata));
                if(senddata.length() !=0){

                    Log.d("WalletLuckyYUY","between ="+String.valueOf(senddata));
                    String[] text_data = senddata.split("\n");
                    for(int i=0;i<text_data.length;i++){
                        Log.d("WalletLuckyYUY"," LUcky ="+text_data[i].toString());


                        text_id  =text_data[i].split(",");

                        Log.d("WalletsenddataLUcky","between new id new 78 = =  "+itemsid.toString());
                        for(int j=0;j<text_id.length;j=j+10){

                            if(text_id[j].length() !=0){
                                String datanewdata = text_id[j];

                                if(datanewdata.length() >=3){
                                    itemsid.add(text_id[j]);
//                                    user.setPasentid(text_id[j]);

                                    if(text_id.length >=j+2){
                                        items_num.add(text_id[j+2]);
//                                        user.setPasentnum(text_id[j+2]);
                                    }
                                    if(text_id.length >=j+3){
                                        items_date.add(text_id[j+1]);
//                                        user.setPasentdate(text_id[j+1]);
//                                        user.setAlldata("");
                                    }


//                                    int h = myDbHandler.insertUser(user);
//                                    Log.d("WalletLuckyYUYnew", "inseart check = " + h);
                                    Log.d("WalletsenddataLUcky","between new id new 786 = =  "+itemsid.toString());
                                }



                            }
//                            items_num.add(text_id[j+2]);
//
//                            items_date.add(text_id[j+1]);





                        }
//                        for(int j=2;j<text_id.length;j=j+9){
//                            items_num.add(text_id[j]);
////                        for(int a=0;a<text_id.length;a=a+10){
////
//                            Log.d("WalletLuckyYUY","between new num new 786 = =  "+items_num.toString());
////                        }
//
//
//                        }
//                        for(int j=1;j<text_id.length;j=j+9){
//
//                            items_date.add(text_id[j]);
////                        for(int a=0;a<text_id.length;a=a+10){
////
//                            Log.d("WalletLuckyYUY","between new data new 786 = =  "+items_date.toString());
////                        }
//
//
//                        }



                        if(i == text_data.length -1){
                            for(int m=0;m<3;m++){
//                                FirstTableData user = new FirstTableData();
//                                user.setPasentid(itemsid.get(i));
//                                user.setPasentnum(items_num.get(i));
//                                user.setPasentdate(items_date.get(i));
//                                user.setAlldata("");
//                                int h = myDbHandler.insertUser(user);
//                                Log.d("WalletLuckyYUYnew", "inseart check = " + h);
                            }
                            Log.d("WalletsenddataLUcky","between new new new 786 size = =  "+itemsid.toString());
                            Log.d("WalletsenddataLUcky","between new new new 786 num = =  "+items_num.toString());
                            Log.d("WalletsenddataLUcky","between new new new 786 date = =  "+items_date.toString());

                            Intent intent = new Intent(SendNewDataActivity.this, IDActivity.class);
                            intent.putStringArrayListExtra("id",itemsid);
                            intent.putStringArrayListExtra("num",items_num);
                            intent.putStringArrayListExtra("date",items_date);

                            startActivity(intent);
                        }
                    }
                }
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ((EditText) findViewById(R.id.outgoingText)).setText("AT\n");

            }
        }, 2000);



//
//        final Handler handlersss = new Handler();
//        handlersss.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDataMDLP.setValue("AT\r\n");
//                mBluetoothLeService.writeCharacteristic(mDataMDLP);
//
//                progressBar.setVisibility(View.GONE);
//                ivcheckiv.setVisibility(View.VISIBLE);
////                        ((EditText) findViewById(R.id.outgoingText)).setText("ATDOK\n");
//            }
//        }, 10000);

        if (isConnected()) {
            internet_connect = true;
            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            internet_connect = false;
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }




        sharedPreferenceslucky = getSharedPreferences("datanewlucky",MODE_PRIVATE);
        editnew = sharedPreferenceslucky.edit();
        etone = (EditText)findViewById(R.id.onevalueet);
        ettwo = (EditText)findViewById(R.id.twovalueet);
        etthree = (EditText)findViewById(R.id.threevalueet);
        etfour = (EditText)findViewById(R.id.fourvalueet);
        bindService(new Intent(this, BluetoothService.class), this.mServiceConnection, newIntent);


        btnsendlucky = (TextView)findViewById(R.id.sendbtnnew);

        etone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }


            }
        });
        ettwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }

            }
        });
        etthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }

            }
        });
        etfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


                if(isNumeric(s.toString())){

                }else {
                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnsendlucky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etone.getText().toString().length() ==0){
                    Toast.makeText(SendNewDataActivity.this, "Please enter value in first input field", Toast.LENGTH_SHORT).show();
                }else if(ettwo.getText().toString().length() ==0){
                    Toast.makeText(SendNewDataActivity.this, "Please enter value in second input field", Toast.LENGTH_SHORT).show();
                }else if(etthree.getText().toString().length() ==0){
                    Toast.makeText(SendNewDataActivity.this, "Please enter value in third input field", Toast.LENGTH_SHORT).show();
                }else if(etfour.getText().toString().length() ==0){
                    Toast.makeText(SendNewDataActivity.this, "Please enter value in four input field", Toast.LENGTH_SHORT).show();
                }else{
                    if(isNumeric(etone.getText().toString())){
                        Log.d("datahhdfdhhgffcffccf","one =  "+etone.getText().toString());
                        if(isNumeric(ettwo.getText().toString())){
                            Log.d("datahhdfdhhgffcffccf","two =  "+ettwo.getText().toString());
                            if(isNumeric(etthree.getText().toString())){

                                if(isNumeric(etfour.getText().toString()))
                                {
                                    Log.d("datahhdfdhhgffcffccf","three =  "+etthree.getText().toString());
                                    String oneet = etone.getText().toString()+",";
                                    String twoet = ettwo.getText().toString()+",";
                                    String threeet = etthree.getText().toString()+",";
                                    String fouret = etfour.getText().toString();
                                    String finavalue = "TH:"+oneet+twoet+threeet+fouret;
                                    Log.d("checknewdatanewlucky","appenddata send  =      "+finavalue);
                                    mDataMDLP.setValue(finavalue+"\n");
                                    mBluetoothLeService.writeCharacteristic(mDataMDLP);

                                    btnsendlucky.setBackgroundResource(R.drawable.green);
                                    btnsendlucky.setEnabled(false);
                                    etone.setEnabled(false);
                                    etthree.setEnabled(false);
                                    ettwo.setEnabled(false);
                                    etfour.setEnabled(false);

                                    HttpGetRequest getRequest = new HttpGetRequest();
                                    getRequest.execute("");
                                }else {
                                    Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                                }


                            }else {
                                Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SendNewDataActivity.this, "Please Enter only Numeric value", Toast.LENGTH_SHORT).show();
                    }

//                    ((EditText) findViewById(R.id.outgoingText)).setText(finavalue+"\n\n");
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            ((EditText) findViewById(R.id.outgoingText)).setText("\n\n");
//
//                        }
//                    }, 2000);



//                    mDataMDLP.setValue("TH:"+oneet+twoet+threeet);
//                    mBluetoothLeService.writeCharacteristic(mDataMDLP);
                }
            }
        });

    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public EditText etone,ettwo,etthree, etfour;
    public TextView btnsendlucky;
    boolean internet_connect = false;
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
    public int newIntent = 1;

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());

        if (this.mBluetoothLeService != null) {
            Log.d(TAG, "Connect request result = " + this.mBluetoothLeService.connect(this.mDeviceAddress));
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        editnew.putString("data",mIncomingText.getText().toString());
        editnew.apply();
        unregisterReceiver(this.mGattUpdateReceiver);

    }




    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        editnew.putString("data",mIncomingText.getText().toString());
        editnew.apply();
        unbindService(this.mServiceConnection);
        this.mBluetoothLeService = null;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (this.mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            case R.id.menu_connect /*2131230730*/:
                this.mBluetoothLeService.connect(this.mDeviceAddress);
                return true;
            case R.id.menu_disconnect /*2131230731*/:
                this.mBluetoothLeService.disconnect();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothService.ACTION_DATA_WRITTEN);
        return intentFilter;
    }

    /* access modifiers changed from: private */
    public void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            public void run() {
                SendNewDataActivity.this.mConnectionState.setText(resourceId);
            }
        });
    }

    /* access modifiers changed from: private */
    public void clearUI() {
        this.mIncomingText.setText((CharSequence) null);
        this.mOutgoingText.setText((CharSequence) null);
    }

    public String NewData;
    String senddata="";
    boolean databoolnew = true;
    /* access modifiers changed from: DATAEND private */
    public ArrayList<String> itemsid= new ArrayList<String>();
    public ArrayList<String> items_num= new ArrayList<String>();
    public ArrayList<String> items_date= new ArrayList<String>();
    public ArrayList<String> row_date= new ArrayList<String>();
    public String[] text_id;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public Set<String> set = new HashSet<String>();



    public GetAllData getAllDataNew = new GetAllData();


    public void appendData(String data) {

        sharedPreferences =getSharedPreferences("lucky",MODE_PRIVATE);


        Log.d("checknewdatanewlucky","appenddata  =      "+data);


        if (data != null) {
            mIncomingText.append(data);
            String newdatadata = mIncomingText.getText().toString();
            if(newdatadata.length() >6){
                String lastFourDigits = newdatadata.substring(newdatadata.length() - 6);
                if(lastFourDigits.equalsIgnoreCase("TH_ACK")){

                    Toast toast= Toast.makeText(getApplicationContext(),
                            "Sent Successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();



                    btnsendlucky.setEnabled(true);
                    etone.setEnabled(true);
                    etthree.setEnabled(true);
                    ettwo.setEnabled(true);
                    etfour.setEnabled(true);
              //      Toast.makeText(mBluetoothLeService, "Sent Successfully", Toast.LENGTH_LONG).setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0).show();
                }
            }


//            if(data.equalsIgnoreCase("TH_ACK")){
//                Toast.makeText(mBluetoothLeService, "Sent Successfully", Toast.LENGTH_SHORT).show();
//            }
//            NewData = data;
//            getAllDataNew.setData(data);
//
//
//            if(data.indexOf("$") > -1){
////                    Login login = new Login();
////                    login.execute();
////                    APIget();
//
//                Log.d("sdfsfsfsfsf","Run API");
//
//                HttpGetRequest getRequest = new HttpGetRequest();
//                getRequest.execute("");
//            }
//
//
//
//
//            editnew.putString("datakey", mIncomingText.getText().toString());
//            editnew.apply();
//            String newnewdata = mIncomingText.getText().toString().trim();
//
//            if(newnewdata.length()>8){
//
//
//                int n = newnewdata.length();
//                Log.d("WalletLUckysss","SJAPANData == "+newnewdata.substring(n - 8));
//                String newdata = mIncomingText.getText().toString().substring(n - 8);
//                Log.d("WalletLUckysss","SJAPANJAPANData == "+newdata);
//                Log.d("WalletLUckysssDATA_END","SJAPANJAPANData 2 == "+newdata);
//                Log.d("WalletLUckysssDATA_END","SJAPANJAPANData 231 == "+newdata.indexOf("DATA_END"));
//
//
//
//                if(internet_connect){
//                    if(newdata.indexOf("DATA_END") > -1){
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mDataMDLP.setValue("ATDOK\r\n");
//                                mBluetoothLeService.writeCharacteristic(mDataMDLP);
////                        ((EditText) findViewById(R.id.outgoingText)).setText("ATDOK\n");
//                            }
//                        }, 2000);
//                    }
//                }else{
//                    if(newdata.indexOf("RES_END") > -1){
//
//                    }
//
//                }
//
////                datanewdata(newdata);
//
//
//            }
//
//            if(sharedPreferences.getBoolean("check",false)){
//                senddata = senddata + data;
//
//
////                if(senddata.startsWith("ATOKRES_STR")){
//                Log.d("WalletsenddataLUcky","appendData 1 == "+senddata);
//                Log.d("WalletsenddataLUcky","appendData 1 == "+senddata);
//
//                if(senddata.indexOf("RES_END") > -1){
//                    edit =sharedPreferences.edit();
//                    edit.putBoolean("check",false);
//                    edit.apply();
//                    Log.d("WalletsenddataLUcky","appendData 2 == "+senddata);
//                    senddata = senddata.substring(senddata.indexOf("RES_STR") + 9);
//                    progressBar.setVisibility(View.GONE);
//                    ivcheckiv.setVisibility(View.VISIBLE);
//
//                    senddata = senddata.substring(0, senddata.indexOf("RES_END"));
//                    Log.d("sjdfljdslfj","appendData 3 == "+senddata);
//                    Log.d("WalletsendLUckynew","appendData == "+senddata);
//
//                    Log.d("WalletsenddataLUcky","appendData == "+senddata);
//                }
////                }
//            }else{}


        }
    }

    private void datanewdata(String newdata) {





        if(newdata.equals("DATA_END")){
            Log.d("WalletLUckysssDATA_END","SJAPANJAPANData 2 == "+newdata);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDataMDLP.setValue("ATDOK\r\n");
                    mBluetoothLeService.writeCharacteristic(mDataMDLP);
                }
            }, 2000);

        }
    }

    public String data = "ATOKRES_START\n" +
            "101,21-NOV-2020,12344321,M1,M2,M3,M4,M6,M7,M8\n" +
            "102,21-NOV-2020,12344321,M1,M2,M3,M4,M6,M7,M8\n" +
            "103,21-NOV-2020,12344321,M1,M2,M3,M4,M6,M7,M8\n" +
            "104,21-NOV-2020,12344321,M1,M2,M3,M4,M6,M7,M8\n" +
            "RES_END";
    /* access modifiers changed from: private */
    public void findMldpGattService(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            Log.d(TAG, "findMldpGattService found no Services");
            return;
        }
        this.mDataMDLP = null;
        Iterator intss = gattServices.iterator();
        while (true) {
            if (!intss.hasNext()) {
                break;
            }
            BluetoothGattService gattService = (BluetoothGattService) intss.next();
            if (gattService.getUuid().toString().equals(MLDP_PRIVATE_SERVICE)) {
                for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
                    String uuid = gattCharacteristic.getUuid().toString();
                    if (uuid.equals(MLDP_DATA_PRIVATE_CHAR)) {
                        this.mDataMDLP = gattCharacteristic;
                        int characteristicProperties = gattCharacteristic.getProperties();



                        if ((characteristicProperties & 48) > 0) {
                            Log.d(TAG, "findMldpGattService PROPERTY_NOTIFY | PROPERTY_INDICATE");
                            this.mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        }
                        if ((characteristicProperties & 12) > 0) {
                            Log.d(TAG, "findMldpGattService PROPERTY_WRITE | PROPERTY_WRITE_NO_RESPONSE");
                        }
                        Log.d(TAG, "findMldpGattService found MLDP service and characteristics");
                    } else if (uuid.equals(MLDP_CONTROL_PRIVATE_CHAR)) {
                    }
                }
            }
        }
        if (this.mDataMDLP == null) {
            Toast.makeText(this, R.string.mldp_not_supported, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "findMldpGattService found no MLDP service");
            finish();
        }
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = RefUrl.BaseUrl_for_send_data;
            String result;
            String inputLine="mIncomingText.getText().toString()";
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                final JSONObject jsonObject = new JSONObject();
                try {
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//                    String Scan_date=mIncomingText.getText().toString();
                    jsonObject.put("id",sessionManager.getAppId()+"-"+mydate);
                    jsonObject.put("TH_1",etone.getText().toString());
                    jsonObject.put("TH_2",ettwo.getText().toString());
                    jsonObject.put("TH_3",etthree.getText().toString());
                    jsonObject.put("TH_4",etfour.getText().toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                String Scan_date=mIncomingText.getText().toString();

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("","Lucky");
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                bufferedReader.write(String.valueOf(jsonObject));
                bufferedReader.flush();
                bufferedReader.close();
                outputStream.close();
                //Connect to our url
                connection.connect();



                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d("datahhdfdhhgffcffccf",result);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SendNewDataActivity.this, FirstPageActivity.class);
        startActivity(intent);
        finish();
    }
}



