package com.example.drillapp.Clock;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.*;  // Classes Canvas, Color, Paint, RectF, etc.
import android.provider.Settings;
import android.util.Log;
import android.view.*;      // Classes View, Display, WindowManager, etc.
import android.widget.*;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MotionEventCompat;

import com.example.drillapp.Clock.AlarmClockView;
import com.example.drillapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmClockActivity extends AppCompatActivity {
    AlarmClockView alarm_clock_view;
    static Ringtone defaultRingtone;
    static boolean isAlarmSet = false;
    static View alarmView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);

        defaultRingtone = RingtoneManager.getRingtone(this,
                Settings.System.DEFAULT_RINGTONE_URI);

        setContentView(R.layout.activity_alarm_clock);

        alarm_clock_view = (AlarmClockView) findViewById(R.id.alarm_clock_view);

        alarmView = findViewById(R.id.alarm_clock_view);

    }

    public void onStart() {
        super.onStart();

        alarm_clock_view.start_animation_thread();
    }

    public void onStop() {
        super.onStop();

        alarm_clock_view.stop_animation_thread();
    }

    public void set_alarm_button_clicked(View view) {
        Button set_alarm_button = (Button) view;

        set_alarm_button = findViewById(R.id.left_button);
        if (set_alarm_button.getText().equals("End Set Alarm")) {
            // We are already setting the alarm. Let's stop it.
            set_alarm_button.setText("Set Alarm");
            set_alarm_button.setTextColor(Color.WHITE);

            alarm_clock_view.disable_alarm_time_setting();
            activateAlarm(false);

        } else {
            set_alarm_button.setText("End Set Alarm");
            set_alarm_button.setTextColor(Color.RED);

            alarm_clock_view.enable_alarm_time_setting();


        }
    }

    public void activate_alarm_button_clicked(View view) {
        Button activate_alarm_button = (Button) view;

        activate_alarm_button = findViewById(R.id.down_button);

        if (activate_alarm_button.getText().equals("Deactivate Alarm")) {
            activate_alarm_button.setText("Activate Alarm");
            activate_alarm_button.setTextColor(Color.WHITE);

            isAlarmSet = false;
        } else {
            activate_alarm_button.setText("Deactivate Alarm");
            activate_alarm_button.setTextColor(Color.RED);

            isAlarmSet = true;
        }

    }


    public static void activateAlarm(boolean activate) {
        // sound
        if (activate && isAlarmSet) {
            // start
            defaultRingtone.play();
            onTouch();

        } else {
            // stop
            defaultRingtone.stop();
        }
    }

    public static void onTouch() {

        alarmView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    AlarmClockView.snooze();
                }
                isAlarmSet = false;

                return true;
            }

        });
    }
}