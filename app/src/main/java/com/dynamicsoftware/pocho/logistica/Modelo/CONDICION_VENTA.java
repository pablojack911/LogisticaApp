package com.dynamicsoftware.pocho.logistica.Modelo;

/**
 * Created by Pocho on 29/05/2017.
 */

public enum CONDICION_VENTA
{
    EFECTIVO,
    CUENTA_CORRIENTE;

    public static CONDICION_VENTA parse(int id)
    {
        CONDICION_VENTA res;
        switch (id)
        {
            case 0:
            default:
                res = EFECTIVO;
                break;
            case 1:
                res = CUENTA_CORRIENTE;
                break;
        }
        return res;
    }
}
