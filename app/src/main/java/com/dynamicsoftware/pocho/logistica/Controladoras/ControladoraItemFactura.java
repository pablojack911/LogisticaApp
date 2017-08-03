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
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.ItemFactura;

import java.util.ArrayList;

/**
 * Created by Pocho on 07/07/2017.
 */

public class ControladoraItemFactura extends Controladora
{
    private static final String TAG = "ControladoraItemFactura";

    public ControladoraItemFactura(Context context)
    {
        this.helper = DatabaseHelper.getInstance(context);
    }

    public Cursor obtenerCursorMercaderiaEnCamion(String usuario)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select " + ItemFacturaContract.ItemFactura._ARTICULO + ", " + ItemFacturaContract.ItemFactura._DESCRIPCION + ", sum(abs(" + ItemFacturaContract.ItemFactura._CANTIDAD + "))" +
                " from " + ItemFacturaContract.ItemFactura.TABLE_NAME + " itf" +
                " inner join " + FacturaContract.Factura.TABLE_NAME + " f on f." + FacturaContract.Factura._ID + "=itf." + ItemFacturaContract.ItemFactura._FACTURA_ID +
                " inner join " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + " ruta on ruta." + RutaDeEntregaContract.RutaDeEntrega._CLIENTE + "=f." + FacturaContract.Factura._CLIENTE +
                " where ruta." + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "<>? and ruta." + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "<>? and (f." + FacturaContract.Factura._CODIGO_RECHAZO + "<>? or itf." + ItemFacturaContract.ItemFactura._MOTIVO_RECHAZO + "<>?) and ruta." + RutaDeEntregaContract.RutaDeEntrega._FLETERO + "=? and itf." + ItemFacturaContract.ItemFactura._ARTICULO + "<>?" +
                " group by " + ItemFacturaContract.ItemFactura._ARTICULO;
        String[] args = {String.valueOf(ESTADO_ENTREGA.A_VISITAR.ordinal()), String.valueOf(ESTADO_ENTREGA.VOLVER_LUEGO.ordinal()), "", "", usuario, "0000000000001"};
        return db.rawQuery(query, args);
    }

    public ArrayList<ItemFactura> obtenerItemsFactura(int idFactura)
    {
        String where = ItemFacturaContract.ItemFactura._FACTURA_ID + "=?  and " + ItemFacturaContract.ItemFactura._ARTICULO + "<>?";
        String[] args = new String[]{String.valueOf(idFactura), "0000000000001"};
        Cursor cursor = this.obtenerCursor(where, args);
        ArrayList<ItemFactura> itemFacturaArrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            ItemFactura itemFactura = new ItemFactura();
            itemFactura.setFactura(idFactura);
            itemFactura.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._ID)));
            itemFactura.setArticulo(cursor.getString(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._ARTICULO)));
            itemFactura.setCodigoBarraBulto(cursor.getString(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._CODIGO_BARRA_BULTO)));
            itemFactura.setCodigoBarraUnidad(cursor.getString(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._CODIGO_BARRA_UNIDAD)));
            itemFactura.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._DESCRIPCION)));
            itemFactura.setMotivoRechazo(cursor.getString(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._MOTIVO_RECHAZO)));
            itemFactura.setPrecioNetoUnitario(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._PRECIO_NETO_UNITARIO)));
            itemFactura.setPrecioFinalUnitario(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._PRECIO_FINAL_UNITARIO)));
            itemFactura.setTasaIva(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._TASA_IVA)));
            itemFactura.setImpuestoInternoUnitario(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._IMPUESTO_INTERNO_UNITARIO)));
            itemFactura.setDescuento1(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._DESCUENTO_1)));
            itemFactura.setDescuento2(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._DESCUENTO_2)));
            itemFactura.setDescuento3(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._DESCUENTO_3)));
            itemFactura.setDescuento4(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._DESCUENTO_4)));
            itemFactura.setCantidad(cursor.getInt(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._CANTIDAD)));
            itemFactura.setImporteFinal(cursor.getDouble(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._IMPORTE_FINAL)));
            itemFacturaArrayList.add(itemFactura);
        }
        cursor.close();
        return itemFacturaArrayList;
    }

    @Override
    public long insertar(BaseModel item)
    {
        try
        {
            ItemFactura itemFactura = (ItemFactura) item;
            ContentValues contentValues = crearContentValues(itemFactura);
            return insertar(ItemFacturaContract.ItemFactura.TABLE_NAME, contentValues);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public long insertOrUpdate(ItemFactura itemFactura)
    {
        long ok = -1;
        try
        {
            String selection = ItemFacturaContract.ItemFactura._FACTURA_ID + " =? and " + ItemFacturaContract.ItemFactura._ARTICULO + "=?";
            String[] selectionArgs = {String.valueOf(itemFactura.getFactura()), itemFactura.getArticulo()};
            Cursor cursor = obtenerCursor(selection, selectionArgs);
            if (cursor.moveToNext())
            {
                Log.d("update-item", String.valueOf(actualizar(itemFactura)));
                ok = cursor.getInt(cursor.getColumnIndexOrThrow(ItemFacturaContract.ItemFactura._ID));
            }
            else
            {
                ok = insertar(itemFactura);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return ok;
    }

    @Override
    public int actualizar(BaseModel item)
    {
        try
        {
            ItemFactura itemFactura = (ItemFactura) item;
            ContentValues contentValues = crearContentValues(itemFactura);
            String where = ItemFacturaContract.ItemFactura._FACTURA_ID + " = ? and " + ItemFacturaContract.ItemFactura._ARTICULO + "=?";
            String[] args = {String.valueOf(itemFactura.getFactura()), itemFactura.getArticulo()};
            return actualizar(ItemFacturaContract.ItemFactura.TABLE_NAME, contentValues, where, args);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    protected Cursor obtenerCursor(String where, String[] args)
    {
        //ORDEN
        String sortOrder = ItemFacturaContract.ItemFactura._ARTICULO + " ASC";
        return obtenerCursor(ItemFacturaContract.ItemFactura.TABLE_NAME, crearProyeccionColumnas(), where, args, sortOrder);
    }

    @Override
    protected String[] crearProyeccionColumnas()
    {
        return new String[]{
                ItemFacturaContract.ItemFactura._ID,
                ItemFacturaContract.ItemFactura._ARTICULO,
                ItemFacturaContract.ItemFactura._CANTIDAD,
                ItemFacturaContract.ItemFactura._CODIGO_BARRA_BULTO,
                ItemFacturaContract.ItemFactura._CODIGO_BARRA_UNIDAD,
                ItemFacturaContract.ItemFactura._DESCRIPCION,
                ItemFacturaContract.ItemFactura._MOTIVO_RECHAZO,
                ItemFacturaContract.ItemFactura._DESCUENTO_1,
                ItemFacturaContract.ItemFactura._DESCUENTO_2,
                ItemFacturaContract.ItemFactura._DESCUENTO_3,
                ItemFacturaContract.ItemFactura._DESCUENTO_4,
                ItemFacturaContract.ItemFactura._FACTURA_ID,
                ItemFacturaContract.ItemFactura._ID_ROW_REF_RECHAZO,
                ItemFacturaContract.ItemFactura._IMPORTE_FINAL,
                ItemFacturaContract.ItemFactura._IMPUESTO_INTERNO_UNITARIO,
                ItemFacturaContract.ItemFactura._PRECIO_FINAL_UNITARIO,
                ItemFacturaContract.ItemFactura._PRECIO_NETO_UNITARIO,
                ItemFacturaContract.ItemFactura._TASA_IVA
        };
    }

    @Override
    protected ContentValues crearContentValues(BaseModel item)
    {
        ItemFactura itemFactura = (ItemFactura) item;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemFacturaContract.ItemFactura._ARTICULO, itemFactura.getArticulo());
        contentValues.put(ItemFacturaContract.ItemFactura._CANTIDAD, itemFactura.getCantidad());
        contentValues.put(ItemFacturaContract.ItemFactura._CODIGO_BARRA_BULTO, itemFactura.getCodigoBarraBulto());
        contentValues.put(ItemFacturaContract.ItemFactura._CODIGO_BARRA_UNIDAD, itemFactura.getCodigoBarraUnidad());
        contentValues.put(ItemFacturaContract.ItemFactura._DESCRIPCION, itemFactura.getDescripcion());
        contentValues.put(ItemFacturaContract.ItemFactura._MOTIVO_RECHAZO, itemFactura.getMotivoRechazo());
        contentValues.put(ItemFacturaContract.ItemFactura._DESCUENTO_1, itemFactura.getDescuento1());
        contentValues.put(ItemFacturaContract.ItemFactura._DESCUENTO_2, itemFactura.getDescuento2());
        contentValues.put(ItemFacturaContract.ItemFactura._DESCUENTO_3, itemFactura.getDescuento3());
        contentValues.put(ItemFacturaContract.ItemFactura._DESCUENTO_4, itemFactura.getDescuento4());
        contentValues.put(ItemFacturaContract.ItemFactura._FACTURA_ID, itemFactura.getFactura());
        contentValues.put(ItemFacturaContract.ItemFactura._ID_ROW_REF_RECHAZO, itemFactura.getIdRowRefRechazo());
        contentValues.put(ItemFacturaContract.ItemFactura._IMPORTE_FINAL, itemFactura.getImporteFinal());
        contentValues.put(ItemFacturaContract.ItemFactura._IMPUESTO_INTERNO_UNITARIO, itemFactura.getImpuestoInternoUnitario());
        contentValues.put(ItemFacturaContract.ItemFactura._PRECIO_FINAL_UNITARIO, itemFactura.getPrecioFinalUnitario());
        contentValues.put(ItemFacturaContract.ItemFactura._PRECIO_NETO_UNITARIO, itemFactura.getPrecioNetoUnitario());
        contentValues.put(ItemFacturaContract.ItemFactura._TASA_IVA, itemFactura.getTasaIva());
        return contentValues;
    }

    @Override
    protected int limpiar()
    {
        return 0;
    }

    public int actualizarMotivoRechazo(int idFactura, String codigoMotivoRechazo)
    {
        return 0;
    }
}
