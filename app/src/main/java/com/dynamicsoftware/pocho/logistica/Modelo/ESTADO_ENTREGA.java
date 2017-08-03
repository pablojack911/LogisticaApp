package com.dynamicsoftware.pocho.logistica.Modelo;

/**
 * Created by Pocho on 13/04/2017.
 */

public enum ESTADO_ENTREGA
{
    A_VISITAR,
    ENTREGA_TOTAL,
    ENTREGA_PARCIAL,
    RECHAZADO,
    LOCAL_CERRADO,
    VOLVER_LUEGO,
    EN_VIAJE,
    SIN_VISITAR;

    public static ESTADO_ENTREGA parse(int i)
    {
        ESTADO_ENTREGA res;
        switch (i)
        {
            case 0:
            default:
                res = A_VISITAR;
                break;
            case 1:
                res = ENTREGA_TOTAL;
                break;
            case 2:
                res = ENTREGA_PARCIAL;
                break;
            case 3:
                res = RECHAZADO;
                break;
            case 4:
                res = LOCAL_CERRADO;
                break;
            case 5:
                res = VOLVER_LUEGO;
                break;
            case 6:
                res = EN_VIAJE;
                break;
            case 7:
                res = SIN_VISITAR;
                break;
        }
        return res;
    }
}
