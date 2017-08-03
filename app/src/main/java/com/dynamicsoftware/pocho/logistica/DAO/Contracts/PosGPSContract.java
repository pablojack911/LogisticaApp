package com.dynamicsoftware.pocho.logistica.DAO.Contracts;

import android.provider.BaseColumns;

import com.dynamicsoftware.pocho.logistica.Modelo.BaseModel;

/**
 * Created by Pocho on 21/04/2017.
 */

public class PosGPSContract
{
    public static class PosGPS implements BaseColumns
    {
        public static String TABLE_NAME = "posGps";
        public static String _FECHA = "fecha";
        public static String _CLIENTE = "cliente";
        public static String _LATITUD = "latitud";
        public static String _LONGITUD = "longitud";
        public static String _ENVIADO = "enviado";
        public static String _ESTADO = "estado";
        public static String _USUARIO = "usuario";
    }
}
