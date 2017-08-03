package com.dynamicsoftware.pocho.logistica.Vista.EstadoMercaderia;

/**
 * Created by Pocho on 01/08/2017.
 */

public class MercaderiaEnCamion
{
    String articulo;
    String descripcion;
    int bultos;

    public String getArticulo()
    {
        return articulo;
    }

    public void setArticulo(String articulo)
    {
        this.articulo = articulo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public int getBultos()
    {
        return bultos;
    }

    public void setBultos(int bultos)
    {
        this.bultos = bultos;
    }
}
