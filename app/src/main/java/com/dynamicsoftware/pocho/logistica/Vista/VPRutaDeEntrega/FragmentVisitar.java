package com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
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
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.RUTA_DE_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

/**
 * Created by Pocho on 18/04/2017.
 */

public class FragmentVisitar extends Fragment
{
    private static final String TAG = "FragmentVisitar";
    ControladoraRutaDeEntrega controladoraRutaDeEntrega;
    ControladoraPosGPS controladoraPosGPS;
    RutaDeEntregaArrayListAdapter adapter;
    View rootView;
    ListView lv;
    ArrayList<RutaDeEntrega> visitaMultiple;
    private TextView noHayDatos;
    private String usuario;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
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
        noHayDatos = (TextView) rootView.findViewById(R.id.no_hay_datos);
        this.configuraListView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        configuraListView();
    }

    public void configuraListView()
    {
        try
        {
            final ArrayList<RutaDeEntrega> mRutas = controladoraRutaDeEntrega.obtenerRutaDeEntregaPendiente(usuario);
            if (mRutas.size() > 0)
            {
                cambiaVisibilidad(noHayDatos, GONE);
                cambiaVisibilidad(lv, VISIBLE);

                lv.setTextFilterEnabled(true);
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
                        if (mRuta.getEstadoEntrega() == ESTADO_ENTREGA.VOLVER_LUEGO)
                        {
                            imageView.setImageResource(R.drawable.ic_volver);
                            cambiaVisibilidad(imageView, View.VISIBLE);
                        }
                        else
                        {
                            cambiaVisibilidad(imageView, GONE);
                        }
                        if (mRuta.isChecked())
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
                            //CHECK_IN
                            controladoraPosGPS.creaIntentGrabar(getActivity(), rutaDeEntrega.getCliente(), usuario, ESTADO_ENTREGA.A_VISITAR);
                            Intent intent = new Intent(getContext(), VisitaCliente.class);
                            intent.putExtra(RUTA_DE_ENTREGA, rutaDeEntrega);
                            startActivity(intent);
                        }
                        else
                        {
                            //CHECK_IN
                            controladoraPosGPS.creaIntentGrabar(getActivity(), "NOGPS", usuario, ESTADO_ENTREGA.A_VISITAR);
                            Utiles.displayPromptForEnablingGPS(getActivity());
                        }
                    }
                });
                //TODO: visita multiple
                lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
                {
                    private int numberOfSelected = 0;

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
                    {
                        if (checked)
                        {
                            numberOfSelected++;
                        }
                        else
                        {
                            numberOfSelected--;
                        }
                        mode.setTitle(numberOfSelected + " seleccionados");
                        mRutas.get(position).setChecked(checked);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu)
                    {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.visita_multiple, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
                    {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.mnu_visitar:
                                visitaMultiple = new ArrayList<RutaDeEntrega>();
                                for (RutaDeEntrega ruta : mRutas)
                                {
                                    if (ruta.isChecked())
                                    {
                                        visitaMultiple.add(ruta);
                                    }
                                }
                                if (visitaMultiple.size() > 0)
                                {
                                    visitarClientes();
                                }
                                mode.finish(); // Action picked, so close the CAB
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode)
                    {
                        numberOfSelected = 0;
                        for (RutaDeEntrega ruta : mRutas)
                        {
                            ruta.setChecked(false);
                        }
                        adapter.notifyDataSetChanged();
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

    private void visitarClientes()
    {
        for (RutaDeEntrega rutaDeEntrega : visitaMultiple)
        {
            //CHECK_IN
            controladoraPosGPS.creaIntentGrabar(getContext(), rutaDeEntrega.getCliente(), usuario, rutaDeEntrega.getEstadoEntrega());
        }
        Intent intent = new Intent(getActivity(), VisitaMultipleClientes.class);
        intent.putExtra(CONSTANTES.CLIENTES_VISITA_MULTIPLE, visitaMultiple);
        startActivity(intent);
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
        return controladoraRutaDeEntrega.obtenerCantidadElementosVisitar(usuario);
    }
}