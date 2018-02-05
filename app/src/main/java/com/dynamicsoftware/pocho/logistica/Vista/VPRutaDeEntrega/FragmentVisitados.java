package com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraPosGPS;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraRutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Controladoras.Utiles;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.RutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.R;
import com.dynamicsoftware.pocho.logistica.VisitaCliente;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.CAMBIA_ESTADO_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.RUTA_DE_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

/**
 * Created by Pocho on 18/04/2017.
 */

public class FragmentVisitados extends Fragment
{
    private static final String TAG = "FragmentVisitados";
    ControladoraRutaDeEntrega controladoraRutaDeEntrega;
    ControladoraPosGPS controladoraPosGPS;
    RutaDeEntregaArrayListAdapter adapter;
    View rootView;
    ListView lv;
    private TextView noHayDatos;

    private String usuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_visita, container, false);
        usuario = SaveSharedPreferences.getUserName(getActivity());
        controladoraRutaDeEntrega = new ControladoraRutaDeEntrega(getActivity());
        controladoraPosGPS = new ControladoraPosGPS(getActivity());
        bindUI();
        return rootView;
    }

    private void bindUI()
    {
        lv = (ListView) rootView.findViewById(R.id.list_ruta_de_venta);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        noHayDatos = (TextView) rootView.findViewById(R.id.no_hay_datos);
        this.configuraListView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        configuraListView();
    }

    private void configuraListView()
    {
        try
        {
            ArrayList<RutaDeEntrega> mRutas = controladoraRutaDeEntrega.obtenerRutaDeEntregaEntregados(usuario);
            if (mRutas.size() > 0)
            {
                cambiaVisibilidad(noHayDatos, GONE);
                cambiaVisibilidad(lv, VISIBLE);
                adapter = new RutaDeEntregaArrayListAdapter(mRutas, R.layout.item_ruta_de_venta, getActivity())
                {
                    @Override
                    public void onItemAdded(Object item, View view)
                    {
                        RutaDeEntrega mRuta = (RutaDeEntrega) item;
                        TextView codigo_cliente = (TextView) view.findViewById(R.id.codigo_cliente);
                        TextView domicilio = (TextView) view.findViewById(R.id.domicilio_cliente);
                        TextView efectivo = (TextView) view.findViewById(R.id.text_efectivo);
                        TextView ctacte = (TextView) view.findViewById(R.id.text_ctacte);
                        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.img_estado);
                        codigo_cliente.setText(mRuta.getCliente() + " - " + mRuta.getNombre());
                        domicilio.setText(mRuta.getDomicilio());
                        efectivo.setText(String.valueOf(mRuta.getEfectivo()));
                        ctacte.setText(String.valueOf(mRuta.getCtacte()));
                        int img_id = 0;
                        switch (mRuta.getEstadoEntrega())
                        {
                            case VOLVER_LUEGO:
                                img_id = R.drawable.ic_volver;
                                break;
                            case ENTREGA_TOTAL:
                                img_id = R.drawable.ic_entrega_total;
                                break;
                            case ENTREGA_PARCIAL:
                                img_id = R.drawable.ic_entrega_parcial;
                                break;
                            case RECHAZADO:
                                img_id = R.drawable.ic_rechazado;
                                break;
                            case LOCAL_CERRADO:
                                img_id = R.drawable.ic_cerrado;
                                break;
                            case SIN_VISITAR:
                                img_id = R.drawable.ic_sin_visitar;
                                break;
                        }
                        if (img_id == 0)
                        {
                            cambiaVisibilidad(imageView, GONE);
                        }
                        else
                        {
                            imageView.setImageResource(img_id);
                            cambiaVisibilidad(imageView, View.VISIBLE);
                        }
                        if (mRuta.getFinalizado() > 0)
                        {
                            view.setBackgroundResource(R.color.colorFinalizado);
                        }
                        else
                        {
                            view.setBackgroundResource(android.R.drawable.screen_background_light_transparent);
                        }
                    }
                };
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (Utiles.GPSActivado(getActivity()))
                        {
                            RutaDeEntrega rutaDeEntrega = (RutaDeEntrega) parent.getItemAtPosition(position);
                            if (rutaDeEntrega.getFinalizado() == 0 && rutaDeEntrega.getEstadoEntrega() != ESTADO_ENTREGA.ENTREGA_PARCIAL)
                            {
                                Intent intent = new Intent(getContext(), VisitaCliente.class);
                                intent.putExtra(RUTA_DE_ENTREGA, rutaDeEntrega);
                                intent.putExtra(CAMBIA_ESTADO_ENTREGA, true);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            controladoraPosGPS.creaIntentGrabar( "NOGPS", usuario, ESTADO_ENTREGA.A_VISITAR);
                            Utiles.displayPromptForEnablingGPS(getActivity());
                        }
                    }
                });
            }
            else
            {
                cambiaVisibilidad(noHayDatos, View.VISIBLE);
                cambiaVisibilidad(lv, GONE);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    @Override
    public void onDestroy()
    {
//        controladoraRutaDeEntrega.cerrar();
//        controladoraPosGPS.cerrar();
        super.onDestroy();
    }

    public void beginSearch(String query)
    {
        Log.e("QueryFragment", query);
        if (adapter != null)
        {
            adapter.getFilter().filter(query);
        }
    }

    public void resetSearch()
    {
        if (adapter != null)
        {
            adapter.getFilter().filter("");
        }
    }

    public int cantidadElementos()
    {
        return controladoraRutaDeEntrega.obtenerCantidadElementosVisitados(usuario);
    }
}
