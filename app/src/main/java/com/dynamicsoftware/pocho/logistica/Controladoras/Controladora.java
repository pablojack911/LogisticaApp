package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.params.ColorSpaceTransform;

import com.dynamicsoftware.pocho.logistica.DAO.DatabaseHelper;
import com.dynamicsoftware.pocho.logistica.Modelo.BaseModel;

/**
 * Created by Pocho on 19/05/2017.
 */

public abstract class Controladora
{
    DatabaseHelper helper;

    protected abstract long insertar(BaseModel item);

    long insertar(String nombreTabla, ContentValues contentValues)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.insert(nombreTabla, null, contentValues);
    }

    protected abstract int actualizar(BaseModel item);

    int actualizar(String nombreTabla, ContentValues contentValues, String where, String[] args)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.update(nombreTabla, contentValues, where, args);
    }

    protected abstract Cursor obtenerCursor(String where, String[] args);

    protected abstract String[] crearProyeccionColumnas();

    Cursor obtenerCursor(String nombreTabla, String[] columnas, String where, String[] args, String sortOrder)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(nombreTabla, columnas, where, args, null, null, sortOrder);
    }

    protected abstract ContentValues crearContentValues(BaseModel item);

    protected abstract int limpiar();

    int limpiar(String nombreTabla, String where, String[] args)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(nombreTabla, where, args);
    }

//    public void cerrar()
//    {
//        helper.close();
//    }

    public void comenzarTransaccion()
    {
        helper.getWritableDatabase().beginTransactionNonExclusive();
    }

    public void finalizarTransaccion()
    {
        helper.getWritableDatabase().endTransaction();
    }

    public void transaccionExistosa()
    {
        helper.getWritableDatabase().setTransactionSuccessful();
    }

    int obtenerCantidadElementos(String nombreTabla, String where, String[] args)
    {
        int count = 0;
        String query = "select count(*) from " + nombreTabla;
        if (where.length() > 0 && args.length > 0)
        {
            query += where;
        }
        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, args);
            if (cursor.moveToNext())
            {
                count = cursor.getInt(0);
            }
            db.close();
            cursor.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return count;
    }
}
