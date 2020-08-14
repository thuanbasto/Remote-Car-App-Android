package com.example.remotecar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class ConnectBT {
    // SPP UUID service: port giao tiep giua HC-06 vs device
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public String currentAddress = "";
    // Adapter dùng để chuyển đỗi giữa HC-06 và Bluetooth
    public BluetoothAdapter btAdapter;
    // socket giữa đt vs xe
    public BluetoothSocket btSocket = null;
    // truyền dữ liệu
    public OutputStream outStream = null;

    public ConnectBT(BluetoothAdapter btAdapter) {
        this.btAdapter = btAdapter;
    }

    // Hàm kết nối xe với điện thoại
    public boolean Connect(String address){
        currentAddress = address;
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
            btAdapter.cancelDiscovery();
            btSocket.connect();
            outStream = btSocket.getOutputStream();
        } catch (IOException e1) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.i("Error",e2.getMessage());
            }
            return false;
        }
        return true;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("Error", "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public boolean sendData(String message) {
        try {
            outStream.write(message.getBytes());
        } catch (IOException e) {
            Log.i("A","Error when send a message");
            return false;
        }
        return true;
    }
}
