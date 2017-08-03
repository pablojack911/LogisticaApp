package com.dynamicsoftware.pocho.logistica.DAO.DbSchema;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.FacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.MotivoRechazoFacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.PosGPSContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.RutaDeEntregaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.UsuariosContract;

/**
 * Created by Pocho on 13/04/2017.
 */

public class DeleteTables
{
    public static String Usuarios = "DROP TABLE IF EXISTS " + UsuariosContract.Usuarios.TABLE_NAME;
    public static String RutaDeEntrega = "DROP TABLE IF EXISTS " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME;
    public static String PosGPS = "DROP TABLE IF EXISTS " + PosGPSContract.PosGPS.TABLE_NAME;
    public static String Facturas = "DROP TABLE IF EXISTS " + FacturaContract.Factura.TABLE_NAME;
    public static String MotivoRechazoFactura ="DROP TABLE IF EXISTS "+ MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME;
}
