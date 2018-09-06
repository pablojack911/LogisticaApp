package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.PosGPSContract;
import com.dynamicsoftware.pocho.logistica.DAO.DatabaseHelper;
import com.dynamicsoftware.pocho.logistica.Modelo.BaseModel;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.PosGPS;
import com.dynamicsoftware.pocho.logistica.Services.GPSIntentService;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Pocho on 21/04/2017.
 */

public class ControladoraPosGPS extends Controladora
{
    private static final String TAG = "ControladoraPosGPS";
    private Context contextGps;

    public ControladoraPosGPS(Context context)
    {
        helper = DatabaseHelper.getInstance(context);
        this.contextGps = context;
    }

    public ArrayList<PosGPS> obtenerPosicionesPendientes()
    {
        String where = PosGPSContract.PosGPS._ENVIADO + "= ? ";
        String[] args = {String.valueOf(0)};
        return obtenerPosiciones(where, args);
    }

    public PosGPS obtenerUltimaPosicion(String usuario)
    {
        String where = PosGPSContract.PosGPS._USUARIO + "= ? ";
        String[] args = {String.valueOf(usuario)};
        String sortOrder = PosGPSContract.PosGPS._ID + " DESC";
        Cursor cursor = obtenerCursor(where, args, sortOrder);
        if(cursor.moveToNext())
        {
            PosGPS posGPS = (PosGPS) parseObjectFromCursor(cursor);
            cursor.close();
            return posGPS;
        }
        return null;
    }

    private ArrayList<PosGPS> obtenerPosiciones(String where, String[] args)
    {
        Cursor cursor = this.obtenerCursor(where, args);
        ArrayList<PosGPS> posGPSArrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            PosGPS mPosGPS = (PosGPS) parseObjectFromCursor(cursor);
            posGPSArrayList.add(mPosGPS);
        }
        cursor.close();
        return posGPSArrayList;
    }

    private Object parseObjectFromCursor(Cursor cursor)
    {
        PosGPS mPosGPS = new PosGPS();
        mPosGPS.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._ID)));
        mPosGPS.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._USUARIO)));
        mPosGPS.setCliente(cursor.getString(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._CLIENTE)));
        mPosGPS.setEstadoEntrega(ESTADO_ENTREGA.parse(cursor.getInt(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._ESTADO))));
        mPosGPS.setFecha(Fecha.convertir(cursor.getString(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._FECHA))));
        mPosGPS.setLatitud(cursor.getFloat(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._LATITUD)));
        mPosGPS.setLongitud(cursor.getFloat(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._LONGITUD)));
        mPosGPS.setEnviado(cursor.getInt(cursor.getColumnIndexOrThrow(PosGPSContract.PosGPS._ENVIADO)));
        return mPosGPS;
    }

    public void creaIntentGrabar(String cliente, String usuario, ESTADO_ENTREGA estadoEntrega)
    {
        PosGPS pos = new PosGPS();
        pos.setCliente(cliente);
        pos.setUsuario(usuario);
        pos.setEstadoEntrega(estadoEntrega);
        pos.setFecha(new Date(0)); //sino palma
        Intent mServiceIntent = new Intent(contextGps, GPSIntentService.class);
//        Intent mServiceIntent = new Intent(contextGps, GpsPositionIntentService.class);
        mServiceIntent.putExtra(CONSTANTES.POSICION_GPS_KEY, pos);
        contextGps.startService(mServiceIntent);
    }

    @Override
    public long insertar(BaseModel item)
    {
        try
        {
            PosGPS posGPS = (PosGPS) item;
            ContentValues contentValues = crearContentValues(posGPS);
            return insertar(PosGPSContract.PosGPS.TABLE_NAME, contentValues);
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
            PosGPS posGPS = (PosGPS) item;
            // New value for one column
            ContentValues values = crearContentValues(posGPS);
            // Which row to update, based on the title
            String selection = PosGPSContract.PosGPS._ID + " = ?";
            String[] selectionArgs = {String.valueOf(posGPS.getId())};
            return actualizar(PosGPSContract.PosGPS.TABLE_NAME, values, selection, selectionArgs);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public int actualizarEnviado(int enviado, int id)
    {
        try
        {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(PosGPSContract.PosGPS._ENVIADO, enviado);
            // Which row to update, based on the title
            String selection = PosGPSContract.PosGPS._ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            return actualizar(PosGPSContract.PosGPS.TABLE_NAME, values, selection, selectionArgs);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public Cursor obtenerCursor(String where, String[] args, String sortOrder)
    {
        if (sortOrder.equals(""))
        {
            sortOrder = PosGPSContract.PosGPS._ID + " ASC";
        }
        return obtenerCursor(PosGPSContract.PosGPS.TABLE_NAME, crearProyeccionColumnas(), where, args, sortOrder);
    }

    protected Cursor obtenerCursor(String where, String[] args)
    {
        return obtenerCursor(where, args, "");
    }

    @Override
    protected String[] crearProyeccionColumnas()
    {
        //COLUMNAS A TRAER
        String[] projection = {
                PosGPSContract.PosGPS._ID, PosGPSContract.PosGPS._USUARIO, PosGPSContract.PosGPS._CLIENTE, PosGPSContract.PosGPS._ESTADO, PosGPSContract.PosGPS._FECHA, PosGPSContract.PosGPS._LATITUD, PosGPSContract.PosGPS._LONGITUD, PosGPSContract.PosGPS._ENVIADO
        };
        return projection;
    }

    @Override
    protected ContentValues crearContentValues(BaseModel item)
    {
        PosGPS posGPS = (PosGPS) item;
        ContentValues contentValues = new ContentValues();
        contentValues.put(PosGPSContract.PosGPS._CLIENTE, posGPS.getCliente());
        contentValues.put(PosGPSContract.PosGPS._USUARIO, posGPS.getUsuario());
        contentValues.put(PosGPSContract.PosGPS._LATITUD, posGPS.getLatitud());
        contentValues.put(PosGPSContract.PosGPS._LONGITUD, posGPS.getLongitud());
        contentValues.put(PosGPSContract.PosGPS._ESTADO, posGPS.getEstadoEntrega().ordinal());
        contentValues.put(PosGPSContract.PosGPS._ENVIADO, posGPS.getEnviado());
        contentValues.put(PosGPSContract.PosGPS._FECHA, Fecha.convertir(posGPS.getFecha()));
        return contentValues;
    }

    @Override
    public int limpiar()
    {
        try
        {
            int total = 0;
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "DELETE FROM " + PosGPSContract.PosGPS.TABLE_NAME + " WHERE " + PosGPSContract.PosGPS._ENVIADO + " =?";
            String[] args = {String.valueOf(1)};
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
}
