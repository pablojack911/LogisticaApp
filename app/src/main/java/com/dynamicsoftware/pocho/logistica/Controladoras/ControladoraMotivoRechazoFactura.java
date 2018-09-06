package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.MotivoRechazoFacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.DatabaseHelper;
import com.dynamicsoftware.pocho.logistica.Modelo.BaseModel;
import com.dynamicsoftware.pocho.logistica.Modelo.MotivoRechazoFactura;

import java.util.ArrayList;

/**
 * Created by Pocho on 22/06/2017.
 */

public class ControladoraMotivoRechazoFactura extends Controladora
{
    final static String TAG = "CtrMotivoRechazoFactura";

    public ControladoraMotivoRechazoFactura(Context context)
    {
        helper = DatabaseHelper.getInstance(context);
    }

    @Override
    protected long insertar(BaseModel item)
    {
        try
        {
            MotivoRechazoFactura motivoRechazoFactura = (MotivoRechazoFactura) item;
            ContentValues contentValues = crearContentValues(motivoRechazoFactura);
            return insertar(MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME, contentValues);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    protected int actualizar(BaseModel item)
    {
        try
        {
            MotivoRechazoFactura motivoRechazoFactura = (MotivoRechazoFactura) item;
            ContentValues contentValues = crearContentValues(motivoRechazoFactura);
            String where = MotivoRechazoFacturaContract.MotivoRechazoFactura._ID + " = ?";
            String[] args = {String.valueOf(motivoRechazoFactura.getId())};
            return actualizar(MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME, contentValues, where, args);
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
        String sortOrder = MotivoRechazoFacturaContract.MotivoRechazoFactura._PRIORIDAD + " ASC";
        return obtenerCursor(MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME, crearProyeccionColumnas(), where, args, sortOrder);
    }

    @Override
    protected String[] crearProyeccionColumnas()
    {
        return new String[]{
                MotivoRechazoFacturaContract.MotivoRechazoFactura._ID, MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO, MotivoRechazoFacturaContract.MotivoRechazoFactura._DESCRIPCION, MotivoRechazoFacturaContract.MotivoRechazoFactura._PRIORIDAD
        };
    }

    @Override
    protected ContentValues crearContentValues(BaseModel item)
    {
        MotivoRechazoFactura motivoRechazoFactura = (MotivoRechazoFactura) item;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MotivoRechazoFacturaContract.MotivoRechazoFactura._ID, motivoRechazoFactura.getId());
        contentValues.put(MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO, motivoRechazoFactura.getCodigo());
        contentValues.put(MotivoRechazoFacturaContract.MotivoRechazoFactura._DESCRIPCION, motivoRechazoFactura.getDescripcion());
        contentValues.put(MotivoRechazoFacturaContract.MotivoRechazoFactura._PRIORIDAD, motivoRechazoFactura.getPrioridad());
        return contentValues;
    }

    @Override
    protected int limpiar()
    {
        return 0;
    }

    private ArrayList<MotivoRechazoFactura> obtenerMotivos(String where, String[] args)
    {
        Cursor cursor = this.obtenerCursor(where, args);
        ArrayList<MotivoRechazoFactura> motivoRechazoFacturaArrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            MotivoRechazoFactura motivoRechazoFactura = new MotivoRechazoFactura();
            motivoRechazoFactura.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MotivoRechazoFacturaContract.MotivoRechazoFactura._ID)));
            motivoRechazoFactura.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO)));
            motivoRechazoFactura.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(MotivoRechazoFacturaContract.MotivoRechazoFactura._DESCRIPCION)));
            motivoRechazoFactura.setPrioridad(cursor.getInt(cursor.getColumnIndexOrThrow(MotivoRechazoFacturaContract.MotivoRechazoFactura._PRIORIDAD)));
            motivoRechazoFacturaArrayList.add(motivoRechazoFactura);
        }
        cursor.close();
        return motivoRechazoFacturaArrayList;
    }

    public Cursor obtenerCursorMotivos()
    {
        return obtenerCursor(null, null);
    }

    public String obtenerMotivo(String codigoRechazo)
    {
        String where = MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO + "=?";
        String[] args = {codigoRechazo};
        ArrayList<MotivoRechazoFactura> motivos = obtenerMotivos(where, args);
        String motivo = "";
        if (motivos != null && motivos.size() > 0)
        {
            motivo = motivos.get(0).getDescripcion();
        }
        return motivo;
    }

    public ArrayList<MotivoRechazoFactura> obtenerMotivos()
    {
        return obtenerMotivos(null, null);
    }
}
