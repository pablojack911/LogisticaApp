package com.dynamicsoftware.pocho.logistica.DAO.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Pocho on 15/05/2017.
 */

public class ItemFacturaContract
{
    public static class ItemFactura implements BaseColumns
    {
        public static String TABLE_NAME = "itemFactura";
        public static String _ARTICULO = "articulo";
        public static String _CODIGO_BARRA_BULTO = "codigoBarraBulto";
        public static String _CODIGO_BARRA_UNIDAD = "codigoBarraUnidad";
        public static String _DESCRIPCION = "descripcion";
        public static String _PRECIO_NETO_UNITARIO = "precioNetoUnitario";
        public static String _TASA_IVA = "tasaIva";
        public static String _IMPUESTO_INTERNO_UNITARIO = "impuestoInternoUnitario";
        public static String _PRECIO_FINAL_UNITARIO = "precioFinalUnitario";
        public static String _DESCUENTO_1 = "descuento1";
        public static String _DESCUENTO_2 = "descuento2";
        public static String _DESCUENTO_3 = "descuento3";
        public static String _DESCUENTO_4 = "descuento4";
        public static String _CANTIDAD = "cantidad";
        public static String _IMPORTE_FINAL = "importeFinal";
        public static String _FACTURA_ID = "factura_id";
        public static String _MOTIVO_RECHAZO = "motivo_rechazo";
        public static String _ID_ROW_REF_RECHAZO = "idRowRef";
    }
}
