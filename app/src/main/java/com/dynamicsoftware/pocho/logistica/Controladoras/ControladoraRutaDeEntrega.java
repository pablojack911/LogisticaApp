package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.RutaDeEntregaContract;
import com.dynamicsoftware.pocho.logistica.DAO.DatabaseHelper;
import com.dynamicsoftware.pocho.logistica.Modelo.BaseModel;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.RutaDeEntrega;

import java.util.ArrayList;

/**
 * Created by Pocho on 14/04/2017.
 */

public class ControladoraRutaDeEntrega extends Controladora
{
    private static final String TAG = "CtrlRutaDeEntrega";

    private static final String whereEntregaPendiente = RutaDeEntregaContract.RutaDeEntrega._FLETERO + " =? and (" + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "= ? or " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "= ?)";
    private static final String whereEntregaEntregados = RutaDeEntregaContract.RutaDeEntrega._FLETERO + " =? and " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "<> ? and " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "<> ?";

    public ControladoraRutaDeEntrega(Context context)
    {
        helper = DatabaseHelper.getInstance(context);
    }

    public ArrayList<RutaDeEntrega> obtenerRutaDeEntregaEntregados(String fletero)
    {
//        String where = RutaDeEntregaContract.RutaDeEntrega._FLETERO + " =? and " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "<> ? and " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "<> ?";
        String[] args = {fletero, String.valueOf(ESTADO_ENTREGA.VOLVER_LUEGO.ordinal()), String.valueOf(ESTADO_ENTREGA.A_VISITAR.ordinal())};
        return obtenerRutasDeEntrega(whereEntregaEntregados, args);
    }

    public ArrayList<RutaDeEntrega> obtenerRutaDeEntregaPendiente(String fletero)
    {
//        String where = RutaDeEntregaContract.RutaDeEntrega._FLETERO + " =? and (" + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "= ? or " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "= ?)";
        String[] args = {fletero, String.valueOf(ESTADO_ENTREGA.VOLVER_LUEGO.ordinal()), String.valueOf(ESTADO_ENTREGA.A_VISITAR.ordinal())};
        return obtenerRutasDeEntrega(whereEntregaPendiente, args);
    }

    private ArrayList<RutaDeEntrega> obtenerRutasDeEntrega(String where, String[] args)
    {
        Cursor cursor = this.obtenerCursor(where, args);
        ArrayList<RutaDeEntrega> mRutas = new ArrayList<>();
        while (cursor.moveToNext())
        {
            RutaDeEntrega mRutaDeEntrega = new RutaDeEntrega();
            mRutaDeEntrega.setId(cursor.getInt(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._ID)));
            mRutaDeEntrega.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._NOMBRE)));
            mRutaDeEntrega.setCliente(cursor.getString(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._CLIENTE)));
            mRutaDeEntrega.setCtacte(cursor.getDouble(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._CTACTE)));
            mRutaDeEntrega.setEfectivo(cursor.getDouble(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._EFECTIVO)));
            mRutaDeEntrega.setDomicilio(cursor.getString(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._DOMICILIO)));
            mRutaDeEntrega.setEnviado(cursor.getInt(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._ENVIADO)) == 1);
            mRutaDeEntrega.setOrdenVisita(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._ORDEN_VISITA));
            mRutaDeEntrega.setEstadoEntrega(ESTADO_ENTREGA.parse(cursor.getInt(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._ESTADO))));
            mRutaDeEntrega.setFinalizado(cursor.getInt(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._FINALIZADO)));
            mRutaDeEntrega.setFletero(cursor.getString(cursor.getColumnIndexOrThrow(RutaDeEntregaContract.RutaDeEntrega._FLETERO)));
            mRutas.add(mRutaDeEntrega);
        }
        cursor.close();
        return mRutas;
    }

    public int actualizaEstado(ESTADO_ENTREGA estado, int id)
    {
        try
        {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(RutaDeEntregaContract.RutaDeEntrega._ESTADO, estado.ordinal());

            // Which row to update, based on the title
            String selection = RutaDeEntregaContract.RutaDeEntrega._ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            return actualizar(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, values, selection, selectionArgs);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public long insertOrUpdate(RutaDeEntrega rutaDeEntrega)
    {
        long ok;
        String selection = RutaDeEntregaContract.RutaDeEntrega._FLETERO + " =? and " + RutaDeEntregaContract.RutaDeEntrega._CLIENTE + "=?";
        String[] selectionArgs = {rutaDeEntrega.getFletero(), rutaDeEntrega.getCliente()};
        if (obtenerCursor(selection, selectionArgs).moveToNext())
        {
            ok = actualizar(rutaDeEntrega);
        }
        else
        {
            ok = insertar(rutaDeEntrega);
        }
        return ok;
    }

    @Override
    protected long insertar(BaseModel item)
    {
        try
        {
            RutaDeEntrega rutaDeEntrega = (RutaDeEntrega) item;
            ContentValues contentValues = crearContentValues(rutaDeEntrega);
            return insertar(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, contentValues);
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
            RutaDeEntrega rutaDeEntrega = (RutaDeEntrega) item;
            ContentValues contentValues = crearContentValues(rutaDeEntrega);
            String where = RutaDeEntregaContract.RutaDeEntrega._ID + " = ?";
            String[] args = {String.valueOf(rutaDeEntrega.getId())};
            return actualizar(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, contentValues, where, args);
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
        String sortOrder = RutaDeEntregaContract.RutaDeEntrega._ORDEN_VISITA + " ASC";
        return obtenerCursor(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, crearProyeccionColumnas(), where, args, sortOrder);
    }

    @Override
    protected String[] crearProyeccionColumnas()
    {
        //COLUMNAS A TRAER
        String[] projection = {RutaDeEntregaContract.RutaDeEntrega._ID,
                RutaDeEntregaContract.RutaDeEntrega._CLIENTE,
                RutaDeEntregaContract.RutaDeEntrega._CTACTE,
                RutaDeEntregaContract.RutaDeEntrega._DOMICILIO,
                RutaDeEntregaContract.RutaDeEntrega._EFECTIVO,
                RutaDeEntregaContract.RutaDeEntrega._ENVIADO,
                RutaDeEntregaContract.RutaDeEntrega._ESTADO,
                RutaDeEntregaContract.RutaDeEntrega._NOMBRE,
                RutaDeEntregaContract.RutaDeEntrega._ORDEN_VISITA,
                RutaDeEntregaContract.RutaDeEntrega._FINALIZADO,
                RutaDeEntregaContract.RutaDeEntrega._FLETERO};
        return projection;
    }

    @Override
    protected ContentValues crearContentValues(BaseModel item)
    {
        RutaDeEntrega rutaDeEntrega = (RutaDeEntrega) item;
        ContentValues contentValues = new ContentValues();
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._CLIENTE, rutaDeEntrega.getCliente());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._CTACTE, rutaDeEntrega.getCtacte());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._DOMICILIO, rutaDeEntrega.getDomicilio());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._EFECTIVO, rutaDeEntrega.getEfectivo());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._ENVIADO, rutaDeEntrega.isEnviado());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._ESTADO, rutaDeEntrega.getEstadoEntrega().ordinal());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._NOMBRE, rutaDeEntrega.getNombre());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._ORDEN_VISITA, rutaDeEntrega.getOrdenVisita());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._FINALIZADO, rutaDeEntrega.getFinalizado());
        contentValues.put(RutaDeEntregaContract.RutaDeEntrega._FLETERO, rutaDeEntrega.getFletero());
        return contentValues;
    }

    @Override
    public int limpiar()
    {
        try
        {
            return limpiar(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, null, null);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return 0;
    }

    public int limpiar(String usuario)
    {
        String where = RutaDeEntregaContract.RutaDeEntrega._FLETERO + "=?";
        String[] args = {usuario};
        try
        {
            return limpiar(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, where, args);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return 0;
    }

    public int finalizarReparto(String fletero)
    {
        try
        {
            String where = RutaDeEntregaContract.RutaDeEntrega._FLETERO + "= ?";
            String[] args = {fletero};
            ContentValues contentValues = new ContentValues();
            contentValues.put(RutaDeEntregaContract.RutaDeEntrega._FINALIZADO, 1);
            return actualizar(RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME, contentValues, where, args);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return 0;
    }

    public int obtenerCantidadElementosVisitar(String fletero)
    {
        String tabla = RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME;
//        String where = " where " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "= ? or " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + "= ?";
        String where = " where " + whereEntregaPendiente;
        String[] args = {fletero, String.valueOf(ESTADO_ENTREGA.VOLVER_LUEGO.ordinal()), String.valueOf(ESTADO_ENTREGA.A_VISITAR.ordinal())};
        return obtenerCantidadElementos(tabla, where, args);
    }

    public int obtenerCantidadElementosVisitados(String fletero)
    {
        String tabla = RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME;
//        String where = " where " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + " <> ? or " + RutaDeEntregaContract.RutaDeEntrega._ESTADO + " <> ?";
        String where = " where " + whereEntregaEntregados;
        String[] args = {fletero, String.valueOf(ESTADO_ENTREGA.VOLVER_LUEGO.ordinal()), String.valueOf(ESTADO_ENTREGA.A_VISITAR.ordinal())};
        return obtenerCantidadElementos(tabla, where, args);
    }

//    public ArrayList<RutaDeEntrega> obtenerRuta(String query)
//    {
//        String selection = RutaDeEntregaContract.RutaDeEntrega._CLIENTE + " like ? OR " + RutaDeEntregaContract.RutaDeEntrega._NOMBRE + " like ?";
//        String[] selectionArgs = {query + "%", "%" + query + "%"};
//        return obtenerRutasDeEntrega(selection, selectionArgs);
//    }

//    public void grabarLista(ArrayList<RutaDeEntrega> rutas)
//    {
//        for (RutaDeEntrega ruta : rutas)
//        {
//            insertar(ruta);
//        }
//    }

}
