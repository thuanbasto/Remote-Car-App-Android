package com.example.remotecar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Speech extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        // set speed về mức độ 2/5
        MainActivity.connectBT.sendData("2");
    }

    // setOnClick của button Reload
    public void reloadConnect(View view){
        if (!MainActivity.connectBT.Connect(MainActivity.connectBT.currentAddress)){
            if (!MainActivity.connectBT.Connect(MainActivity.connectBT.currentAddress))
                Toast.makeText(getBaseContext(), "Connect Failed", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
    }

    // setOnClick của ImageView
    public void getSpeechInput(View view) {
        //  thiết lập ngôn ngữ để nhận dạng
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        // 3000 là khoảng thời gian chờ để nhận dạng giọng nói
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);


        // nếu nhận được dữ liệu thì gửi 1 RequstCode về onActivityResult để xử lý
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,10);
        } else {
            Toast.makeText(this,"Don't support Speech Input",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.get(0).contains("go straight")){
                        int time = getSecond(result.get(0));
                        MainActivity.connectBT.sendData("U");
                        // nếu không nói thời gian thì mặc định sẽ đi 2s và dừng lại
                        if (time == 0)
                            timeStop(2000);
                        else
                            timeStop(time*1000);
                    }
                    else if (result.get(0).contains("right")){
                        MainActivity.connectBT.sendData("R");
                        timeStop(500);
                    }
                    else if (result.get(0).contains("left")) {
                        MainActivity.connectBT.sendData("L");
                        timeStop(500);
                    }
                    else if (result.get(0).contains("go backward")) {
                        int time = getSecond(result.get(0));
                        MainActivity.connectBT.sendData("D");
                        // nếu không nói thời gian thì mặc định sẽ đi 2s và dừng lại
                        if (time == 0)
                            timeStop(2000);
                        else
                            timeStop(time*1000);
                    } else if (result.get(0).equals("stop")){
                        MainActivity.connectBT.sendData("S");
                    }

                }
                break;
        }
    }

    // hàm lấy thời gian để cho xe dừng lại
    public void timeStop(int time){
        try {
            Thread.sleep(time);
            MainActivity.connectBT.sendData("S");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // hàm lấy thời gian trong dữ liệu nhận được từ Speech
    public int getSecond(String data){
        int second = 0;
        String[] arr = data.split(" ");
        for (String s : arr) {
            try {
                second = Integer.valueOf(s);
                return second;
            } catch (Exception e){
                Log.i("Number Error","Can't convert string to int");
            }
        }
        return second;
    }
}
