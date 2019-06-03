package com.example.blelibrary.blelibrary.Server;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.blelibrary.blelibrary.EventBus.SerchDevice;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ScanDevice {
    private BluetoothAdapter mBluetoothAdapter;
    private Activity activity;
    public ArrayList<BluetoothDevice> mLeDevices= new ArrayList<BluetoothDevice>();
    public BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    if(!mLeDevices.contains(device)){
                        EventBus.getDefault().post(new SerchDevice(device));
                        }
                            StringBuilder stringBuilder=new StringBuilder();
                            for(byte a:scanRecord){stringBuilder.append(String.format("%02X",a));}
                            Log.d("scanrecord",stringBuilder.toString());
                            try{
                                Log.d("name",device.getName());
                            } catch (Exception e){  mLeDevices.add(device);
                                Log.d("name","UNROWN");}
                }
            };
    public void setmBluetoothAdapter(Activity setting){
        this.activity=setting;
        initPermission(setting);
        BluetoothManager bluetoothManager=(BluetoothManager) setting.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(setting, "notsupport", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    //-----------------------method1檢查權限------------------------------------------
    public void initPermission(Activity context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                 RequestPermission();
            }
        } else {
             RequestPermission();
        }
    }
    public void RequestPermission(){
         mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!isLocServiceEnable(activity)){
            Toast.makeText(activity,"請打開定位功能",Toast.LENGTH_SHORT).show();
        }
        boolean originalBluetooth = (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
        if (originalBluetooth) {
            scanLeDevice(true);
            mBluetoothAdapter.startDiscovery();
        } else if (originalBluetooth == false) {
            scanLeDevice(true);
            mBluetoothAdapter.enable();
        }
    }
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    //----------method2開始掃描
    public void scanLeDevice( boolean enable) {
        if (enable) {
            if(!EventBus.getDefault().isRegistered(activity)){EventBus.getDefault().register(activity);}
            mLeDevices.clear();
            if (mBluetoothAdapter == null) {Log.w("ss","是null");}
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

}
