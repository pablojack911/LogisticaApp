package com.dynamicsoftware.pocho.logistica.DAO.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Pocho on 13/04/2017.
 */

public class RutaDeEntregaContract
{
    private RutaDeEntregaContract()
    {
    }

    public static class RutaDeEntrega implements BaseColumns
    {
        public static String TABLE_NAME = "rutaDeEntrega";
        //        public static String _FECHA = "fecha";
        public static String _CLIENTE = "cliente";
        public static String _NOMBRE = "nombre";
        public static String _DOMICILIO = "domicilio";
        public static String _EFECTIVO = "efectivo";
        public static String _CTACTE = "ctacte";
        public static String _ESTADO = "estado";
        public static String _ENVIADO = "enviado";
        public static String _ORDEN_VISITA = "orden_visita";
        public static String _FINALIZADO = "finalizado";
        public static String _FLETERO ="fletero";
    }
}
