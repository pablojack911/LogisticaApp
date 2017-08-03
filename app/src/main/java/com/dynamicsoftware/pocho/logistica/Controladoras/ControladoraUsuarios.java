package com.dynamicsoftware.pocho.logistica.Controladoras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.UsuariosContract;
import com.dynamicsoftware.pocho.logistica.DAO.DatabaseHelper;
import com.dynamicsoftware.pocho.logistica.Modelo.Usuarios;

import java.util.ArrayList;

/**
 * Created by Pocho on 13/04/2017.
 */

public class ControladoraUsuarios
{
    DatabaseHelper helper;

    public ControladoraUsuarios(Context context)
    {
        helper = DatabaseHelper.getInstance(context);
//        String path = context.getDatabasePath(helper.getDatabaseName()).getPath();
    }

    public long insertar(Usuarios usuario)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UsuariosContract.Usuarios._USUARIO, usuario.getUsuario());
        values.put(UsuariosContract.Usuarios._USUARIO, usuario.getContraseña());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(UsuariosContract.Usuarios.TABLE_NAME, null, values);
        return newRowId;
    }

    public ArrayList<Usuarios> obtenerUsuarios()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        //COLUMNAS A TRAER
        String[] projection = {UsuariosContract.Usuarios._ID,
                UsuariosContract.Usuarios._USUARIO,
                UsuariosContract.Usuarios._CONTRASEÑA
        };
        //ORDEN
        String sortOrder = UsuariosContract.Usuarios._USUARIO + " DESC";

        Cursor cursor = db.query(
                UsuariosContract.Usuarios.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        ArrayList<Usuarios> mUsuarios = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Usuarios usuarios = new Usuarios();
            usuarios.setId(cursor.getInt(cursor.getColumnIndexOrThrow(UsuariosContract.Usuarios._ID)));
            usuarios.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.Usuarios._USUARIO)));
            usuarios.setContraseña(cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.Usuarios._CONTRASEÑA)));
            mUsuarios.add(usuarios);
        }
        cursor.close();
        return mUsuarios;
    }

    public boolean validaUsuario(String usuario, String contraseña)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        //COLUMNAS A TRAER
        String[] projection = {UsuariosContract.Usuarios._USUARIO,
                UsuariosContract.Usuarios._CONTRASEÑA
        };
        //CLAUSULA WHERE
        String selection = UsuariosContract.Usuarios._USUARIO + " = ? AND " +
                UsuariosContract.Usuarios._CONTRASEÑA + " = ?";
        //VALORES A USAR EN WHERE
        String[] selectionArgs = {usuario, contraseña};

        Cursor cursor = db.query(
                UsuariosContract.Usuarios.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        boolean res = cursor.moveToNext();
        cursor.close();
        return res;
    }

    public void cerrar()
    {
        helper.close();
    }
}
