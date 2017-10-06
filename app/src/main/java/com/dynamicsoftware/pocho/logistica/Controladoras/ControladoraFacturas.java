package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.FacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.ItemFacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.RutaDeEntregaContract;
import com.dynamicsoftware.pocho.logistica.DAO.DatabaseHelper;
import com.dynamicsoftware.pocho.logistica.Modelo.BaseModel;
import com.dynamicsoftware.pocho.logistica.Modelo.CONDICION_VENTA;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.Factura;

import java.util.ArrayList;

/**
 * Created by Pocho on 19/05/2017.
 */

public class ControladoraFacturas extends Controladora
{
    static final String TAG = "ControladoraFacturas";

    public ControladoraFacturas(Context context)
    {
        helper = DatabaseHelper.getInstance(context);
    }

    @Override
    public long insertar(BaseModel item)
    {
        try
        {
            Factura factura = (Factura) item;
            ContentValues contentValues = crearContentValues(factura);
            return insertar(FacturaContract.Factura.TABLE_NAME, contentValues);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public int actualizar(BaseModel item)
    {
        try
        {
            Factura factura = (Factura) item;
            ContentValues contentValues = crearContentValues(factura);
            String where = FacturaContract.Factura._ID + " = ?";
            String[] args = {String.valueOf(factura.getId())};
            return actualizar(FacturaContract.Factura.TABLE_NAME, contentValues, where, args);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public long insertOrUpdate(Factura factura)
    {
        long ok;
        String selection = FacturaContract.Factura._CLIENTE + " =? and " + FacturaContract.Factura._TIPO + "=? and " + FacturaContract.Factura._NUMERO + "=? and " + FacturaContract.Factura._EMPRESA + "=? and " + FacturaContract.Factura._SUBEMPRESA + "=? and " + FacturaContract.Factura._REPARTO + "=?";
        String[] selectionArgs = {factura.getCliente(), factura.getTipo(), factura.getNumero(), factura.getEmpresa(), factura.getSubempresa(), factura.getReparto()};
        Cursor cursor = obtenerCursor(selection, selectionArgs);
        if (cursor.moveToNext())
        {
            actualizar(factura);
            ok = cursor.getInt(cursor.getColumnIndexOrThrow(FacturaContract.Factura._ID));
        }
        else
        {
            ok = insertar(factura);
        }
        return ok;
    }

    public ArrayList<Factura> obtenerFacturas(String cliente)
    {
        String where = FacturaContract.Factura._CLIENTE + "=?";
        String[] args = new String[]{cliente};
        Cursor cursor = this.obtenerCursor(where, args);
        ArrayList<Factura> facturaArrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Factura factura = new Factura();

            factura.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FacturaContract.Factura._ID)));
            factura.setCliente(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._CLIENTE)));
            factura.setEmpresa(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._EMPRESA)));
            factura.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._TIPO)));
            factura.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._NUMERO)));
            factura.setSubempresa(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._SUBEMPRESA)));
            factura.setAlicuotaIIBB(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._ALICUOTA_IIBB)));
            factura.setImpuestoInterno(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._IMPUESTO_INTERNO_TOTAL)));
            factura.setIvaAdicional(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._IVA_ADICIONAL_TOTAL)));
            factura.setIvaBasico(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._IVA_BASICO_TOTAL)));
            factura.setPercepcionIIBB(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._PERCEPCION_IIBB_TOTAL)));
            factura.setReparto(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._REPARTO)));
            factura.setSubtotal(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._SUBTOTAL)));
            factura.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(FacturaContract.Factura._TOTAL)));
            factura.setCodigoRechazo(cursor.getString(cursor.getColumnIndexOrThrow(FacturaContract.Factura._CODIGO_RECHAZO)));
            factura.setCondicionVenta(CONDICION_VENTA.parse(cursor.getInt(cursor.getColumnIndexOrThrow(FacturaContract.Factura._CONDICION_VENTA))));


            facturaArrayList.add(factura);
        }
        cursor.close();
        return facturaArrayList;
    }

    @Override
    protected Cursor obtenerCursor(String where, String[] args)
    {
        String sortOrder = FacturaContract.Factura._ID + " ASC";
        return obtenerCursor(FacturaContract.Factura.TABLE_NAME, crearProyeccionColumnas(), where, args, sortOrder);
    }

    @Override
    protected String[] crearProyeccionColumnas()
    {
        //COLUMNAS A TRAER
        String[] projection = {FacturaContract.Factura._ID,
                FacturaContract.Factura._CLIENTE,
                FacturaContract.Factura._TIPO,
                FacturaContract.Factura._EMPRESA,
                FacturaContract.Factura._NUMERO,
                FacturaContract.Factura._SUBEMPRESA,
                FacturaContract.Factura._ALICUOTA_IIBB,
                FacturaContract.Factura._IMPUESTO_INTERNO_TOTAL,
                FacturaContract.Factura._IVA_ADICIONAL_TOTAL,
                FacturaContract.Factura._IVA_BASICO_TOTAL,
                FacturaContract.Factura._PERCEPCION_IIBB_TOTAL,
                FacturaContract.Factura._REPARTO,
                FacturaContract.Factura._SUBTOTAL,
                FacturaContract.Factura._CONDICION_VENTA,
                FacturaContract.Factura._CODIGO_RECHAZO,
                FacturaContract.Factura._TOTAL};
        return projection;
    }

    @Override
    protected ContentValues crearContentValues(BaseModel item)
    {
        Factura factura = (Factura) item;
        ContentValues contentValues = new ContentValues();
        contentValues.put(FacturaContract.Factura._CLIENTE, factura.getCliente());
        contentValues.put(FacturaContract.Factura._TIPO, factura.getTipo());
        contentValues.put(FacturaContract.Factura._EMPRESA, factura.getEmpresa());
        contentValues.put(FacturaContract.Factura._NUMERO, factura.getNumero());
        contentValues.put(FacturaContract.Factura._SUBEMPRESA, factura.getSubempresa());
        contentValues.put(FacturaContract.Factura._ALICUOTA_IIBB, factura.getAlicuotaIIBB());
        contentValues.put(FacturaContract.Factura._IMPUESTO_INTERNO_TOTAL, factura.getImpuestoInterno());
        contentValues.put(FacturaContract.Factura._IVA_ADICIONAL_TOTAL, factura.getIvaAdicional());
        contentValues.put(FacturaContract.Factura._IVA_BASICO_TOTAL, factura.getIvaBasico());
        contentValues.put(FacturaContract.Factura._PERCEPCION_IIBB_TOTAL, factura.getPercepcionIIBB());
        contentValues.put(FacturaContract.Factura._REPARTO, factura.getReparto());
        contentValues.put(FacturaContract.Factura._SUBTOTAL, factura.getSubtotal());
        contentValues.put(FacturaContract.Factura._TOTAL, factura.getTotal());
        contentValues.put(FacturaContract.Factura._CODIGO_RECHAZO, factura.getCodigoRechazo());
        contentValues.put(FacturaContract.Factura._CONDICION_VENTA, factura.getCondicionVenta().ordinal());
        return contentValues;
    }

    public int limpiar(String usuario)
    {
        try
        {
            int total = 0;
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "DELETE FROM " + FacturaContract.Factura.TABLE_NAME +
                    " WHERE " + FacturaContract.Factura._CLIENTE + " IN" +
                    " (SELECT DISTINCT " + RutaDeEntregaContract.RutaDeEntrega._CLIENTE +
                    " FROM " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME +
                    " WHERE " + RutaDeEntregaContract.RutaDeEntrega._FLETERO + "=?)";
            String[] args = {usuario};
            Cursor cursor = db.rawQuery(query, args);
            if (cursor.moveToNext())
            {
                total = cursor.getInt(0);
                cursor.close();
            }
            return total;
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    protected int limpiar()
    {
        return 0;
    }

    public int actualizarFacturas(String cliente, String codigoRechazo)
    {
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FacturaContract.Factura._CODIGO_RECHAZO, codigoRechazo);
            String where = FacturaContract.Factura._CLIENTE + " = ?";
            String[] args = {String.valueOf(cliente)};
            return actualizar(FacturaContract.Factura.TABLE_NAME, contentValues, where, args);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public double totalEfectivoEntregaTotalParaRendir(String usuario)
    {
        double total = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select sum(f." + FacturaContract.Factura._TOTAL + ")"
                + " from " + FacturaContract.Factura.TABLE_NAME + " f"
                + " inner join " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + " ruta on ruta." + RutaDeEntregaContract.RutaDeEntrega._CLIENTE + "= f." + FacturaContract.Factura._CLIENTE
                + " where f." + FacturaContract.Factura._CODIGO_RECHAZO + "=? and f." + FacturaContract.Factura._CONDICION_VENTA + "=? and ruta." + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "=? and ruta." + RutaDeEntregaContract.RutaDeEntrega._FLETERO + "=?";
        String[] args = {"", String.valueOf(CONDICION_VENTA.EFECTIVO.ordinal()), String.valueOf(ESTADO_ENTREGA.ENTREGA_TOTAL.ordinal()), usuario};
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToNext())
        {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }

    public double totalEfectivoEntregaParcialParaRendir(String usuario)
    {
        double finalFacturas;
        double finalRechazado;
        finalFacturas = totalEntregaParcial(usuario);
        finalRechazado = totalRechazadoEntregaParcial(usuario);
        return finalFacturas + finalRechazado;
    }

    private double totalEntregaParcial(String usuario)
    {
        double total = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select sum(f." + FacturaContract.Factura._TOTAL + ")"
                + " from " + FacturaContract.Factura.TABLE_NAME + " f"
                + " inner join " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + " ruta on ruta." + RutaDeEntregaContract.RutaDeEntrega._CLIENTE + "= f." + FacturaContract.Factura._CLIENTE
                + " where f." + FacturaContract.Factura._CONDICION_VENTA + "=? and f." + FacturaContract.Factura._CODIGO_RECHAZO + "=? and ruta." + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "=? and ruta." + RutaDeEntregaContract.RutaDeEntrega._FLETERO + "=?";
        String[] args = {String.valueOf(CONDICION_VENTA.EFECTIVO.ordinal()), "", String.valueOf(ESTADO_ENTREGA.ENTREGA_PARCIAL.ordinal()), usuario};
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToNext())
        {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }

    private double totalRechazadoEntregaParcial(String usuario)
    {
        double total = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        /*
            select sum(importeFinal)
            from itemFactura
            inner join facturas on itemFactura.factura_id=facturas._id
            inner join rutaDeEntrega on rutaDeEntrega.cliente = facturas.cliente
            where itemFactura.idRowRef>0 and facturas.condicionVenta=0 and rutaDeEntrega.estado=2 and rutaDeEntrega.fletero='F0030'
        */
        String query = "select sum(itf." + ItemFacturaContract.ItemFactura._IMPORTE_FINAL + ")"
                + " from " + ItemFacturaContract.ItemFactura.TABLE_NAME + " itf"
                + " inner join " + FacturaContract.Factura.TABLE_NAME + " f on f." + FacturaContract.Factura._ID + " = itf." + ItemFacturaContract.ItemFactura._FACTURA_ID
                + " inner join " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + " ruta on ruta." + RutaDeEntregaContract.RutaDeEntrega._CLIENTE + "= f." + FacturaContract.Factura._CLIENTE
                + " where itf." + ItemFacturaContract.ItemFactura._ID_ROW_REF_RECHAZO + ">? and f." + FacturaContract.Factura._CONDICION_VENTA + "=? and ruta." + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "=? and ruta." + RutaDeEntregaContract.RutaDeEntrega._FLETERO + "=?";
        String[] args = {String.valueOf(0), String.valueOf(CONDICION_VENTA.EFECTIVO.ordinal()), String.valueOf(ESTADO_ENTREGA.ENTREGA_PARCIAL.ordinal()), usuario};
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToNext())
        {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }
}
