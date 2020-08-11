package com.example.remotecar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button buttonScan;
    ListView listViewDevices;

    Context context;
    private ArrayAdapter deviceAdapter;
    private List<String> nameDevices = new ArrayList<String>();
    private List<Device> devices = new ArrayList<>();

    public static ConnectBT connectBT;

    private BluetoothAdapter btAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        connectBT = new ConnectBT(btAdapter);

        checkBTState();
        setup();
    }

    public void scanPairedDevice(){
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        nameDevices.clear();
        devices.clear();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceAddress = device.getAddress(); // MAC address

                Log.i("ok",deviceAddress);
                nameDevices.add(deviceName);
                devices.add(new Device(deviceName,deviceAddress));
            }
        }
        deviceAdapter = new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,nameDevices);
        listViewDevices.setAdapter(deviceAdapter);
        deviceAdapter.notifyDataSetChanged();
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Bluetooth doesn't support!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("Bluetooth", "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void setup(){
        buttonScan = (Button) findViewById(R.id.buttonScan);
        listViewDevices = (ListView) findViewById(R.id.listView);

        buttonScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scanPairedDevice();
            }
        });

        listViewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (connectBT.btSocket != null){
                    if (connectBT.btSocket.isConnected()){
                        try {
                            connectBT.btSocket.close();
                            if (connectBT.outStream != null)
                                connectBT.outStream.flush();
                            connectBT.outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!connectBT.Connect(devices.get(position).address))
                    Toast.makeText(getBaseContext(), "Connect Failed", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,Home.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connectBT.btSocket != null){
            try {
                connectBT.btSocket.close();
                if (connectBT.outStream != null)
                    connectBT.outStream.flush();
                connectBT.outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}