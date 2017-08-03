package com.dynamicsoftware.pocho.logistica.Modelo;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Pocho on 15/05/2017.
 */

public class Factura extends BaseModel
{
    public static final Creator<Factura> CREATOR = new Creator<Factura>()
    {
        @Override
        public Factura createFromParcel(Parcel in)
        {
            return new Factura(in);
        }

        @Override
        public Factura[] newArray(int size)
        {
            return new Factura[size];
        }
    };
    private String cliente;
    private String empresa;
    private String reparto;
    private String tipo;
    private String numero;
    private String subempresa;
    private String codigoRechazo;
    private double alicuotaIIBB;
    private double subtotal;
    private double ivaBasico;
    private double ivaAdicional;
    private double impuestoInterno;
    private double percepcionIIBB;
    private double total;
    private double total_rechazado;
    private CONDICION_VENTA condicionVenta;
    private ArrayList<ItemFactura> items;

    public Factura()
    {
    }

    protected Factura(Parcel in)
    {
        cliente = in.readString();
        empresa = in.readString();
        reparto = in.readString();
        tipo = in.readString();
        numero = in.readString();
        subempresa = in.readString();
        alicuotaIIBB = in.readDouble();
        subtotal = in.readDouble();
        ivaBasico = in.readDouble();
        ivaAdicional = in.readDouble();
        impuestoInterno = in.readDouble();
        percepcionIIBB = in.readDouble();
        total = in.readDouble();
        condicionVenta = CONDICION_VENTA.parse(in.readInt());
        codigoRechazo = in.readString();
        items = in.createTypedArrayList(ItemFactura.CREATOR);
    }

    public String getSubempresa()
    {
        return subempresa;
    }

    public void setSubempresa(String subempresa)
    {
        this.subempresa = subempresa;
    }

    public String getCodigoRechazo()
    {
        return codigoRechazo;
    }

    public void setCodigoRechazo(String codigoRechazo)
    {
        this.codigoRechazo = codigoRechazo;
    }

    public CONDICION_VENTA getCondicionVenta()
    {
        return condicionVenta;
    }

    public void setCondicionVenta(CONDICION_VENTA condicionVenta)
    {
        this.condicionVenta = condicionVenta;
    }

    public String getCliente()
    {
        return cliente;
    }

    public void setCliente(String cliente)
    {
        this.cliente = cliente;
    }

    public String getEmpresa()
    {
        return empresa;
    }

    public void setEmpresa(String empresa)
    {
        this.empresa = empresa;
    }

    public String getReparto()
    {
        return reparto;
    }

    public void setReparto(String reparto)
    {
        this.reparto = reparto;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public double getAlicuotaIIBB()
    {
        return alicuotaIIBB;
    }

    public void setAlicuotaIIBB(double alicuotaIIBB)
    {
        this.alicuotaIIBB = alicuotaIIBB;
    }

    public double getSubtotal()
    {
        return subtotal;
    }

    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }

    public double getIvaBasico()
    {
        return ivaBasico;
    }

    public void setIvaBasico(double ivaBasico)
    {
        this.ivaBasico = ivaBasico;
    }

    public double getIvaAdicional()
    {
        return ivaAdicional;
    }

    public void setIvaAdicional(double ivaAdicional)
    {
        this.ivaAdicional = ivaAdicional;
    }

    public double getImpuestoInterno()
    {
        return impuestoInterno;
    }

    public void setImpuestoInterno(double impuestoInterno)
    {
        this.impuestoInterno = impuestoInterno;
    }

    public double getPercepcionIIBB()
    {
        return percepcionIIBB;
    }

    public void setPercepcionIIBB(double percepcionIIBB)
    {
        this.percepcionIIBB = percepcionIIBB;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public ArrayList<ItemFactura> getItems()
    {
        return items;
    }

    public void setItems(ArrayList<ItemFactura> items)
    {
        this.items = items;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(cliente);
        dest.writeString(empresa);
        dest.writeString(reparto);
        dest.writeString(tipo);
        dest.writeString(numero);
        dest.writeString(subempresa);
        dest.writeDouble(alicuotaIIBB);
        dest.writeDouble(subtotal);
        dest.writeDouble(ivaBasico);
        dest.writeDouble(ivaAdicional);
        dest.writeDouble(impuestoInterno);
        dest.writeDouble(percepcionIIBB);
        dest.writeDouble(total);
        dest.writeInt(condicionVenta.ordinal());
        dest.writeString(codigoRechazo);
        dest.writeTypedList(items);
    }

    public double getTotal_rechazado()
    {
        return total_rechazado;
    }

    public void setTotal_rechazado(double total_rechazado)
    {
        this.total_rechazado = total_rechazado;
    }
}
