package com.dynamicsoftware.pocho.logistica.Modelo;

/**
 * Created by Pocho on 13/04/2017.
 */

public class Usuarios extends BaseModel
{
    private String contraseña;
    private String usuario;

    public Usuarios()
    {
    }

    public String getContraseña()
    {
        return contraseña;
    }

    public void setContraseña(String contraseña)
    {
        this.contraseña = contraseña;
    }

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }
}
