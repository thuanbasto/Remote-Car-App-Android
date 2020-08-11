package com.example.remotecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    Button button,speech,gyroscope;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;

        setup();
    }

    private void setup(){
        button = (Button) findViewById(R.id.buttonRemote);
        speech = (Button) findViewById(R.id.buttonSpeech);
        gyroscope = (Button) findViewById(R.id.buttonGyroscope);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ButtonRemote.class);
                startActivity(intent);
            }
        });
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Speech.class);
                startActivity(intent);
            }
        });
        gyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Gyroscope.class);
                startActivity(intent);
            }
        });
    }
}
