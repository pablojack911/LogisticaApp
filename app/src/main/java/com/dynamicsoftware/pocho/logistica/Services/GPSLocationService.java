package com.dynamicsoftware.pocho.logistica.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraPosGPS;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Modelo.PosGPS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.sql.Date;

/**
 * Created by Pocho on 09/05/2017.
 */

public class GPSLocationService extends Service implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    private static final String TAG = "GPSLocationService";
    ControladoraPosGPS controladoraPosGPS;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

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
        controladoraPosGPS = new ControladoraPosGPS(getApplicationContext());
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try
        {
            mGoogleApiClient.connect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    public void onDestroy()
    {
        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.disconnect();
        }
        Intent intent = new Intent("com.android.techtrainner");
        intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        SaveSharedPreferences.setLastKnownLocation(getApplicationContext(), location);
        procesaPosicion(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.e(TAG, "onConnectionFailed - " + connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
//        mLocationRequest.setFastestInterval(60 * 1000);
//        mLocationRequest.setSmallestDisplacement(10);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.e(TAG, "onConnectionSuspended - " + i);
    }
}
