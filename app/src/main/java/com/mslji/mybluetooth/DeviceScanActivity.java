package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.first.FirstPageActivity;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import kotlinx.coroutines.channels.Send;

public class DeviceScanActivity extends ListActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = DeviceScanActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public LeDeviceListAdapter mLeDeviceListAdapter;

//    SharedPreferences sharedPref;
//    SharedPreferences.Editor edit;
    public String nh = "2501";

    SessionManager sessionManager;
    /* access modifiers changed from: private */


    public String code;

    public final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            DeviceScanActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("WalletWallet","Check 1=="+device.getName());
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public boolean mScanning;
    public Button btnSubmit;
    public EditText etpassword;
    public String getData;
    public String nameD;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager =new SessionManager(this);
        code = getIntent().getStringExtra(SendActivity.EXTRAS_DEVICE_ADDRESS);
        nameD = getIntent().getStringExtra(SendActivity.EXTRAS_DEVICE_NAME);

//        sharedPref = getSharedPreferences("gotodata",0);
//        edit = sharedPref.edit();
        sessionManager.setDName("Bio");
        sessionManager.setAppId(nameD);
        sessionManager.setDAdd(code);

//        edit.putString("id",nameD);
//        edit.apply();
        getData = sessionManager.getGotodata();

        Log.d("Checkdevicedata","getData 2  =     "+getData);


        mHandler = new Handler() {
            @Override
            public void publish(LogRecord record) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        if (!getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
        }
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }


        if(getData.equalsIgnoreCase("sendtwo")){
            Intent intent = new Intent(this, SendNewDataActivity.class);
            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, code);
            intent.putExtra("id",nameD);
            if (this.mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                this.mScanning = false;
            }
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, SendActivity.class);
            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, code);
            intent.putExtra("id",nameD);
            if (this.mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                this.mScanning = false;
            }
            startActivity(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d("WalletWallet","Check 2 ==");
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        this.mLeDeviceListAdapter = new LeDeviceListAdapter();
        Log.d("WalletWallet","Check 3 ==");
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }





    private void setListAdapter(LeDeviceListAdapter mLeDeviceListAdapter) {
    }


    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        this.mLeDeviceListAdapter.clear();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!this.mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                this.mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        BluetoothDevice device = this.mLeDeviceListAdapter.getDevice(position);
        if (device != null) {
            Intent intent = new Intent(this, SendActivity.class);

            intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
            intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, "68:27:19:3C:7F:3D");
            if (this.mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                this.mScanning = false;
            }
            startActivity(intent);
        }

    }

    private void scanLeDevice(boolean enable) {
        if (enable) {

            Log.d("WalletWallet","Check 4 ==");
                    boolean unused = DeviceScanActivity.this.mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.d("WalletWallet","Check 5 ==");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    private class LeDeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();

        public LeDeviceListAdapter() {
            this.mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!this.mLeDevices.contains(device)) {
                this.mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return this.mLeDevices.get(position);
        }

        public void clear() {
            this.mLeDevices.clear();
        }

        public int getCount() {
            return this.mLeDevices.size();
        }

        public Object getItem(int i) {
            return this.mLeDevices.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflator.inflate(R.layout.listitem_device, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = this.mLeDevices.get(i);
            String deviceName =
                    device.getName();
            if (deviceName == null || deviceName.length() <= 0) {
                viewHolder.deviceName.setText(R.string.unknown_device);
            } else {
                viewHolder.deviceName.setText(deviceName);
            }
            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;

        ViewHolder() {
        }
    }
}