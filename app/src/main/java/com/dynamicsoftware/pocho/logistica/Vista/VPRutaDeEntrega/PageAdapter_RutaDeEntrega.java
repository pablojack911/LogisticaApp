package com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Pocho on 18/04/2017.
 */

public class PageAdapter_RutaDeEntrega extends FragmentStatePagerAdapter
{
    private static final int VISITAR_VIEW = 0;
    private static final int VISITADOS_VIEW = 1;
    private static final int CANTIDAD_VISTAS = 2;

    public PageAdapter_RutaDeEntrega(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position)
        {
            case VISITAR_VIEW:
                fragment = new FragmentVisitar();
//                args.putString(CONSTANTES.ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                fragment.setArguments(args);
                break;
            case VISITADOS_VIEW:
                fragment = new FragmentVisitados();
//                args.putString(ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                fragment.setArguments(args);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        CharSequence titulo = "";
        switch (position)
        {
            case VISITAR_VIEW:
                titulo = "A visitar";
                break;
            case VISITADOS_VIEW:
                titulo = "Visitados";
                break;
            default:
                break;
        }
        return titulo;
    }

    @Override
    public int getCount()
    {
        return CANTIDAD_VISTAS;
    }
}
