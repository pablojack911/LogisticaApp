package com.dynamicsoftware.pocho.logistica.DAO.DbSchema;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.FacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.ItemFacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.MotivoRechazoFacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.PosGPSContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.RutaDeEntregaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.UsuariosContract;

/**
 * Created by Pocho on 13/04/2017.
 */

public class CreateTables
{
    public static String Usuarios = "CREATE TABLE " + UsuariosContract.Usuarios.TABLE_NAME + " ( " +
            UsuariosContract.Usuarios._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UsuariosContract.Usuarios._USUARIO + " text, " +
            UsuariosContract.Usuarios._CONTRASEÑA + " text)";

    public static String RutaDeEntrega = "CREATE TABLE " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + " ( " +
            RutaDeEntregaContract.RutaDeEntrega._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            RutaDeEntregaContract.RutaDeEntrega._FECHA + " date, " +
            RutaDeEntregaContract.RutaDeEntrega._CLIENTE + " text, " +
            RutaDeEntregaContract.RutaDeEntrega._NOMBRE + " text, " +
            RutaDeEntregaContract.RutaDeEntrega._DOMICILIO + " text, " +
            RutaDeEntregaContract.RutaDeEntrega._EFECTIVO + " float, " +
            RutaDeEntregaContract.RutaDeEntrega._CTACTE + " float, " +
            RutaDeEntregaContract.RutaDeEntrega._ESTADO + " int, " +
            RutaDeEntregaContract.RutaDeEntrega._ENVIADO + " int, " +
            RutaDeEntregaContract.RutaDeEntrega._ORDEN_VISITA + " int," +
            RutaDeEntregaContract.RutaDeEntrega._FINALIZADO + " int," +
            RutaDeEntregaContract.RutaDeEntrega._FLETERO + " text)";

    public static String PosGPS = "CREATE TABLE " + PosGPSContract.PosGPS.TABLE_NAME + " ( " +
            PosGPSContract.PosGPS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PosGPSContract.PosGPS._CLIENTE + " text, " +
            PosGPSContract.PosGPS._USUARIO + " text, " +
            PosGPSContract.PosGPS._ENVIADO + " int, " +
            PosGPSContract.PosGPS._ESTADO + " int, " +
            PosGPSContract.PosGPS._FECHA + " date, " +
            PosGPSContract.PosGPS._LATITUD + " float, " +
            PosGPSContract.PosGPS._LONGITUD + " float)";

    public static String Facturas = "CREATE TABLE " + FacturaContract.Factura.TABLE_NAME + " (" +
            FacturaContract.Factura._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FacturaContract.Factura._CLIENTE + " text, " +
            FacturaContract.Factura._TIPO + " text, " +
            FacturaContract.Factura._NUMERO + " text, " +
            FacturaContract.Factura._EMPRESA + " text," +
            FacturaContract.Factura._SUBEMPRESA + " text," +
            FacturaContract.Factura._CODIGO_RECHAZO + " text," +
            FacturaContract.Factura._ALICUOTA_IIBB + " float, " +
            FacturaContract.Factura._PERCEPCION_IIBB_TOTAL + " float, " +
            FacturaContract.Factura._IVA_BASICO_TOTAL + " float," +
            FacturaContract.Factura._IVA_ADICIONAL_TOTAL + " float, " +
            FacturaContract.Factura._IMPUESTO_INTERNO_TOTAL + " float," +
            FacturaContract.Factura._REPARTO + " text, " +
            FacturaContract.Factura._CONDICION_VENTA + " int, " +
            FacturaContract.Factura._SUBTOTAL + " float, " +
            FacturaContract.Factura._TOTAL + " float)";

    public static String MotivoRechazoFactura = "CREATE TABLE " + MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME + " (" +
            MotivoRechazoFacturaContract.MotivoRechazoFactura._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO + " text, " +
            MotivoRechazoFacturaContract.MotivoRechazoFactura._PRIORIDAD + " int, " +
            MotivoRechazoFacturaContract.MotivoRechazoFactura._DESCRIPCION + " text)";

    public static String ItemFactura = "CREATE TABLE " + ItemFacturaContract.ItemFactura.TABLE_NAME + " (" +
            ItemFacturaContract.ItemFactura._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ItemFacturaContract.ItemFactura._ARTICULO + " text, " +
            ItemFacturaContract.ItemFactura._DESCRIPCION + " text," +
            ItemFacturaContract.ItemFactura._CODIGO_BARRA_BULTO + " text," +
            ItemFacturaContract.ItemFactura._CODIGO_BARRA_UNIDAD + " text," +
            ItemFacturaContract.ItemFactura._MOTIVO_RECHAZO + " text," +
            ItemFacturaContract.ItemFactura._FACTURA_ID + " int," +
            ItemFacturaContract.ItemFactura._ID_ROW_REF_RECHAZO + " int," +
            ItemFacturaContract.ItemFactura._UNIDADES_POR_BULTO + " int," +
            ItemFacturaContract.ItemFactura._MINIMO_VENTA + " int," +
            ItemFacturaContract.ItemFactura._CANTIDAD + " double, " +
            ItemFacturaContract.ItemFactura._DESCUENTO_1 + " float," +
            ItemFacturaContract.ItemFactura._DESCUENTO_2 + " float," +
            ItemFacturaContract.ItemFactura._DESCUENTO_3 + " float," +
            ItemFacturaContract.ItemFactura._DESCUENTO_4 + " float," +
            ItemFacturaContract.ItemFactura._IMPORTE_FINAL + " float," +
            ItemFacturaContract.ItemFactura._IMPUESTO_INTERNO_UNITARIO + " float," +
            ItemFacturaContract.ItemFactura._PRECIO_FINAL_UNITARIO + " float," +
            ItemFacturaContract.ItemFactura._PRECIO_NETO_UNITARIO + " float," +
            ItemFacturaContract.ItemFactura._TASA_IVA + " float)";

}
