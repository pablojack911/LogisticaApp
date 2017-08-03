package com.dynamicsoftware.pocho.logistica.DAO.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Pocho on 13/04/2017.
 */

public class UsuariosContract
{
    private UsuariosContract()
    {
    }

    public static class Usuarios implements BaseColumns
    {
        public static final String TABLE_NAME = "usuarios";
        public static final String _USUARIO = "usuario";
        public static final String _CONTRASEÑA = "contraseña";
    }
}
