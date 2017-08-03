package com.dynamicsoftware.pocho.logistica.DAO.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Pocho on 15/05/2017.
 */

public class FacturaContract
{
    public static class Factura implements BaseColumns
    {
        public static String TABLE_NAME = "facturas";
        public static String _REPARTO = "reparto";
        public static String _EMPRESA = "empresa";
        public static String _TIPO = "tipo";
        public static String _NUMERO = "numero";
        public static String _ALICUOTA_IIBB = "alicuotaIibb";
        public static String _SUBTOTAL = "subtotal";
        public static String _IVA_BASICO_TOTAL = "ivaBasicoTotal";
        public static String _IVA_ADICIONAL_TOTAL = "ivaAdicionalTotal";
        public static String _IMPUESTO_INTERNO_TOTAL = "impuestoInternoTotal";
        public static String _PERCEPCION_IIBB_TOTAL = "percepcionIIBBTotal";
        public static String _TOTAL = "total";
        public static String _CONDICION_VENTA = "condicionVenta";
        public static String _CLIENTE = "cliente";
        public static String _CODIGO_RECHAZO = "codigoRechazo";
        public static String _SUBEMPRESA = "subempresa";
    }
}
