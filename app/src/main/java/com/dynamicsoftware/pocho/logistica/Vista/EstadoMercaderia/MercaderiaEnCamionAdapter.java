package com.dynamicsoftware.pocho.logistica.Vista.EstadoMercaderia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.R;

import java.util.List;

/**
 * Created by Pocho on 01/08/2017.
 */

public class MercaderiaEnCamionAdapter extends BaseAdapter
{
    List<MercaderiaEnCamion> Items;
    Context context;

    public MercaderiaEnCamionAdapter(Context context, List<MercaderiaEnCamion> items)
    {
        Items = items;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (Items != null)
        {
            return Items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return Items.get(position);
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
            convertView = vi.inflate(R.layout.item_articulo_rechazado, null);
        }
        TextView tvArticulo = (TextView) convertView.findViewById(R.id.tvCodigoArticulo);
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        EditText etBultos = (EditText) convertView.findViewById(R.id.etCantidad);
        MercaderiaEnCamion item = Items.get(position);
        tvArticulo.setText(item.getArticulo());
        tvDescripcion.setText(item.getDescripcion());
        etBultos.setText(String.valueOf(item.getBultos()));
        return convertView;
    }
}
