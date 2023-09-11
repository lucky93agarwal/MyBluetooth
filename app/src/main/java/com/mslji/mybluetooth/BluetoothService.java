package com.mslji.mybluetooth;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.List;
import java.util.UUID;

public class BluetoothService extends Service {

    public static final String ACTION_DATA_AVAILABLE = "com.microchip.android.btle.example.ACTION_DATA_AVAILABLE";
    public static final String ACTION_DATA_WRITTEN = "com.microchip.android.btle.example.ACTION_DATA_WRITTEN";
    public static final String ACTION_GATT_CONNECTED = "com.microchip.android.btle.example.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.microchip.android.btle.example.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.microchip.android.btle.example.ACTION_GATT_SERVICES_DISCOVERED";
    private static final String CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String EXTRA_DATA = "com.microchip.android.btle.example.EXTRA_DATA";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    /* access modifiers changed from: private */
    public static final String TAG = BluetoothService.class.getSimpleName();
    private static final UUID UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR = UUID.fromString(CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR);
    private static final UUID UUID_SERVER_PRIVATE_CHARACTERISTIC = UUID.fromString(SendActivity.MLDP_DATA_PRIVATE_CHAR);
    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    /* access modifiers changed from: private */
    public BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    /* access modifiers changed from: private */
    public int mConnectionState = 0;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == 2) {
                int unused = BluetoothService.this.mConnectionState = 2;
                BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_GATT_CONNECTED);
//                Log.d("WalletLUckybroadcastUpdateLucky","appendData == 776 ");
                Log.i(BluetoothService.TAG, "Connected to GATT server.");
                Log.i(BluetoothService.TAG, "Attempting to start service discovery: " + mBluetoothGatt.discoverServices());
            } else if (newState == 0) {
                int unused2 = BluetoothService.this.mConnectionState = 0;
                Log.i(BluetoothService.TAG, "Disconnected from GATT server.");
//                Log.d("WalletLUckybroadcastUpdateLucky","appendData == 775 ");
                BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_GATT_DISCONNECTED);
            }
        }
        // 04:91:62:1F:60:47

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
//                Log.d("WalletLUckybroadcastUpdateLucky","appendData == 774 ");
                BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(BluetoothService.TAG, "onServicesDiscovered received: " + status);
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0) {
//                Log.d("WalletLUckybroadcastUpdateLucky","appendData == 773 ");
                BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0) {
//                Log.d("WalletLUckybroadcastUpdateLucky","appendData == 772 ");
                BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_DATA_WRITTEN, characteristic);
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d("WalletLUckybroadcastUpdateLucky","appendData == 771 =="+characteristic.getStringValue(0));
            BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        /* access modifiers changed from: package-private */
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    /* access modifiers changed from: private */
    public void broadcastUpdate(String action) {
        sendBroadcast(new Intent(action));
    }

    /* access modifiers changed from: private */
    public void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        if (UUID_SERVER_PRIVATE_CHARACTERISTIC.equals(characteristic.getUuid())) {
//            Log.d("WalletLUckybroadcastUpdateLucky","appendData == "+characteristic.getStringValue(0));
            intent.putExtra(EXTRA_DATA, characteristic.getStringValue(0));
        }
        sendBroadcast(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(String address) {
        if (this.mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        } else if (this.mBluetoothDeviceAddress == null || !address.equals(this.mBluetoothDeviceAddress) || this.mBluetoothGatt == null) {
            BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
            if (device == null) {
                Log.w(TAG, "Device not found.  Unable to connect.");
                return false;
            }
            this.mBluetoothGatt = device.connectGatt(this, false, this.mGattCallback);
            Log.d(TAG, "Trying to create a new connection.");
            this.mBluetoothDeviceAddress = address;
            this.mConnectionState = 1;
            return true;
        } else {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (!this.mBluetoothGatt.connect()) {
                return false;
            }
            this.mConnectionState = 1;
            return true;
        }
    }

    public void disconnect() {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.disconnect();
        }
    }

    public void close() {
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        int test = characteristic.getProperties();
        if ((test & 8) != 0 || (test & 4) != 0) {
            if (this.mBluetoothGatt.writeCharacteristic(characteristic)) {
                Log.d(TAG, "writeCharacteristic successful");
            } else {
                Log.d(TAG, "writeCharacteristic failed");
            }
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (UUID_SERVER_PRIVATE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            this.mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getServices();
    }
}

