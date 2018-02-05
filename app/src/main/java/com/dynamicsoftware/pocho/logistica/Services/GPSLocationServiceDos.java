package com.dynamicsoftware.pocho.logistica.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Modelo.PosGPS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.sql.Date;

/**
 * Created by pinsua on 30/01/2018.
 */

public class GPSLocationServiceDos extends Service
{
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                for (Location location : locationResult.getLocations())
                {
                    procesaPosicion(location);
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
        }
    }

    @Override
    public void onDestroy()
    {
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    private void stopLocationUpdates()
    {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void procesaPosicion(Location location)
    {
        PosGPS pos = new PosGPS();
        pos.setCliente("VIAJE");
        pos.setUsuario(SaveSharedPreferences.getUserName(getApplicationContext()));
        if (location != null)
        {
            pos.setLatitud(Float.valueOf(String.valueOf(location.getLatitude())));
            pos.setLongitud(Float.valueOf(String.valueOf(location.getLongitude())));
            pos.setFecha(new Date(location.getTime()));
        }
        Intent mServiceIntent = new Intent(getApplicationContext(), GPSIntentService.class);
        mServiceIntent.putExtra(CONSTANTES.POSICION_GPS_KEY, pos);
        startService(mServiceIntent);
    }
}
