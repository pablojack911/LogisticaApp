package com.dynamicsoftware.pocho.logistica.Modelo;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Pocho on 13/04/2017.
 */

public class RutaDeEntrega extends BaseModel
{
    public static final Creator<RutaDeEntrega> CREATOR = new Creator<RutaDeEntrega>()
    {
        @Override
        public RutaDeEntrega createFromParcel(Parcel in)
        {
            return new RutaDeEntrega(in);
        }

        @Override
        public RutaDeEntrega[] newArray(int size)
        {
            return new RutaDeEntrega[size];
        }
    };
    //    private Date fecha;
    private String cliente;
    private String nombre;
    private String domicilio;
    private double efectivo;
    private double ctacte;
    private ESTADO_ENTREGA estadoEntrega;
    private boolean enviado;
    private int ordenVisita;
    private int finalizado;
    private String fletero;
    private boolean checked;
    private ArrayList<Factura> facturas;

    public RutaDeEntrega()
    {
        super();
    }

    protected RutaDeEntrega(Parcel in)
    {
        super(in);
        cliente = in.readString();
        nombre = in.readString();
        domicilio = in.readString();
        efectivo = in.readDouble();
        ctacte = in.readDouble();
        enviado = in.readByte() != 0;
        ordenVisita = in.readInt();
        estadoEntrega = ESTADO_ENTREGA.parse(in.readInt());
        finalizado = in.readInt();
        fletero = in.readString();
        checked = in.readByte() != 0;
        facturas = in.createTypedArrayList(Factura.CREATOR);
    }

    public ArrayList<Factura> getFacturas()
    {
        return facturas;
    }

    public void setFacturas(ArrayList<Factura> facturas)
    {
        this.facturas = facturas;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public int getFinalizado()
    {
        return finalizado;
    }

    public void setFinalizado(int finalizado)
    {
        this.finalizado = finalizado;
    }

//    public Date getFecha()
//    {
//        return fecha;
//    }
//
//    public void setFecha(Date fecha)
//    {
//        this.fecha = fecha;
//    }

    public String getCliente()
    {
        return cliente;
    }

    public void setCliente(String cliente)
    {
        this.cliente = cliente;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getDomicilio()
    {
        return domicilio;
    }

    public void setDomicilio(String domicilio)
    {
        this.domicilio = domicilio;
    }

    public double getEfectivo()
    {
        return efectivo;
    }

    public void setEfectivo(double efectivo)
    {
        this.efectivo = efectivo;
    }

    public double getCtacte()
    {
        return ctacte;
    }

    public void setCtacte(double ctacte)
    {
        this.ctacte = ctacte;
    }

    public ESTADO_ENTREGA getEstadoEntrega()
    {
        return estadoEntrega;
    }

    public void setEstadoEntrega(ESTADO_ENTREGA estadoEntrega)
    {
        this.estadoEntrega = estadoEntrega;
    }

    public boolean isEnviado()
    {
        return enviado;
    }

    public void setEnviado(boolean enviado)
    {
        this.enviado = enviado;
    }

    public int getOrdenVisita()
    {
        return ordenVisita;
    }

    public void setOrdenVisita(int ordenVisita)
    {
        this.ordenVisita = ordenVisita;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public String getFletero()
    {
        return fletero;
    }

    public void setFletero(String fletero)
    {
        this.fletero = fletero;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeString(cliente);
        dest.writeString(nombre);
        dest.writeString(domicilio);
        dest.writeDouble(efectivo);
        dest.writeDouble(ctacte);
        dest.writeByte((byte) (enviado ? 1 : 0));
        dest.writeInt(ordenVisita);
        dest.writeInt(estadoEntrega.ordinal());
        dest.writeInt(finalizado);
        dest.writeString(fletero);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeTypedList(facturas);
    }
}
