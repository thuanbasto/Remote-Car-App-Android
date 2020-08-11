package com.example.remotecar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Gyroscope extends AppCompatActivity {
    // quản lí cảm biến
    SensorManager sensorManager;
    // con quay hồi chuyển
    Sensor gyroscopeSensor;
    SensorEventListener gyroscopeSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (gyroscopeSensor == null)
            Toast.makeText(this,"The device has no Gyroscope",Toast.LENGTH_LONG).show();

        // Create a listener
        gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.i("ok","ok");
                if(sensorEvent.values[2] > 0.5f) { // anticlockwise
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if(sensorEvent.values[2] < -0.5f) { // clockwise
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener,gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeSensorListener);
    }
}
