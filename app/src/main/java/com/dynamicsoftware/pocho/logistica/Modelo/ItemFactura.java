package com.dynamicsoftware.pocho.logistica.Modelo;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Pocho on 15/05/2017.
 */

public class ItemFactura extends BaseModel implements Comparable
{
    public static final Creator<ItemFactura> CREATOR = new Creator<ItemFactura>()
    {
        @Override
        public ItemFactura createFromParcel(Parcel in)
        {
            return new ItemFactura(in);
        }

        @Override
        public ItemFactura[] newArray(int size)
        {
            return new ItemFactura[size];
        }
    };
    public static final Comparator<ItemFactura> comparator = new Comparator<ItemFactura>()
    {
        @Override
        public int compare(ItemFactura o1, ItemFactura o2)
        {
            return o1.getArticulo().compareTo(o2.getArticulo());
        }
    };
    private String articulo;
    private String codigoBarraBulto;
    private String codigoBarraUnidad;
    private String descripcion;
    private String motivoRechazo;
    private int factura;
    private double precioNetoUnitario;
    private double tasaIva;
    private double impuestoInternoUnitario;
    private double precioFinalUnitario;
    private double descuento1;
    private double descuento2;
    private double descuento3;
    private double descuento4;
    private double cantidad;
    private double importeFinal;
    private int idRowRefRechazo;
    private int unidadesPorBulto;
    private int minimoVenta;

    public ItemFactura()
    {
    }

    public ItemFactura(Parcel in)
    {
        super(in);
        articulo = in.readString();
        codigoBarraBulto = in.readString();
        codigoBarraUnidad = in.readString();
        descripcion = in.readString();
        precioNetoUnitario = in.readDouble();
        tasaIva = in.readDouble();
        impuestoInternoUnitario = in.readDouble();
        precioFinalUnitario = in.readDouble();
        descuento1 = in.readDouble();
        descuento2 = in.readDouble();
        descuento3 = in.readDouble();
        descuento4 = in.readDouble();
        cantidad = in.readDouble();
        importeFinal = in.readDouble();
        factura = in.readInt();
        motivoRechazo = in.readString();
        idRowRefRechazo = in.readInt();
        unidadesPorBulto = in.readInt();
        minimoVenta= in.readInt();
    }

    public ItemFactura(ItemFactura itemFactura)
    {
        articulo = itemFactura.getArticulo();
        codigoBarraBulto = itemFactura.getCodigoBarraBulto();
        codigoBarraUnidad = itemFactura.getCodigoBarraUnidad();
        descripcion = itemFactura.getDescripcion();
        precioNetoUnitario = itemFactura.getPrecioNetoUnitario();
        tasaIva = itemFactura.getTasaIva();
        impuestoInternoUnitario = itemFactura.getImpuestoInternoUnitario();
        precioFinalUnitario = itemFactura.getPrecioFinalUnitario();
        descuento1 = itemFactura.getDescuento1();
        descuento2 = itemFactura.getDescuento2();
        descuento3 = itemFactura.getDescuento3();
        descuento4 = itemFactura.getDescuento4();
        cantidad = itemFactura.getCantidad();
        importeFinal = itemFactura.getImporteFinal();
        factura = itemFactura.getFactura();
        motivoRechazo = itemFactura.getMotivoRechazo();
        idRowRefRechazo = itemFactura.getId();
        unidadesPorBulto = itemFactura.getUnidadesPorBulto();
        minimoVenta= itemFactura.getMinimoVenta();
    }

    public int getIdRowRefRechazo()
    {
        return idRowRefRechazo;
    }

    public void setIdRowRefRechazo(int idRowRefRechazo)
    {
        this.idRowRefRechazo = idRowRefRechazo;
    }

    public String getMotivoRechazo()
    {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo)
    {
        this.motivoRechazo = motivoRechazo;
    }

    public int getFactura()
    {
        return factura;
    }

    public void setFactura(int factura)
    {
        this.factura = factura;
    }

    public String getArticulo()
    {
        return articulo;
    }

    public void setArticulo(String articulo)
    {
        this.articulo = articulo;
    }

    public String getCodigoBarraBulto()
    {
        return codigoBarraBulto;
    }

    public void setCodigoBarraBulto(String codigoBarraBulto)
    {
        this.codigoBarraBulto = codigoBarraBulto;
    }

    public String getCodigoBarraUnidad()
    {
        return codigoBarraUnidad;
    }

    public void setCodigoBarraUnidad(String codigoBarraUnidad)
    {
        this.codigoBarraUnidad = codigoBarraUnidad;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public double getPrecioNetoUnitario()
    {
        return precioNetoUnitario;
    }

    public void setPrecioNetoUnitario(double precioNetoUnitario)
    {
        this.precioNetoUnitario = precioNetoUnitario;
    }

    public double getTasaIva()
    {
        return tasaIva;
    }

    public void setTasaIva(double tasaIva)
    {
        this.tasaIva = tasaIva;
    }

    public double getImpuestoInternoUnitario()
    {
        return impuestoInternoUnitario;
    }

    public void setImpuestoInternoUnitario(double impuestoInternoUnitario)
    {
        this.impuestoInternoUnitario = impuestoInternoUnitario;
    }

    public double getPrecioFinalUnitario()
    {
        return precioFinalUnitario;
    }

    public void setPrecioFinalUnitario(double precioFinalUnitario)
    {
        this.precioFinalUnitario = precioFinalUnitario;
    }

    public double getDescuento1()
    {
        return descuento1;
    }

    public void setDescuento1(double descuento1)
    {
        this.descuento1 = descuento1;
    }

    public double getDescuento2()
    {
        return descuento2;
    }

    public void setDescuento2(double descuento2)
    {
        this.descuento2 = descuento2;
    }

    public double getDescuento3()
    {
        return descuento3;
    }

    public void setDescuento3(double descuento3)
    {
        this.descuento3 = descuento3;
    }

    public double getDescuento4()
    {
        return descuento4;
    }

    public void setDescuento4(double descuento4)
    {
        this.descuento4 = descuento4;
    }

    public double getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(double cantidad)
    {
        this.cantidad = cantidad;
    }

    public double getImporteFinal()
    {
        return importeFinal;
    }

    public void setImporteFinal(double importeFinal)
    {
        this.importeFinal = importeFinal;
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
        dest.writeString(articulo);
        dest.writeString(codigoBarraBulto);
        dest.writeString(codigoBarraUnidad);
        dest.writeString(descripcion);
        dest.writeDouble(precioNetoUnitario);
        dest.writeDouble(tasaIva);
        dest.writeDouble(impuestoInternoUnitario);
        dest.writeDouble(precioFinalUnitario);
        dest.writeDouble(descuento1);
        dest.writeDouble(descuento2);
        dest.writeDouble(descuento3);
        dest.writeDouble(descuento4);
        dest.writeDouble(cantidad);
        dest.writeDouble(importeFinal);
        dest.writeInt(factura);
        dest.writeString(motivoRechazo);
        dest.writeInt(idRowRefRechazo);
        dest.writeInt(unidadesPorBulto);
        dest.writeInt(minimoVenta);
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        return this.getArticulo().compareTo(((ItemFactura) o).getArticulo());
    }

    public int getUnidadesPorBulto()
    {
        return unidadesPorBulto;
    }

    public void setUnidadesPorBulto(int unidadPorBulto)
    {
        this.unidadesPorBulto = unidadPorBulto;
    }

    public int getMinimoVenta()
    {
        return minimoVenta;
    }

    public void setMinimoVenta(int minimoVenta)
    {
        this.minimoVenta = minimoVenta;
    }
}
