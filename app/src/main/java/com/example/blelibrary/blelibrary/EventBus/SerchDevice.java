package com.example.blelibrary.blelibrary.EventBus;

import android.bluetooth.BluetoothDevice;

public class SerchDevice {
    private BluetoothDevice mLeDevices;
    public SerchDevice(BluetoothDevice mLeDevices){this.mLeDevices=mLeDevices;}
    public BluetoothDevice getDevic(){
        return mLeDevices;
    }
}
