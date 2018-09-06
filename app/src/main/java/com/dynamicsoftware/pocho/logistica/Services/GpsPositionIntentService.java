package com.dynamicsoftware.pocho.logistica.Services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraPosGPS;
import com.dynamicsoftware.pocho.logistica.Modelo.PosGPS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static com.dynamicsoftware.pocho.logistica.CONSTANTES.POSICION_GPS_KEY;

/**
 * Created by pinsua on 08/03/2018.
 */

public class GpsPositionIntentService extends IntentService
{
    private static final String TAG = "GPSPosIntentService";
    FusedLocationProviderClient mFusedLocationClient;
    private ControladoraPosGPS controladorPosicionesGPS;

    public GpsPositionIntentService()
    {
        this(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GpsPositionIntentService(String name)
    {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        controladorPosicionesGPS = new ControladoraPosGPS(getApplicationContext());
        final PosGPS posGPS;
        if (intent != null && intent.hasExtra(POSICION_GPS_KEY))
        {
            posGPS = intent.getParcelableExtra(POSICION_GPS_KEY);
        }
        else
        {
            posGPS = new PosGPS();
        }
        if (ActivityCompat.checkSelfPermission(GpsPositionIntentService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GpsPositionIntentService.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location)
                {
                    if (location.getLatitude() != 0)
                    {
                        posGPS.setFecha(new Date(location.getTime()));
                        posGPS.setLatitud(Float.valueOf(String.valueOf(location.getLatitude())));
                        posGPS.setLongitud(Float.valueOf(String.valueOf(location.getLongitude())));
                        controladorPosicionesGPS.insertar(posGPS);
                    }
                }
            });
            try
            {
                this.enviarPosPendientes();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    private void enviarPosPendientes() throws Exception
    {
        ArrayList<PosGPS> posPendientes = controladorPosicionesGPS.obtenerPosicionesPendientes();
        for (PosGPS pos : posPendientes)
        {
            this.enviaPosGPS(pos);
        }
    }

    private void enviaPosGPS(PosGPS posGPS) throws Exception
    {
//        JSONObject json = creaJson(posGPS);
        Gson gson = new Gson();
        String json = gson.toJson(posGPS, PosGPS.class);
        URL url = new URL(CONSTANTES.URL_ENVIA_POS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONSTANTES.TIMEOUT);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.close();
        // read the response
        int res = conn.getResponseCode();
        if (res == 200) //OK
        {
            Log.d(TAG, "Grabando en server -> " + res);
            res = controladorPosicionesGPS.actualizarEnviado(1, posGPS.getId());
            Log.d(TAG, "Actualizado en bd -> " + res + "_" + posGPS.getCliente());
        }
    }

}
