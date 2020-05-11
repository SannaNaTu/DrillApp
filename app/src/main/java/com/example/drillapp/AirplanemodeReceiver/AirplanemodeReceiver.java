package com.example.drillapp.AirplanemodeReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class AirplanemodeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if
        (Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
            Toast.makeText(context, "AIRPLANE MODE On", Toast.LENGTH_SHORT).show();
        } else


            Toast.makeText(context, "AIRPLANE MODE Off", Toast.LENGTH_SHORT).show();
    }
}
