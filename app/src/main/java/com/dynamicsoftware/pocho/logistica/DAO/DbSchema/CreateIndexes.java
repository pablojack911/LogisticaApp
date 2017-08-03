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

public class CreateIndexes
{
    public static String Usuarios_id = "CREATE INDEX IF NOT EXISTS Usuarios_id on " +
            UsuariosContract.Usuarios.TABLE_NAME + "(" + UsuariosContract.Usuarios._ID + " asc)";

    public static String Usuarios_usuario = "CREATE INDEX IF NOT EXISTS Usuarios_usuario on " +
            UsuariosContract.Usuarios.TABLE_NAME + "(" + UsuariosContract.Usuarios._USUARIO + " asc)";

    public static String RutaDeEntrega_id = "CREATE INDEX IF NOT EXISTS RutaDeEntrega_id on " +
            RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + "(" + RutaDeEntregaContract.RutaDeEntrega._ID + " asc)";

    public static String RutaDeEntrega_cliente = "CREATE INDEX IF NOT EXISTS RutaDeEntrega_cliente on " +
            RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + "(" + RutaDeEntregaContract.RutaDeEntrega._CLIENTE + " asc)";

    public static String RutaDeEntrega_enviado = "CREATE INDEX IF NOT EXISTS RutaDeEntrega_enviado on " +
            RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + "(" + RutaDeEntregaContract.RutaDeEntrega._ENVIADO + " asc)";

    public static String RutaDeEntrega_finalizado = "CREATE INDEX IF NOT EXISTS RutaDeEntrega_finalizado on " +
            RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + "(" + RutaDeEntregaContract.RutaDeEntrega._FINALIZADO + " asc)";

    public static String RutaDeEntrega_fletero = "CREATE INDEX IF NOT EXISTS RutaDeEntrega_fletero on " +
            RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + "(" + RutaDeEntregaContract.RutaDeEntrega._FLETERO + " asc)";

    public static String PosGPS_id = "CREATE INDEX IF NOT EXISTS PosGPS_id on " +
            PosGPSContract.PosGPS.TABLE_NAME + "(" + PosGPSContract.PosGPS._ID + " asc)";

    public static String PosGPS_cliente = "CREATE INDEX IF NOT EXISTS PosGPS_cliente on " +
            PosGPSContract.PosGPS.TABLE_NAME + "(" + PosGPSContract.PosGPS._CLIENTE + " asc)";

    public static String PosGPS_enviado = "CREATE INDEX IF NOT EXISTS PosGPS_enviado on " +
            PosGPSContract.PosGPS.TABLE_NAME + "(" + PosGPSContract.PosGPS._ENVIADO + " asc)";

    public static String Facturas_id = "CREATE INDEX IF NOT EXISTS Facturas_id on " +
            FacturaContract.Factura.TABLE_NAME + "(" + FacturaContract.Factura._ID + " asc)";

    public static String Facturas_cliente = "CREATE INDEX IF NOT EXISTS Facturas_cliente on " +
            FacturaContract.Factura.TABLE_NAME + " (" + FacturaContract.Factura._CLIENTE + " asc)";

    public static String Facturas_reparto = "CREATE INDEX IF NOT EXISTS Facturas_reparto on " +
            FacturaContract.Factura.TABLE_NAME + " (" + FacturaContract.Factura._REPARTO + " asc)";

    public static String Facturas_tipo = "CREATE INDEX IF NOT EXISTS Facturas_tipo on " +
            FacturaContract.Factura.TABLE_NAME + " (" + FacturaContract.Factura._TIPO + " asc)";

    public static String Facturas_numero = "CREATE INDEX IF NOT EXISTS Factura_numero on " +
            FacturaContract.Factura.TABLE_NAME + " (" + FacturaContract.Factura._NUMERO + " asc)";

    public static String Facturas_empresa = "CREATE INDEX IF NOT EXISTS Factura_empresa on " +
            FacturaContract.Factura.TABLE_NAME + " (" + FacturaContract.Factura._EMPRESA + " asc)";

    public static String Facturas_subempresa = "CREATE INDEX IF NOT EXISTS Factura_subempresa on " +
            FacturaContract.Factura.TABLE_NAME + " (" + FacturaContract.Factura._SUBEMPRESA + " asc)";

    public static String MotivoRechazoFactura_id = "CREATE INDEX IF NOT EXISTS MotivoRechazoFactura_id on " +
            MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME + " (" + MotivoRechazoFacturaContract.MotivoRechazoFactura._ID + " asc)";

    public static String MotivoRechazoFactura_codigo = "CREATE INDEX IF NOT EXISTS MotivoRechazoFactura_codigo on " +
            MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME + " (" + MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO + " asc)";

    public static String MotivoRechazoFactura_descripcion = "CREATE INDEX IF NOT EXISTS MotivoRechazoFactura_descripcion on " +
            MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME + " (" + MotivoRechazoFacturaContract.MotivoRechazoFactura._DESCRIPCION + " asc)";

    public static String ItemFactura_articulo = "CREATE INDEX IF NOT EXISTS Factura_articulo on " +
            ItemFacturaContract.ItemFactura.TABLE_NAME + " (" + ItemFacturaContract.ItemFactura._ARTICULO + " asc)";

    public static String ItemFactura_factura = "CREATE INDEX IF NOT EXISTS ItemFactura_factura on " +
            ItemFacturaContract.ItemFactura.TABLE_NAME + " (" + ItemFacturaContract.ItemFactura._FACTURA_ID + " asc)";

    public static String ItemFactura_codigoBarraBulto = "CREATE INDEX IF NOT EXISTS ItemFactura_codigoBarraBulto on " +
            ItemFacturaContract.ItemFactura.TABLE_NAME + " (" + ItemFacturaContract.ItemFactura._CODIGO_BARRA_BULTO + " asc)";

    public static String ItemFactura_codigoBarraUnidad = "CREATE INDEX IF NOT EXISTS ItemFactura_codigoBarraUnidad on " +
            ItemFacturaContract.ItemFactura.TABLE_NAME + " (" + ItemFacturaContract.ItemFactura._CODIGO_BARRA_UNIDAD + " asc)";

    public static String ItemFactura_descripcion = "CREATE INDEX IF NOT EXISTS ItemFactura_descripcion on " +
            ItemFacturaContract.ItemFactura.TABLE_NAME + " (" + ItemFacturaContract.ItemFactura._DESCRIPCION + " asc)";

    public static String ItemFactura_idRowRefRechazo = "CREATE INDEX IF NOT EXISTS ItemFactura_idRowRefRechazo on " +
            ItemFacturaContract.ItemFactura.TABLE_NAME + " (" + ItemFacturaContract.ItemFactura._ID_ROW_REF_RECHAZO + " asc)";
}
