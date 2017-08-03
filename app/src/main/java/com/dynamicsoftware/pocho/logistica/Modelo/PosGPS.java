package com.dynamicsoftware.pocho.logistica.Modelo;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by Pocho on 21/04/2017.
 */

public class PosGPS extends BaseModel
{
    public static final Creator<PosGPS> CREATOR = new Creator<PosGPS>()
    {
        @Override
        public PosGPS createFromParcel(Parcel in)
        {
            return new PosGPS(in);
        }

        @Override
        public PosGPS[] newArray(int size)
        {
            return new PosGPS[size];
        }
    };
    private String cliente;
    private String usuario;
    private Date fecha;
    private int enviado;
    private ESTADO_ENTREGA estadoEntrega;
    private float latitud;
    private float longitud;

    public PosGPS()
    {
        this.enviado = 0;
        this.estadoEntrega = ESTADO_ENTREGA.A_VISITAR;
    }

    private PosGPS(Parcel in)
    {
        super(in);
        cliente = in.readString();
        usuario = in.readString();
        enviado = in.readInt();
        latitud = in.readFloat();
        longitud = in.readFloat();
        estadoEntrega = ESTADO_ENTREGA.parse(in.readInt());
        fecha = new java.sql.Date(in.readLong());
    }

    public String getCliente()
    {
        return cliente;
    }

    public void setCliente(String cliente)
    {
        this.cliente = cliente;
    }

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public int getEnviado()
    {
        return enviado;
    }

    public void setEnviado(int enviado)
    {
        this.enviado = enviado;
    }

    public ESTADO_ENTREGA getEstadoEntrega()
    {
        return estadoEntrega;
    }

    public void setEstadoEntrega(ESTADO_ENTREGA estadoEntrega)
    {
        this.estadoEntrega = estadoEntrega;
    }

    public float getLatitud()
    {
        return latitud;
    }

    public void setLatitud(float latitud)
    {
        this.latitud = latitud;
    }

    public float getLongitud()
    {
        return longitud;
    }

    public void setLongitud(float longitud)
    {
        this.longitud = longitud;
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
        dest.writeString(cliente);
        dest.writeString(usuario);
        dest.writeInt(enviado);
        dest.writeFloat(latitud);
        dest.writeFloat(longitud);
        dest.writeInt(estadoEntrega.ordinal());
        dest.writeLong(fecha.getTime());
    }
}
