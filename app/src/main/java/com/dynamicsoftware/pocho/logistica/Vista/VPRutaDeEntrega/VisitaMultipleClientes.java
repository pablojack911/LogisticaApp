package com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.RUTA_DE_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

public class VisitaMultipleClientes extends AppCompatActivity
{
    private static final String TAG = "VisitaMultipleClientes";
    ControladoraRutaDeEntrega controladoraRutaDeEntrega;
    ControladoraPosGPS controladoraPosGPS;
    RutaDeEntregaArrayListAdapter adapter;
    ListView lv;
    ArrayList<RutaDeEntrega> mRutas;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_multiple_clientes);
        usuario = SaveSharedPreferences.getUserName(VisitaMultipleClientes.this);
        controladoraRutaDeEntrega = new ControladoraRutaDeEntrega(VisitaMultipleClientes.this);
        controladoraPosGPS = new ControladoraPosGPS(VisitaMultipleClientes.this);
        Intent intent = getIntent();
        mRutas = intent.getParcelableArrayListExtra(CONSTANTES.CLIENTES_VISITA_MULTIPLE);
        bindUI();
    }

    private void bindUI()
    {
        lv = (ListView) findViewById(R.id.list_clientes);
        configuraListView();
    }

    private void configuraListView()
    {
        try
        {
            if (mRutas.size() > 0)
            {
                adapter = new RutaDeEntregaArrayListAdapter(mRutas, R.layout.item_ruta_de_venta, VisitaMultipleClientes.this)
                {
                    @Override
                    public void onItemAdded(Object item, View view)
                    {
                        RutaDeEntrega mRuta = (RutaDeEntrega) item;
                        if (mRuta.isChecked() && mRuta.getEstadoEntrega() == ESTADO_ENTREGA.A_VISITAR)
                        {
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
                        }
                        else
                        {
                            cambiaVisibilidad(view, GONE);
                        }
                    }
                };
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (Utiles.GPSActivado(VisitaMultipleClientes.this))
                        {
                            Intent intent = new Intent(VisitaMultipleClientes.this, VisitaCliente.class);
                            RutaDeEntrega rutaDeEntrega = (RutaDeEntrega) parent.getItemAtPosition(position);
                            intent.putExtra(RUTA_DE_ENTREGA, rutaDeEntrega);
                            startActivityForResult(intent, CONSTANTES.CLIENTES_VISITA_MULTIPLE_INTENT);
                        }
                        else
                        {
                            controladoraPosGPS.creaIntentGrabar(VisitaMultipleClientes.this, "NOGPS", usuario, ESTADO_ENTREGA.A_VISITAR);
                            Utiles.displayPromptForEnablingGPS(VisitaMultipleClientes.this);
                        }
                    }
                });
            }
            else
            {
                finish();
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        configuraListView();
    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CONSTANTES.CLIENTES_VISITA_MULTIPLE_INTENT)
        {
            if (resultCode == RESULT_OK)
            {
                RutaDeEntrega rutaDeEntrega = data.getParcelableExtra(RUTA_DE_ENTREGA);
                for (int i = 0; i < mRutas.size(); i++)
                {
                    if (mRutas.get(i).getCliente().equals(rutaDeEntrega.getCliente()))
                    {
                        mRutas.remove(i);
                        break;
                    }
                }
//                mRutas.remove(rutaDeEntrega);
                configuraListView();
                //TODO: REVISAR DONDE SE HACE EL MULTIPLE CHECKIN
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
