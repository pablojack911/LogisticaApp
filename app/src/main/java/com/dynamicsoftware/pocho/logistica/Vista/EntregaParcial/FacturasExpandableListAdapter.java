package com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraMotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.CONDICION_VENTA;
import com.dynamicsoftware.pocho.logistica.Modelo.Factura;
import com.dynamicsoftware.pocho.logistica.Modelo.ItemFactura;
import com.dynamicsoftware.pocho.logistica.R;

import java.util.Hashtable;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

/**
 * Created by Pocho on 11/07/2017.
 */

public class FacturasExpandableListAdapter extends BaseExpandableListAdapter
{
    Context context;
    List<Factura> mFacturas;
    Hashtable<Factura, List<ItemFactura>> mItems;

    public FacturasExpandableListAdapter(Context context, List<Factura> facturas)
    {
        this.context = context;
        this.mFacturas = facturas;
        this.mItems = new Hashtable<>();
        for (Factura factura : mFacturas)
        {
            mItems.put(factura, factura.getItems());
        }
    }

    @Override
    public int getGroupCount()
    {
        //Cantidad de facturas presentes en el Hashmap
        return mItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mItems.get(mFacturas.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        //Devuelve la factura en esa posicion
        return mFacturas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        //Devuelve la List<ItemFactura>
        return mItems.get(mFacturas.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        Factura factura = (Factura) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_factura, null);
        }
        TextView tipo_numero = (TextView) convertView.findViewById(R.id.tipo_numero);
        TextView condicionVenta = (TextView) convertView.findViewById(R.id.condicionVenta);
        TextView efectivo = (TextView) convertView.findViewById(R.id.pesos);
        TextView motivoRechazo = (TextView) convertView.findViewById(R.id.motivo_rechazo);
        String tipoNumero = factura.getTipo() + factura.getNumero();
        tipo_numero.setText(tipoNumero);
        if (factura.getCondicionVenta() == CONDICION_VENTA.EFECTIVO)
        {
            condicionVenta.setText(R.string.efectivo);
            condicionVenta.setTextColor(ContextCompat.getColor(context, R.color.efectivo));
            efectivo.setText(String.valueOf(factura.getTotal()));
            efectivo.setTextColor(ContextCompat.getColor(context, R.color.efectivo));
        }
        else
        {
            condicionVenta.setText(R.string.ctacte);
            condicionVenta.setTextColor(ContextCompat.getColor(context, R.color.ctacte));
            efectivo.setText(String.valueOf(factura.getTotal()));
            efectivo.setTextColor(ContextCompat.getColor(context, R.color.ctacte));
        }
        if (factura.getCodigoRechazo() != null && !factura.getCodigoRechazo().equals(""))
        {
            ControladoraMotivoRechazoFactura controladoraMotivoRechazoFactura = new ControladoraMotivoRechazoFactura(context);
            String motivo = controladoraMotivoRechazoFactura.obtenerMotivo(factura.getCodigoRechazo());
            motivoRechazo.setText(motivo);
            cambiaVisibilidad(motivoRechazo, VISIBLE);
        }
        else
        {
            cambiaVisibilidad(motivoRechazo, GONE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        ItemFactura itemFactura = (ItemFactura) getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_item_factura, null);
        }
        TextView tvArticulo = (TextView) convertView.findViewById(R.id.tvCodigoArticulo);
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        TextView tvCantidad = (TextView) convertView.findViewById(R.id.tvCantidad);
        TextView tvImporteFinal = (TextView) convertView.findViewById(R.id.tvImporteFinal);
        tvArticulo.setText(itemFactura.getArticulo());
        tvDescripcion.setText(itemFactura.getDescripcion());
        double cantidad = itemFactura.getCantidad();
        tvCantidad.setText(String.valueOf(cantidad));
        if (cantidad < 0)
        {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.rechazo));
        }
        else
        {
            convertView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }
        tvImporteFinal.setText(String.format("%.2f", itemFactura.getImporteFinal()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
