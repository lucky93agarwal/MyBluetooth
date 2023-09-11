package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mslji.mybluetooth.Database.FirstTableData;
import com.mslji.mybluetooth.Database.MyDbHandler;
import com.mslji.mybluetooth.Database.RefUrl;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.Database.Temp;
import com.mslji.mybluetooth.Model.GetAllData;
import com.mslji.mybluetooth.first.FirstPageActivity;
import com.mslji.mybluetooth.fragment.FormFragment;

import org.json.JSONArray;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class SendActivity extends AppCompatActivity {
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    private static final String MLDP_CONTROL_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a0003ff";
    public static final String MLDP_DATA_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a000301";
    private static final String MLDP_PRIVATE_SERVICE = "00035b03-58e6-07dd-021a-08123a000300";

    public ImageView ivcheckiv;
    public ProgressBar progressBar;

    /* access modifiers changed from: private */
    public static final String TAG = SendActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferenceslucky;
//    public SharedPreferences sharedPreferencesid;
    public SharedPreferences.Editor editnew;
    public BluetoothService mBluetoothLeService;
    private Button mClearIncomingButton;
    private final View.OnClickListener mClearIncomingButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            SendActivity.this.mIncomingText.setText((CharSequence) null);
        }
    };
    String reserver = "";
    private Button mClearOutgoingButton;
    private final View.OnClickListener mClearOutgoingButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            SendActivity.this.mOutgoingText.setText((CharSequence) null);
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
                Log.d(SendActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_CONNECTED");

                boolean unused = SendActivity.this.mConnected = true;
                SendActivity.this.updateConnectionState(R.string.connected);
                SendActivity.this.invalidateOptionsMenu();

            } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(SendActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_DISCONNECTED");

                boolean unused2 = SendActivity.this.mConnected = false;
                SendActivity.this.updateConnectionState(R.string.disconnected);
                SendActivity.this.invalidateOptionsMenu();
                SendActivity.this.clearUI();
            } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(SendActivity.TAG, " BroadcastReceiver.onReceive ACTION_GATT_SERVICES_DISCOVERED");
//                Log.d("WalletLUcky","3");
                SendActivity.this.findMldpGattService(SendActivity.this.mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
//                Log.d(SendActivity.TAG, " BroadcastReceiver.onReceive ACTION_DATA_AVAILABLE");
//                Log.d("WalletLUcky","4");

//                Log.d("WalletLUckyLucky","appendData == "+intent.getStringExtra(BluetoothService.EXTRA_DATA));
                appendData(intent.getStringExtra(BluetoothService.EXTRA_DATA));
            } else if (BluetoothService.ACTION_DATA_WRITTEN.equals(action)) {
                Log.d(SendActivity.TAG, " BroadcastReceiver.onReceive ACTION_DATA_WRITTEN");
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


            if(String.valueOf(before)!=null){
                if (count > before) {
                    Log.d("datahhhhgffcffccf","$   = "+cs.subSequence(start + before, start + count).toString());
                    mDataMDLP.setValue(cs.subSequence(start + before, start + count).toString());
                    mBluetoothLeService.writeCharacteristic(mDataMDLP);
                }
            }

        }

        public void afterTextChanged(Editable edtbl) {
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BluetoothService unused = SendActivity.this.mBluetoothLeService = ((BluetoothService.LocalBinder) service).getService();
            if (!SendActivity.this.mBluetoothLeService.initialize()) {
                Log.e(SendActivity.TAG, "Unable to initialize Bluetooth");
                SendActivity.this.finish();
            }
            SendActivity.this.mBluetoothLeService.connect(SendActivity.this.mDeviceAddress);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            BluetoothService unused = SendActivity.this.mBluetoothLeService = null;
        }
    };
//    public MyDbHandler myDbHandler;

    SessionManager sessionManager;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Intent intent = getIntent();
        sessionManager = new SessionManager(this);
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
//        sharedPreferencesid = getSharedPreferences("gotodata",MODE_PRIVATE);

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
        findViewById(R.id.datanew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendActivity.this, IDActivity.class);

                startActivity(intent);
            }
        });

//        final Handler handlerx = new Handler();
//        handlerx.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                ((EditText) findViewById(R.id.outgoingText)).setText("AT\n");
//
//            }
//        }, 2000);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ((EditText) findViewById(R.id.outgoingText)).setText("ATD\n");
                mDataMDLP.setValue("ATD"+"\n");
                mBluetoothLeService.writeCharacteristic(mDataMDLP);

            }
        }, 2000);




        final Handler handlertwo = new Handler();
        handlertwo.postDelayed(new Runnable() {
            @Override
            public void run() {


                JSONAsyncTask getRequest = new JSONAsyncTask();
                getRequest.execute("");
//                mDataMDLP.setValue("PT:1"+","+"\n");
//                mBluetoothLeService.writeCharacteristic(mDataMDLP);
            }
        }, 18000);




//        final Handler handlersss = new Handler();
//        handlersss.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDataMDLP.setValue("ATDOK\r\n");
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

    }
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                registerReceiver(SendActivity.this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
            }
        });
        thread.start();


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
                SendActivity.this.mConnectionState.setText(resourceId);
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


//    public Handler handlernew;
//    public void getdatainblue(){
//        handlernew = new HandlerMain
//    }
    public void appendData(String data) {

        sharedPreferences =getSharedPreferences("lucky",MODE_PRIVATE);




        if (data != null) {
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            };
//            new Thread(runnable).start();
            mIncomingText.append(data);
            NewData = data;
            getAllDataNew.setData(data);

            Log.d("checknewdatanewlucky",data);
                if(NewData.indexOf('$') > -1){
                    Log.d("checknewdatanewlucky","Run API");

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpGetRequest getRequest = new HttpGetRequest();
                            getRequest.execute("");
                        }
                    });
                    thread.start();



                }




            editnew.putString("datakey", mIncomingText.getText().toString());
            editnew.apply();
            String newnewdata = mIncomingText.getText().toString().trim();

            if(newnewdata.length()>8){

//
                int n = newnewdata.length();
                Log.d("WalletLUckysss","SJAPANData == "+newnewdata.substring(n - 8));
                String newdata = mIncomingText.getText().toString().substring(n - 8);
                Log.d("WalletLUckysss","SJAPANJAPANData == "+newdata);
                Log.d("WalletLUckysssDATA_END","SJAPANJAPANData 2 == "+newdata);
                Log.d("WalletLUckysssDATA_END","SJAPANJAPANData 231 == "+newdata.indexOf("DATA_END"));



                if(internet_connect){
                    if(newdata.indexOf("DATA_END") > -1){
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDataMDLP.setValue("ATDOK\r\n");
                                mBluetoothLeService.writeCharacteristic(mDataMDLP);
//                        ((EditText) findViewById(R.id.outgoingText)).setText("ATDOK\n");
                            }
                        }, 2000);
                    }
                }else{
                    if(newdata.indexOf("RES_END") > -1){

                    }

                }

//                datanewdata(newdata);


            }

//            if(sharedPreferences.getBoolean("check",false)){
//                senddata = senddata + data;
//
//
////                if(senddata.startsWith("ATOKRES_STR")){
//                    Log.d("WalletsenddataLUcky","appendData 1 == "+senddata);
//                    Log.d("WalletsenddataLUcky","appendData 1 == "+senddata);
//
//                    if(senddata.indexOf("RES_END") > -1){
//                       edit =sharedPreferences.edit();
//                        edit.putBoolean("check",false);
//                        edit.apply();
//                        Log.d("WalletsenddataLUcky","appendData 2 == "+senddata);
//                        senddata = senddata.substring(senddata.indexOf("RES_STR") + 9);
//                        progressBar.setVisibility(View.GONE);
//                        ivcheckiv.setVisibility(View.VISIBLE);
//
//                        senddata = senddata.substring(0, senddata.indexOf("RES_END"));
//                        Log.d("sjdfljdslfj","appendData 3 == "+senddata);
//                        Log.d("WalletsendLUckynew","appendData == "+senddata);
//
//                        Log.d("WalletsenddataLUcky","appendData == "+senddata);
//                    }
////                }
//            }else{}


        }
    }

    public void datanewdata(String newdata) {





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
            String stringUrl = RefUrl.BaseUrl;
            Log.d("checknewdatanewlucky","Run API stringUrl ="+stringUrl);
            String result;
            String inputLine="";

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
//
                final JSONObject jsonObject = new JSONObject();
                String Scan_date=mIncomingText.getText().toString();
                try {
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//
                    jsonObject.put("id",sessionManager.getAppId()+"-"+mydate);
                    jsonObject.put("Device_Data",mIncomingText.getText().toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }

//                String Scan_date=mIncomingText.getText().toString();

//                String Scan_date= "Lucky Agarwal";
                Log.d("checknewdatanewlucky","print json = "+String.valueOf(jsonObject));
                Log.d("checknewdatanewlucky","print get alll json = "+String.valueOf(Scan_date));

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                bufferedReader.write(String.valueOf(jsonObject));
//                bufferedReader.write(Scan_date);
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
                Log.d("checknewdatanewlucky","Message =    "+e.getMessage());
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d("checknewdatanewlucky","Result =    "+result);

        }
    }




    public String id;


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/interrupt?DeviceID="+sessionManager.getAppId());
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                Log.d("checknewdatanewlucky","before  status =    "+String.valueOf(status));
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

//                    JSONArray jsonArray = new JSONArray(data);
//                    for(int i=0;i<jsonArray.length();i++){
//
//                    }
                    id = data;
//                    JSONObject jsono = new JSONObject(data);
                    Log.d("checknewdatanewlucky","before  Result =    "+data.toString());
                    return true;
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.d("checknewdatanewlucky","IOException =    "+e.getMessage().toString());
            }
//            catch (JSONException e) {
//
//                e.printStackTrace();
//                Log.i("datahhhhgffcffccf","JSONException =    "+e.getMessage().toString());
//            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            if(result){
                String finavalue = "PT:"+id+",";
//                String finavalue = "PT:01,";
                mDataMDLP.setValue(finavalue+"\n");
                mBluetoothLeService.writeCharacteristic(mDataMDLP);
            }

        }
    }





    ////// id api
//    public class HttpGetRequestApi extends AsyncTask<String, Void, String> {
//        public static final String REQUEST_METHOD = "POST";
//        public static final int READ_TIMEOUT = 15000;
//        public static final int CONNECTION_TIMEOUT = 15000;
//        @Override
//        protected String doInBackground(String... params){
//            String stringUrl = RefUrl.BaseUrlnew;
//            String result;
//            String inputLine=mIncomingText.getText().toString();
//
//            try {
//                //Create a URL object holding our url
//                URL myUrl = new URL(stringUrl);
//                //Create a connection
//                HttpURLConnection connection =(HttpURLConnection)
//                        myUrl.openConnection();
//                //Set methods and timeouts
//                connection.setRequestMethod(REQUEST_METHOD);
//                connection.setReadTimeout(READ_TIMEOUT);
//                connection.setConnectTimeout(CONNECTION_TIMEOUT);
//
//
//                String Scan_date="";
////                String Scan_date= "Lucky Agarwal";
//
//
//                OutputStream outputStream = connection.getOutputStream();
//                BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
//                bufferedReader.write(Scan_date);
//                bufferedReader.flush();
//                bufferedReader.close();
//                outputStream.close();
//
//
//
//                //Connect to our url
//                connection.connect();
//                //Create a new InputStreamReader
//                InputStreamReader streamReader = new
//                        InputStreamReader(connection.getInputStream());
//                //Create a new buffered reader and String Builder
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                //Check if the line we are reading is not null
//                while((inputLine = reader.readLine()) != null){
//                    stringBuilder.append(inputLine);
//                }
//                //Close our InputStream and Buffered reader
//                reader.close();
//                streamReader.close();
//                //Set our result equal to our stringBuilder
//                result = stringBuilder.toString();
//            }
//            catch(IOException e){
//                e.printStackTrace();
//                result = null;
//            }
//            return result;
//        }
//        protected void onPostExecute(String result){
//            super.onPostExecute(result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String id = jsonObject.getString("PT");
//                String finavalue = "PT:"+id+",";
//                mDataMDLP.setValue(finavalue+"\n");
//                mBluetoothLeService.writeCharacteristic(mDataMDLP);
//                Log.d("checknewdatanewlucky","Result id =    "+finavalue);
//            } catch (JSONException e) {
//                e.getMessage();
//            }
//
//
//        }
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SendActivity.this, FirstPageActivity.class);
        startActivity(intent);
        finish();
    }
}