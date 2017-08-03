package com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dynamicsoftware.pocho.logistica.Modelo.MotivoRechazoFactura;

import java.util.List;

/**
 * Created by Pocho on 13/07/2017.
 */

public class MotivoRechazoSpinnerAdapter extends ArrayAdapter<MotivoRechazoFactura>
{
    private Context context;
    private List<MotivoRechazoFactura> mItems;

    public MotivoRechazoSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MotivoRechazoFactura> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.mItems = objects;
    }

    @Override
    public int getCount()
    {
        return mItems.size();
    }

    @Nullable
    @Override
    public MotivoRechazoFactura getItem(int position)
    {
        return mItems.get(position);
    }

    @Override
    public int getPosition(@Nullable MotivoRechazoFactura item)
    {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return super.getDropDownView(position, convertView, parent);
    }
}
