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
 * Lala
 */

public class SaveSharedPreferences
{
    private static SharedPreferences sharedPref;

    private static SharedPreferences getSharedPreferences(Context context)
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
        editor.apply();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USUARIO, "");
    }

    public static void setConectado(Context ctx, Boolean estado)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(USUARIO_CONECTADO, estado);
        editor.apply();
    }

    public static Boolean getConectado(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(USUARIO_CONECTADO, false);
    }

    public static void LimpiarUsuario(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        //LIMPIAR UNICAMENTE EL FLAG PARA QUE SE LOGUEE NUEVAMENTE. MANTENER EL KEY DEL USUARIO PARA QUE CUANDO ESTE DESLOGUEADO AUN MANDE UBICACIONES GPS
        setConectado(ctx, false);
        editor.remove(USUARIO_CONECTADO); //clear all stored data
        editor.apply();
    }

    public static void setLastDownload(Context ctx, String fecha, String usuario)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LAST_DOWNLOAD + "_" + usuario, fecha);
        editor.apply();
    }

    public static String getLastDownload(Context ctx, String usuario)
    {
        return getSharedPreferences(ctx).getString(LAST_DOWNLOAD + "_" + usuario, "");
    }

//    public static void setLastKnownLocation(Context ctx, Location location)
//    {
//        if (location.getLatitude() != 0)
//        {
//            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(location);
//            editor.putString(LAST_KNOWN_LOCATION, json);
//            editor.apply();
//        }
//    }

//    public static Location getLastKnownLocation(Context ctx)
//    {
//        String json = getSharedPreferences(ctx).getString(LAST_KNOWN_LOCATION, "");
//        Gson gson = new Gson();
//        return gson.fromJson(json, Location.class);
//    }
}
