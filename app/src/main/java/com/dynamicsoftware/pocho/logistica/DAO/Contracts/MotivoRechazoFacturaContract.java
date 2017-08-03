package com.dynamicsoftware.pocho.logistica.DAO.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Pocho on 16/06/2017.
 */

public class MotivoRechazoFacturaContract
{
    public class MotivoRechazoFactura implements BaseColumns
    {
        public static final String TABLE_NAME = "MotivoRechazoFactura";
        public static final String _CODIGO = "codigo";
        public static final String _DESCRIPCION = "descripcion";
    }
}
