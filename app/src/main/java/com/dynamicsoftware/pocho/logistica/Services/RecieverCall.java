package com.dynamicsoftware.pocho.logistica.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pocho on 09/05/2017.
 */

public class RecieverCall extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("Service Stops", "Ohhhhhhh");
        context.startService(new Intent(context, GPSLocationServiceDos.class));
    }
}
