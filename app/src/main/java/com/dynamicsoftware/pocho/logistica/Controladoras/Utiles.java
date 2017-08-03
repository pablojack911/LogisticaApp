package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.View;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Pocho on 25/04/2017.
 */

public class Utiles
{
    private static final String TAG = "Utiles";

    public static void displayPromptForEnablingGPS(final Activity activity)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Es necesario que GPS esté activado para poder continuar.";

        builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface d, int id)
            {
                activity.startActivity(new Intent(action));
                d.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public static void cambiaVisibilidad(View vista, int visibilidad)
    {
        vista.setVisibility(visibilidad);
    }

    //    public static Location obtenerUbicacion(Context context)
//    {
//        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        Location bestLocation = null;
//        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//        {
//            bestLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (bestLocation == null)
//            {
//                bestLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
//        }
//        return bestLocation;
//    }
    public static Location obtenerUbicacion(Context context)
    {
        Location bestLocation = SaveSharedPreferences.getLastKnownLocation(context);
        return bestLocation;
    }

    public static boolean GPSActivado(Context context)
    {
        LocationManager manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
