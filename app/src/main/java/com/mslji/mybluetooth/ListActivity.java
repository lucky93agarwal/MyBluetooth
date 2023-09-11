package com.mslji.mybluetooth;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mslji.mybluetooth.first.FirstPageActivity;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements DeviceListFragment.OnFragmentInteractionListener  {
    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    private DeviceListFragment mDeviceListFragment;
    private BluetoothAdapter BTAdapter;


    public static int REQUEST_BLUETOOTH = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(ListActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private boolean checkAndRequestPermissions() {
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (checkAndRequestPermissions()) {
            // All permission are granted already. Proceed ahead
        }


        if (ContextCompat.checkSelfPermission(ListActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(ListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(ListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        mDeviceListFragment = DeviceListFragment.newInstance(BTAdapter);
        fragmentManager.beginTransaction().replace(R.id.container, mDeviceListFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ListActivity.this, FirstPageActivity.class);
        startActivity(intent);
        finish();
    }
}

