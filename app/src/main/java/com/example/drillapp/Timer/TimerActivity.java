package com.example.drillapp.Timer;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.provider.Settings;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Spinner;

import com.example.drillapp.MainActivity;
import com.example.drillapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    TextView timerText;
    Spinner spinner;
    CountDownTimer countDownTimer;
    Button pauseButton;
    String startTime;
    Ringtone defaultRingtone;
    ToggleButton toggleButton;
    boolean isPaused;
    long timeLeft;
    ImageView timerImage;
    Animation shakeAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);
        timerImage = findViewById(R.id.timer_activity_image);
        timerText = findViewById(R.id.timer_activity_textView);
        pauseButton = findViewById(R.id.timer_activity_pause_action);
        pauseButton.setVisibility(View.GONE);
        pauseButton.setOnClickListener(this);
        defaultRingtone = RingtoneManager.getRingtone(this,
                Settings.System.DEFAULT_RINGTONE_URI);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(startTime != null) {
                    if (isChecked) {
                        pauseButton.setVisibility(View.VISIBLE);
                        if (isPaused) {
                            pausedTimer();
                            isPaused = false;
                        } else {

                            countDownTimer();
                        }
                    } else {

                        pauseButton.setVisibility(View.GONE);
                        defaultRingtone.stop();
                    }
                } else {
                    Context context = getApplicationContext();
                    Toast.makeText( context,"Set time!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.timer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
    public void countDownTimer() {
        long time = Long.parseLong(startTime);
        countDownTimer = new CountDownTimer(time * 1000,1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("seconds remaining: " + millisUntilFinished / 1000);

                timeLeft = millisUntilFinished;
            }
            public void onFinish() {
                defaultRingtone.play();
                timerImage.startAnimation(shakeAnimation);
                timerText.setText("Done!");
            }
        }.start();
    }

    public void pausedTimer() {
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                defaultRingtone.play();
                timerImage.startAnimation(shakeAnimation);
                timerText.setText("Done!");
            }
        }.start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.timer_activity_pause_action:
                isPaused = true;
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                toggleButton.setChecked(false);
                defaultRingtone.stop();
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if(pos > 0) {
            startTime = parent.getItemAtPosition(pos).toString();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}