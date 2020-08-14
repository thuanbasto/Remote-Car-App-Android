package com.example.remotecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class ButtonRemote extends AppCompatActivity {
    Button buttonUp,buttonDown,buttonLeft, buttonRight;
    SeekBar speed;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_remote);
        context = this;

        setup();
    }

    // SetOnClick cua button Reload
    public void reloadConnect(View view){
        if (!MainActivity.connectBT.Connect(MainActivity.connectBT.currentAddress)){
            if (!MainActivity.connectBT.Connect(MainActivity.connectBT.currentAddress))
                Toast.makeText(getBaseContext(), "Connect Failed", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
    }

    private void setup(){
        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        speed = (SeekBar) findViewById(R.id.seekBar);

        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(ButtonRemote.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                MainActivity.connectBT.sendData(String.valueOf(progressChangedValue));
            }
        });

        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!MainActivity.connectBT.sendData("U"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (!MainActivity.connectBT.sendData("S"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!MainActivity.connectBT.sendData("R"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (!MainActivity.connectBT.sendData("S"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!MainActivity.connectBT.sendData("L"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (!MainActivity.connectBT.sendData("S"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!MainActivity.connectBT.sendData("D"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (!MainActivity.connectBT.sendData("S"))
                        Toast.makeText(context,"Error.Reload connect pls!!",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }
}
