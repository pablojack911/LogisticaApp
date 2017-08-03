package com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.dynamicsoftware.pocho.logistica.Modelo.MotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pocho on 22/06/2017.
 */

public class MotivoRechazoFacturaSingleChoiceItemAdapter extends BaseAdapter
{
    List<MotivoRechazoFactura> mItems;
    private Context context;

    public MotivoRechazoFacturaSingleChoiceItemAdapter(List<MotivoRechazoFactura> mItems, Context context)
    {
        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public int getCount()
    {
        return mItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        if (position != -1)
        {
            return mItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return mItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //android.R.layout.select_dialog_singlechoice
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_single_choice, null);
        }
        ((CheckedTextView) convertView).setText(mItems.get(position).getDescripcion());
        return convertView;
    }

    public int getPosition(String codigo)
    {
        for (MotivoRechazoFactura motivo : mItems)
        {
            if (motivo.getCodigo().equals(codigo))
            {
                return mItems.indexOf(motivo);
            }
        }
        return -1;
    }
}
