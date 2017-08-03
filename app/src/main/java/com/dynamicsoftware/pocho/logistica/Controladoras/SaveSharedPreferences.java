package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.gson.Gson;

import static com.dynamicsoftware.pocho.logistica.CONSTANTES.LAST_DOWNLOAD;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.LAST_KNOWN_LOCATION;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.PREFERENCIAS;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.USUARIO;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.USUARIO_CONECTADO;

/**
 * Created by Pocho on 11/04/2017.
 */

public class SaveSharedPreferences
{
    static SharedPreferences sharedPref;

    static SharedPreferences getSharedPreferences(Context context)
    {
        if (sharedPref == null)
        {
            sharedPref = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        }
        return sharedPref;
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USUARIO, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        String usr = getSharedPreferences(ctx).getString(USUARIO, "");
        return usr;
    }

    public static void setConectado(Context ctx, Boolean estado)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(USUARIO_CONECTADO, estado);
        editor.commit();
    }

    public static Boolean getConectado(Context ctx)
    {
        Boolean remember = getSharedPreferences(ctx).getBoolean(USUARIO_CONECTADO, false);
        return remember;
    }

    public static void LimpiarUsuario(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        //LIMPIAR UNICAMENTE EL FLAG PARA QUE SE LOGUEE NUEVAMENTE. MANTENER EL KEY DEL USUARIO PARA QUE CUANDO ESTE DESLOGUEADO AUN MANDE UBICACIONES GPS
        editor.remove(USUARIO_CONECTADO); //clear all stored data
        editor.commit();
    }

    public static void setLastDownload(Context ctx, String fecha, String usuario)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LAST_DOWNLOAD + "_" + usuario, fecha);
        editor.commit();
    }

    public static String getLastDownload(Context ctx, String usuario)
    {
        String lastDownload = getSharedPreferences(ctx).getString(LAST_DOWNLOAD + "_" + usuario, "");
        return lastDownload;
    }

    public static void setLastKnownLocation(Context ctx, Location location)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        editor.putString(LAST_KNOWN_LOCATION, json);
        editor.commit();
    }

    public static Location getLastKnownLocation(Context ctx)
    {
        String json = getSharedPreferences(ctx).getString(LAST_KNOWN_LOCATION, "");
        Gson gson = new Gson();
        Location location = gson.fromJson(json, Location.class);
        return location;
    }
}
