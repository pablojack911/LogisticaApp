package com.dynamicsoftware.pocho.logistica.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraPosGPS;
import com.dynamicsoftware.pocho.logistica.Controladoras.Fecha;
import com.dynamicsoftware.pocho.logistica.Controladoras.Utiles;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.PosGPSContract;
import com.dynamicsoftware.pocho.logistica.Modelo.PosGPS;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

import static com.dynamicsoftware.pocho.logistica.CONSTANTES.POSICION_GPS_KEY;

/**
 * Created by Pocho on 24/04/2017.
 */

public class GPSIntentService extends IntentService
{

    private static final String TAG = "GPSIntentService";
    private ControladoraPosGPS controladorPosicionesGPS;

    public GPSIntentService()
    {
        this(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPSIntentService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        controladorPosicionesGPS = new ControladoraPosGPS(getApplicationContext());
        PosGPS posGPS;
        if (intent != null && intent.hasExtra(POSICION_GPS_KEY))
        {
            posGPS = intent.getParcelableExtra(POSICION_GPS_KEY);
        }
        else
        {
            posGPS = new PosGPS();
        }
        if (posGPS.getLatitud() == 0)
        {
            Location location = Utiles.obtenerUbicacion(getApplicationContext());
            if (location != null)
            {
                posGPS.setLongitud((float) location.getLongitude());
                posGPS.setLatitud((float) location.getLatitude());
                posGPS.setFecha(new Date(location.getTime()));
            }
        }
//        posGPS.setFecha(Fecha.obtenerFechaHoraActual());
        int id = (int) controladorPosicionesGPS.insertar(posGPS);
        posGPS.setId(id);
        try
        {
            this.enviarPosPendientes();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //TODO: CERRAR
            //controladorPosicionesGPS.cerrar();
        }
    }

    private void enviarPosPendientes() throws IOException, JSONException
    {
        ArrayList<PosGPS> posPendientes = controladorPosicionesGPS.obtenerPosicionesPendientes();
        for (PosGPS pos : posPendientes)
        {
            this.enviaPosGPS(pos);
        }
    }

    private void enviaPosGPS(PosGPS posGPS) throws IOException, JSONException
    {
//        JSONObject json = creaJson(posGPS);
        Gson gson = new Gson();
        String json = gson.toJson(posGPS, PosGPS.class);
        URL url = new URL(CONSTANTES.URL_ENVIA_POS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(60 * 1000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        OutputStream os = conn.getOutputStream();
        os.write(json.toString().getBytes("UTF-8"));
        os.close();
        // read the response
        int res = conn.getResponseCode();
        if (res == 200) //OK
        {
//            Log.d(TAG, "Grabando en server -> " + res);
            res = controladorPosicionesGPS.actualizarEnviado(1, posGPS.getId());
//            Log.d(TAG, "Actualizado en bd -> " + res);
        }
    }

    private JSONObject creaJson(PosGPS posGPS) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put(PosGPSContract.PosGPS._CLIENTE, posGPS.getCliente());
        json.put(PosGPSContract.PosGPS._USUARIO, posGPS.getUsuario());
        json.put(PosGPSContract.PosGPS._ESTADO, posGPS.getEstadoEntrega().ordinal());
        json.put(PosGPSContract.PosGPS._LATITUD, posGPS.getLatitud());
        json.put(PosGPSContract.PosGPS._LONGITUD, posGPS.getLongitud());
        json.put(PosGPSContract.PosGPS._FECHA, Fecha.convertir(posGPS.getFecha()));
        return json;
    }
}
