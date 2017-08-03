package com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.dynamicsoftware.pocho.logistica.Modelo.Factura;

import java.util.ArrayList;

/**
 * Created by Pocho on 15/06/2017.
 */

public abstract class FacturasArrayListAdapter extends BaseAdapter implements Filterable
{
    private ArrayList<Factura> mItemsFiltrados;
    private ArrayList<Factura> mFacturas;

    private int R_layout_Id_View;
    private Context context;
    private ValueFilter valueFilter;

    public FacturasArrayListAdapter(ArrayList<Factura> mFacturas, int r_layout_Id_View, Context context)
    {
        this.mItemsFiltrados = mFacturas;
        this.mFacturas = mFacturas;
        R_layout_Id_View = r_layout_Id_View;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (this.mFacturas != null)
        {
            return this.mFacturas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return this.mFacturas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
       return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R_layout_Id_View, null);
        }
        onItemAdded(mFacturas.get(position), convertView);
        return convertView;
    }

    public abstract void onItemAdded(Object item, View view);

    @Override
    public Filter getFilter()
    {
        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            String str = constraint.toString().toUpperCase();
            Log.e("constraint", str);
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<Factura> filterList = new ArrayList<Factura>();
                for (int i = 0; i < mItemsFiltrados.size(); i++)
                {
                    if ((mItemsFiltrados.get(i).getTipo().toUpperCase()).contains(constraint.toString().toUpperCase()) || (mItemsFiltrados.get(i).getNumero().toUpperCase().contains(constraint.toString().toUpperCase())) || (mItemsFiltrados.get(i).getReparto().contains(constraint.toString().toUpperCase())))
                    {
                        Factura bean_contacts = mItemsFiltrados.get(i);
                        filterList.add(bean_contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else
            {
                results.count = mItemsFiltrados.size();
                results.values = mItemsFiltrados;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            mFacturas = (ArrayList<Factura>) results.values;
            notifyDataSetChanged();
        }
    }
}
