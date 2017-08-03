package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pocho on 14/04/2017.
 */

public class Fecha
{
    private static final java.lang.String FORMATO_DIA_MES_AÑO = "dd/MM/yyyy";
    private static final java.lang.String FORMATO_FECHA_COMPLETA = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG = "Fecha";

    public static String convertirFecha(Date fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_DIA_MES_AÑO);
        return format.format(fecha);
    }

    public static String convertir(Date fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA_COMPLETA);
        return format.format(fecha);
    }

    public static java.util.Date convertir(String fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA_COMPLETA);
        Date sqlFecha = null;
//        System.out.println("Fecha.convertirFechaHora - " + fechaHoraFormat.parse(fecha));
        try
        {
            sqlFecha = format.parse(fecha);
        }
        catch (ParseException e)
        {
            Log.e(TAG, e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return sqlFecha;
    }

    public static Date obtenerFechaHoraActual()
    {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        System.out.println("Fecha.obtenerFechaActual - " + sqlDate);
        return sqlDate;
    }

    public static String obtenerFechaActual()
    {
        return Fecha.convertirFecha(Fecha.obtenerFechaHoraActual());
    }
}
