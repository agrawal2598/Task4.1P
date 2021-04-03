package com.example.workoutTimerApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.assignment3.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView countdown,studytime;
    Button start, pause,reset;
    EditText workoutTextField;
    Handler customHander=new Handler();
    int sec;
    int mins;
    int millisecond;

    long startTime=0L,timeMiliSecond=0L,updateTime=0L,timeSwapBuff=0L;

  public static final String Shared_pref="sharedprefs";
    public static final String TEXT="text";
    private String text;
    private String text2;

    Runnable updateTimerThread=new Runnable() {
    @Override
    public void run() {
        timeMiliSecond=SystemClock.uptimeMillis()-startTime;

        updateTime=timeSwapBuff+timeMiliSecond;
        sec=(int)(updateTime/1000);
        mins=sec/60;
        sec%=60;
        millisecond=(int)(updateTime%1000);
        countdown.setText(""+mins+":"+String.format("%02d",sec)+":"+String.format("%03d",millisecond));
        customHander.postDelayed(this,0);
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studytime = findViewById(R.id.studytime);
        workoutTextField = findViewById(R.id.workoutTextField);
        countdown = findViewById(R.id.countdownText);
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        reset = findViewById(R.id.reset);

if(savedInstanceState!=null)
{
    sec=savedInstanceState.getInt("count3");
    mins=savedInstanceState.getInt("count");
    millisecond=savedInstanceState.getInt("count2");
    countdown.setText(""+mins+":"+String.format("%02d",sec)+":"+String.format("%03d",millisecond));


}

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime= SystemClock.uptimeMillis();
                customHander.postDelayed(updateTimerThread,0);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff+=timeMiliSecond;
                customHander.removeCallbacks(updateTimerThread);
            }
        });
                reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeSwapBuff+=timeMiliSecond;
                customHander.removeCallbacks(updateTimerThread);
                studytime.setText("You Spend "+countdown.getText().toString()+" on "+workoutTextField.getText().toString()+ " Last Time");
                SharedPreferences sharedPreferences=getSharedPreferences(Shared_pref,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(TEXT,studytime.getText().toString());
                editor.apply();
            }
        });
                loadData();
                updateView();

    }
    public void loadData()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_pref,MODE_PRIVATE);
        text=sharedPreferences.getString(TEXT, "");
    }
    public void updateView()
    {
        studytime.setText(text);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("count3",sec);
        outState.putInt("count",mins);
        outState.putInt("count2",millisecond);
    }


}
