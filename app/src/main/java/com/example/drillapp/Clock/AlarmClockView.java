package com.example.drillapp.Clock;

import android.os.Bundle;
import android.graphics.*;  // Classes Canvas, Color, Paint, RectF, etc.
import android.view.*;      // Classes View, Display, WindowManager, etc.
import android.content.Context;
import android.util.AttributeSet;



import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmClockView extends View implements Runnable {


    Thread animation_thread = null;

    boolean thread_must_be_executed = false;

    int view_width, view_height;

    int clock_center_point_x, clock_center_point_y;

    int hour_hand_length = 152;
    int minute_hand_length = 188;
    int second_hand_length = 200;

    Paint background_paint = new Paint();
    Paint paint_for_clock_dial = new Paint();
    Paint paint_for_text = new Paint();

    Paint paint_for_second_hand = new Paint();
    Paint paint_for_minute_hand = new Paint();
    Paint paint_for_hour_hand = paint_for_minute_hand;
    Paint paint_for_alarm_hand = new Paint();

    int alarm_hand_end_x, alarm_hand_end_y;
    static int alarm_hour, alarm_minute;

    // The following variable is set to true when the setting
    // of alarm time is activated from 'outside' this object.
    boolean alarm_time_setting_enabled = false;

    // The following variable is set to true while the 'mouse'
    // operation for setting the alarm time is going on.
    boolean alarm_time_is_being_set = false;


    //  The following constructor is needed when a View-based object is
    //  specified in an XML file, and is thus created automatically.

    public AlarmClockView(Context context, AttributeSet attributes) {
        super(context, attributes);

        background_paint.setColor(Color.rgb(210, 255, 210)); // light green

        paint_for_clock_dial.setStyle(Paint.Style.FILL);

        paint_for_text.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_for_text.setTextSize(35);
        paint_for_text.setColor(Color.BLACK);

        paint_for_second_hand.setStyle(Paint.Style.STROKE);
        paint_for_second_hand.setColor(Color.RED);

        paint_for_minute_hand.setStyle(Paint.Style.STROKE);
        paint_for_minute_hand.setColor(Color.rgb(0x6B, 0x8E, 0x23)); // olive color
        paint_for_minute_hand.setStrokeWidth(4);

        paint_for_alarm_hand.setStyle(Paint.Style.STROKE);
        paint_for_alarm_hand.setColor(Color.GREEN);
        paint_for_alarm_hand.setStrokeWidth(2);

    }

    public void onSizeChanged(int current_width_of_this_view,
                              int current_height_of_this_view,
                              int old_width_of_this_view,
                              int old_height_of_this_view) {
        view_width = current_width_of_this_view;
        view_height = current_height_of_this_view;

        clock_center_point_x = view_width / 2;
        clock_center_point_y = view_height / 2;

        // The lengths of the clock hands will be set so that the clock
        // fits nicely onto the available screen.

        int maximum_clock_hand_length = Math.min(view_width, view_height) / 2;

        second_hand_length = maximum_clock_hand_length * 80 / 100;
        minute_hand_length = maximum_clock_hand_length * 76 / 100;
        hour_hand_length = maximum_clock_hand_length * 60 / 100;

        alarm_hand_end_x = clock_center_point_x;
        alarm_hand_end_y = clock_center_point_y - minute_hand_length;

    }

    public void enable_alarm_time_setting() {
        alarm_time_setting_enabled = true;
    }

    public void disable_alarm_time_setting() {
        alarm_time_setting_enabled = false;
    }

    public void start_animation_thread() {
        if (animation_thread == null) {
            animation_thread = new Thread(this);

            thread_must_be_executed = true;

            animation_thread.start();
        }
    }

    public void stop_animation_thread() {
        if (animation_thread != null) {
            animation_thread.interrupt();

            thread_must_be_executed = false;

            animation_thread = null;
        }
    }

    public void run() {
        while (thread_must_be_executed == true) {
            try {
                Thread.sleep(200);  //  Delay of 200 milliseconds.
            } catch (InterruptedException caught_exception) {
                thread_must_be_executed = false;
            }

            // Here we must use the method postInvalidate() instead of
            // invalidate(). postInvalidate() must be used to invalidate
            // a View from a non-UI thread.

            postInvalidate();
        }
    }


    void update_alarm_information(int given_pointed_position_x,
                                  int given_pointed_position_y) {
        //  We'll calculate an angle for the alarm time hand

        int x_for_alarm_hand = given_pointed_position_x - clock_center_point_x;
        int y_for_alarm_hand = given_pointed_position_y - clock_center_point_y;

        double alarm_hand_angle = Math.atan2(y_for_alarm_hand,
                x_for_alarm_hand);

        alarm_hand_end_x = (int) (Math.cos(alarm_hand_angle) *
                minute_hand_length + clock_center_point_x);
        alarm_hand_end_y = (int) (Math.sin(alarm_hand_angle) *
                minute_hand_length + clock_center_point_y);

        // Now we know how the alarm time hand should be located on the screen.
        // We still need to decide what it means in hours and minutes.

        //  The following are facts related to the clock dial and
        //  our system of angles:
        //    -  00:00 hours corresponds to the angle math.pi / 2
        //    -  03:00 hours corresponds to the angle 0
        //    -  an hour in the clock dial corresponds to 2 * math.pi / 12

        // Currently the alarm_hand_angle is such that it is
        // negative when the hand points upwards, and positive otherwise.
        // We'll modify it so that it corresponds to the normal mathematical
        // system of angles.

        if (alarm_hand_angle <= 0) {
            alarm_hand_angle = -alarm_hand_angle;
        } else {
            alarm_hand_angle = 2 * Math.PI - alarm_hand_angle;
        }

        double alarm_time;

        if (alarm_hand_angle >= 0 &&
                alarm_hand_angle <= Math.PI / 2) {
            alarm_time = 3 - alarm_hand_angle * 6 / Math.PI;
        } else {
            alarm_time = 3 + 12 - alarm_hand_angle * 6 / Math.PI;
        }

        alarm_hour = (int) alarm_time;
        alarm_minute = (int) (60 * (alarm_time - alarm_hour));

        //  Now alarm_hour is a value between 0 .... 11
        //  We want to set the alarm so that the clock will alarm
        //  within the next 12 hours

        //  current_time will refer to a 'decimal' time value in the
        //  same way as alarm_time. This allows us to compare these
        //  times in an accurate manner.

        Calendar time_now = new GregorianCalendar();

        double current_time = time_now.get(Calendar.HOUR_OF_DAY) +
                (time_now.get(Calendar.MINUTE) / 60.0);

        if (current_time >= 12.0) {
            // The clock is now running the p.m. hours

            if (alarm_time + 12 > current_time) {
                alarm_hour += 12;
            }
        } else {
            // The clock time belongs to the a.m. part of the day

            if (alarm_time < current_time) {
                alarm_hour += 12;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motion_event) {
        if (motion_event.getAction() == MotionEvent.ACTION_DOWN) {
            alarm_time_is_being_set = true;

            update_alarm_information((int) motion_event.getX(),
                    (int) motion_event.getY());

        } else if (motion_event.getAction() == MotionEvent.ACTION_MOVE) {
            if (alarm_time_is_being_set == true) {
                update_alarm_information((int) motion_event.getX(),
                        (int) motion_event.getY());
            }
        } else if (motion_event.getAction() == MotionEvent.ACTION_UP) {
            alarm_time_is_being_set = false;
        }

        invalidate();

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(background_paint); // This fills the entire canvas.

        String[] days_of_week = {"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"};

        String[] names_of_months =

                {"January", "February", "March", "April",
                        "May", "June", "July", "August",
                        "September", "October", "November", "December"};

        Calendar time_now = new GregorianCalendar();

        int current_year = time_now.get(Calendar.YEAR);
        int current_day = time_now.get(Calendar.DAY_OF_MONTH);
        int month_index = time_now.get(Calendar.MONTH);
        int number_of_day_of_week
                = time_now.get(Calendar.DAY_OF_WEEK);

        String current_month = names_of_months[month_index];

        String current_day_of_week =
                days_of_week[number_of_day_of_week - 1];

        int current_hours = time_now.get(Calendar.HOUR_OF_DAY);
        int current_minutes = time_now.get(Calendar.MINUTE);
        int current_seconds = time_now.get(Calendar.SECOND);
        int current_milliseconds = time_now.get(Calendar.MILLISECOND);

        canvas.drawText("" + current_day_of_week +
                ", " + current_month +
                " " + current_day +
                ", " + current_year, 30, 40, paint_for_text);

        canvas.drawText(String.format("%d:%02d:%02d",
                current_hours, current_minutes, current_seconds),
                30, 80, paint_for_text);

        // Let's print a 10-point dot in the center of the clock.

        canvas.drawOval(new RectF(clock_center_point_x - 5,
                        clock_center_point_y - 5,
                        clock_center_point_x + 5,
                        clock_center_point_y + 5),
                paint_for_clock_dial);

        // The following loop prints dots on the clock dial.

        int dot_index = 0;

        while (dot_index < 60) {
            double dot_angle = dot_index * Math.PI / 30 - Math.PI / 2;

            int dot_position_x = (int) (Math.cos(dot_angle) * second_hand_length
                    + clock_center_point_x);
            int dot_position_y = (int) (Math.sin(dot_angle) * second_hand_length
                    + clock_center_point_y);

            int dot_radius = 2;

            if ((dot_index % 5) == 0) {
                // Every 5th dot on the clock circle is a larger dot.
                dot_radius = 4;
            }

            canvas.drawOval(new RectF(dot_position_x - dot_radius,
                            dot_position_y - dot_radius,
                            dot_position_x + dot_radius,
                            dot_position_y + dot_radius),
                    paint_for_clock_dial);

            dot_index = dot_index + 1;
        }


        //  For every hand of the clock, we'll first calculte the
        //  angle of the hand in question. Then we can calculate the
        //  the coordinates of the end point of the hand, and finally
        //  we'll draw the hand.

        double hour_hand_angle = (current_hours * 30 + current_minutes / 2)
                * Math.PI / 180 - Math.PI / 2;

        int hour_hand_end_x = (int) (Math.cos(hour_hand_angle) *
                hour_hand_length + clock_center_point_x);
        int hour_hand_end_y = (int) (Math.sin(hour_hand_angle) *
                hour_hand_length + clock_center_point_y);

        canvas.drawLine(clock_center_point_x, clock_center_point_y,
                hour_hand_end_x, hour_hand_end_y, paint_for_hour_hand);


        double minute_hand_angle = ((double) current_minutes +
                (double) current_seconds / 60.0)
                * Math.PI / 30 - Math.PI / 2;

        int minute_hand_end_x = (int) (Math.cos(minute_hand_angle) *
                minute_hand_length + clock_center_point_x);

        int minute_hand_end_y = (int) (Math.sin(minute_hand_angle) *
                minute_hand_length + clock_center_point_y);

        canvas.drawLine(clock_center_point_x, clock_center_point_y,
                minute_hand_end_x, minute_hand_end_y,
                paint_for_minute_hand);


        double second_hand_angle = ((double) current_seconds +
                (double) current_milliseconds / 1000.0)
                * Math.PI / 30 - Math.PI / 2;

        int second_hand_end_x = (int) (Math.cos(second_hand_angle) *
                second_hand_length + clock_center_point_x);
        int second_hand_end_y = (int) (Math.sin(second_hand_angle) *
                second_hand_length + clock_center_point_y);

        canvas.drawLine(clock_center_point_x, clock_center_point_y,
                second_hand_end_x, second_hand_end_y,
                paint_for_second_hand);

        if (alarm_time_setting_enabled == true) {
            // Let's draw the alarm hand.

            canvas.drawLine(clock_center_point_x, clock_center_point_y,
                    alarm_hand_end_x, alarm_hand_end_y,
                    paint_for_alarm_hand);

            // Let's draw the alarm time as text.

            canvas.drawText(String.format("ALARM: %d:%02d",
                    alarm_hour, alarm_minute),
                    25, 125, paint_for_text);

            int hourLeft = current_hours - alarm_hour;
            int minuteLeft = current_minutes - alarm_minute;

            canvas.drawText( "TIME TO ALARM " + hourLeft + " hours " + minuteLeft + " minutes",
                    450, 125, paint_for_text);
        }

        if (alarm_time_setting_enabled) {
            if (current_hours == alarm_hour && current_minutes == alarm_minute) {
                AlarmClockActivity.activateAlarm(true);

            }
        }
    }

    public static void snooze() {
        alarm_minute += 5;

        if (alarm_minute >= 55) {
            alarm_hour += 1;
            alarm_minute = 00;
        }
    }

}