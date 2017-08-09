package com.dynamicsoftware.pocho.logistica.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dynamicsoftware.pocho.logistica.DAO.DbSchema.CreateIndexes;
import com.dynamicsoftware.pocho.logistica.DAO.DbSchema.CreateTables;
import com.dynamicsoftware.pocho.logistica.DAO.DbSchema.DeleteTables;
import com.dynamicsoftware.pocho.logistica.DAO.DbSchema.MockData;

/**
 * Created by Pocho on 13/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Logistica.db";
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper mInstance;

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context ctx)
    {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null)
        {
            mInstance = new DatabaseHelper(ctx);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        try
        {
            //Usuarios
            db.execSQL(CreateTables.Usuarios);
            db.execSQL(CreateIndexes.Usuarios_id);
            db.execSQL(CreateIndexes.Usuarios_usuario);
//            db.execSQL(MockData.Usuarios);
            //Ruta de entrega
            db.execSQL(CreateTables.RutaDeEntrega);
            db.execSQL(CreateIndexes.RutaDeEntrega_id);
            db.execSQL(CreateIndexes.RutaDeEntrega_cliente);
            db.execSQL(CreateIndexes.RutaDeEntrega_enviado);
            db.execSQL(CreateIndexes.RutaDeEntrega_finalizado);
            db.execSQL(CreateIndexes.RutaDeEntrega_fletero);
//            db.execSQL(MockData.RutaDeEntrega);
            //PosGPS
            db.execSQL(CreateTables.PosGPS);
            db.execSQL(CreateIndexes.PosGPS_id);
            db.execSQL(CreateIndexes.PosGPS_cliente);
            db.execSQL(CreateIndexes.PosGPS_enviado);
            //Facturas
            db.execSQL(CreateTables.Facturas);
            db.execSQL(CreateIndexes.Facturas_id);
            db.execSQL(CreateIndexes.Facturas_cliente);
            db.execSQL(CreateIndexes.Facturas_reparto);
            db.execSQL(CreateIndexes.Facturas_tipo);
            db.execSQL(CreateIndexes.Facturas_numero);
            db.execSQL(CreateIndexes.Facturas_empresa);
            db.execSQL(CreateIndexes.Facturas_subempresa);
//            db.execSQL(MockData.Facturas);
            //MotivoRechazoFactura
            db.execSQL(CreateTables.MotivoRechazoFactura);
            db.execSQL(CreateIndexes.MotivoRechazoFactura_id);
            db.execSQL(CreateIndexes.MotivoRechazoFactura_codigo);
            db.execSQL(CreateIndexes.MotivoRechazoFactura_descripcion);
            db.execSQL(MockData.MotivoRechazoFactura);
            //ItemFactura
            db.execSQL(CreateTables.ItemFactura);
            db.execSQL(CreateIndexes.ItemFactura_articulo);
            db.execSQL(CreateIndexes.ItemFactura_factura);
            db.execSQL(CreateIndexes.ItemFactura_codigoBarraBulto);
            db.execSQL(CreateIndexes.ItemFactura_codigoBarraUnidad);
            db.execSQL(CreateIndexes.ItemFactura_descripcion);
            db.execSQL(CreateIndexes.ItemFactura_idRowRefRechazo);
            db.setTransactionSuccessful();
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            db.execSQL(DeleteTables.Usuarios);
            db.execSQL(DeleteTables.RutaDeEntrega);
            db.execSQL(DeleteTables.PosGPS);
            db.execSQL(DeleteTables.Facturas);
            db.execSQL(DeleteTables.MotivoRechazoFactura);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.onUpgrade(db, oldVersion, newVersion);
    }

    public SQLiteDatabase obtenerDB(Context context)
    {
        return getInstance(context).getWritableDatabase();
    }
}
