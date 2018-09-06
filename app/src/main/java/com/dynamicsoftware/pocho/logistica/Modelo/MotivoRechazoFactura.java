package com.dynamicsoftware.pocho.logistica.Modelo;

import android.os.Parcel;

/**
 * Created by Pocho on 22/06/2017.
 */

public class MotivoRechazoFactura extends BaseModel
{
    public static final Creator<MotivoRechazoFactura> CREATOR = new Creator<MotivoRechazoFactura>()
    {
        @Override
        public MotivoRechazoFactura createFromParcel(Parcel in)
        {
            return new MotivoRechazoFactura(in);
        }

        @Override
        public MotivoRechazoFactura[] newArray(int size)
        {
            return new MotivoRechazoFactura[size];
        }
    };
    private String codigo;
    private String descripcion;
    private int prioridad;

    public MotivoRechazoFactura()
    {
    }

    private MotivoRechazoFactura(Parcel in)
    {
        super(in);
        codigo = in.readString();
        descripcion = in.readString();
        prioridad = in.readInt();
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeString(codigo);
        dest.writeString(descripcion);
        dest.writeInt(prioridad);
    }

    @Override
    public String toString()
    {
        return descripcion;
    }

    public int getPrioridad()
    {
        return prioridad;
    }

    public void setPrioridad(int prioridad)
    {
        this.prioridad = prioridad;
    }
}
