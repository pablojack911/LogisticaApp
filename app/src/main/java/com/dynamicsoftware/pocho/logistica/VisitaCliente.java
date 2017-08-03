package com.dynamicsoftware.pocho.logistica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraFacturas;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraMotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraPosGPS;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraRutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Controladoras.Utiles;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.MotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.RutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial.ActivityListadoFactura;
import com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial.MotivoRechazoFacturaSingleChoiceItemAdapter;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.dynamicsoftware.pocho.logistica.CONSTANTES.CAMBIA_ESTADO_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.ID_RUTA_DE_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.RUTA_DE_ENTREGA;

public class VisitaCliente extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener
{
    private static final String TAG = "VisitaCliente";
    RutaDeEntrega mRutaDeEntrega;
    ArrayList<MotivoRechazoFactura> motivoRechazoFacturas;

    ControladoraRutaDeEntrega controladoraRutaDeEntrega;
    ControladoraPosGPS controladoraPosGPS;
    ControladoraMotivoRechazoFactura controladoraMotivoRechazoFactura;
    ControladoraFacturas controladoraFacturas;
    boolean cambiaEstado;
    String usuario;

    Button btnEntregaTotal;
    Button btnEntregaParcial;
    Button btnRechazado;
    Button btnCerrado;
    Button btnSinVisitar;
    Button btnVolverLuego;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_cliente);
        bindUI();
        usuario = SaveSharedPreferences.getUserName(VisitaCliente.this);
        Intent intent = getIntent();
        if (intent.hasExtra(RUTA_DE_ENTREGA))
        {
            mRutaDeEntrega = intent.getParcelableExtra(RUTA_DE_ENTREGA);
        }
        if (intent.hasExtra(CAMBIA_ESTADO_ENTREGA))
        {
            cambiaEstado = intent.getBooleanExtra(CAMBIA_ESTADO_ENTREGA, false);
            if (cambiaEstado)
            {
                btnVolverLuego.setVisibility(View.GONE);
            }
            else
            {
                btnVolverLuego.setVisibility(View.VISIBLE);
            }
        }

        setTitle(mRutaDeEntrega.getCliente() + " - " + mRutaDeEntrega.getNombre());

        controladoraRutaDeEntrega = new ControladoraRutaDeEntrega(VisitaCliente.this);
        controladoraPosGPS = new ControladoraPosGPS(VisitaCliente.this);
        controladoraMotivoRechazoFactura = new ControladoraMotivoRechazoFactura(VisitaCliente.this);
        controladoraFacturas = new ControladoraFacturas(VisitaCliente.this);

        motivoRechazoFacturas = controladoraMotivoRechazoFactura.obtenerMotivos(null, null);

//        //CHECK IN SE HACE EN FragmentVisitar unicamente
//        controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, mRutaDeEntrega.getCliente(), usuario, mRutaDeEntrega.getEstado());
    }

    private void bindUI()
    {
        btnEntregaTotal = (Button) findViewById(R.id.btn_entrega_total);
        btnEntregaTotal.setOnClickListener(this);
        btnEntregaParcial = (Button) findViewById(R.id.btn_entrega_parcial);
        btnEntregaParcial.setOnClickListener(this);
        btnRechazado = (Button) findViewById(R.id.btn_rechazado);
        btnRechazado.setOnClickListener(this);
        btnCerrado = (Button) findViewById(R.id.btn_cerrado);
        btnCerrado.setOnClickListener(this);
        btnSinVisitar = (Button) findViewById(R.id.btn_sin_visitar);
        btnSinVisitar.setOnClickListener(this);
        btnVolverLuego = (Button) findViewById(R.id.btn_volver_luego);
        btnVolverLuego.setOnClickListener(this);
    }

    void ActualizaEstado(ESTADO_ENTREGA estado_entrega)
    {
        controladoraRutaDeEntrega.actualizaEstado(estado_entrega, mRutaDeEntrega.getId());
        //CHECK OUT
        controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, mRutaDeEntrega.getCliente(), usuario, estado_entrega);
        finalizar();
    }

    @Override
    protected void onDestroy()
    {
//        controladoraRutaDeEntrega.cerrar();
//        controladoraPosGPS.cerrar();
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_entrega_total:
                ConfiguraEntregaTotal();
                break;
            case R.id.btn_entrega_parcial:
                ConfiguraEntregaParcial();
                break;
            case R.id.btn_rechazado:
                ConfiguraEntregaRechazado();
                break;
            case R.id.btn_cerrado:
                ConfiguraCerrado();
                break;
            case R.id.btn_sin_visitar:
                ConfiguraSinVisitar();
                break;
            case R.id.btn_volver_luego:
                ActualizaEstado(ESTADO_ENTREGA.VOLVER_LUEGO);
                break;
            default:
                break;
        }
    }

    void ConfiguraSinVisitar()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            String sinVisitar = "006";// 006 - No se pudo llegar con el reparto
            preparaEntrega(sinVisitar, ESTADO_ENTREGA.SIN_VISITAR);
            ActualizaEstado(ESTADO_ENTREGA.SIN_VISITAR);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, "NOGPS", usuario, ESTADO_ENTREGA.SIN_VISITAR);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraCerrado()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            String cerrado = "011"; // 011 - Cerrado
            preparaEntrega(cerrado, ESTADO_ENTREGA.LOCAL_CERRADO);
            ActualizaEstado(ESTADO_ENTREGA.LOCAL_CERRADO);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, "NOGPS", usuario, ESTADO_ENTREGA.LOCAL_CERRADO);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraEntregaTotal()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            //no necesita llamar a preparaEntrega para grabar en detDescarga
            ActualizaEstado(ESTADO_ENTREGA.ENTREGA_TOTAL);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, "NOGPS", usuario, ESTADO_ENTREGA.ENTREGA_TOTAL);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraEntregaParcial()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            Intent intent = new Intent(VisitaCliente.this, ActivityListadoFactura.class);
            intent.putExtra(CONSTANTES.CLIENTE, mRutaDeEntrega.getCliente());
            intent.putExtra(CONSTANTES.NOMBRE_CLIENTE, mRutaDeEntrega.getNombre());
            intent.putExtra(ID_RUTA_DE_ENTREGA, mRutaDeEntrega.getId());
            startActivityForResult(intent, CONSTANTES.ENTREGA_PARCIAL_INTENT);
            //se graba en detDescarga en FinalizaEntregaParcial
            //se graba la posicion en onActivityResult, cuando vuelve de la clase ActivityListadoFactura -> cuando vuelve de FinalizaEntregaParcial
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, "NOGPS", usuario, ESTADO_ENTREGA.ENTREGA_PARCIAL);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraEntregaRechazado()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(VisitaCliente.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
            builder.setTitle(R.string.rechazar_todo);
            MotivoRechazoFacturaSingleChoiceItemAdapter motivosAdapter = new MotivoRechazoFacturaSingleChoiceItemAdapter(motivoRechazoFacturas, VisitaCliente.this);
            int seleccionado = -1;
            builder.setSingleChoiceItems(motivosAdapter, seleccionado, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.setPositiveButton(R.string.rechazar, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    ListView lw = ((AlertDialog) dialog).getListView();
                    MotivoRechazoFactura motivoSeleccionado = (MotivoRechazoFactura) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                    if (motivoSeleccionado != null)
                    {
                        preparaEntrega(motivoSeleccionado.getCodigo(), ESTADO_ENTREGA.RECHAZADO);
                        controladoraRutaDeEntrega.actualizaEstado(ESTADO_ENTREGA.RECHAZADO, mRutaDeEntrega.getId());
                        //CHECK OUT
                        controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, mRutaDeEntrega.getCliente(), usuario, ESTADO_ENTREGA.RECHAZADO);
                        finalizar();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                }
            });
            builder.setOnDismissListener(VisitaCliente.this);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, "NOGPS", usuario, ESTADO_ENTREGA.RECHAZADO);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void preparaEntrega(String codigoMotivoSeleccionado, ESTADO_ENTREGA estado)
    {
        controladoraFacturas.actualizarFacturas(mRutaDeEntrega.getCliente(), codigoMotivoSeleccionado);
        mRutaDeEntrega.setFacturas(controladoraFacturas.obtenerFacturas(mRutaDeEntrega.getCliente()));
        mRutaDeEntrega.setEstadoEntrega(estado);
        enviarRechazo();
    }

    private void enviarRechazo()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                Gson gson = new Gson();
                String json = gson.toJson(mRutaDeEntrega, RutaDeEntrega.class);
                HttpURLConnection conn;
                try
                {
                    URL url = new URL(CONSTANTES.URL_FINALIZA_ENTREGA);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60 * 1000);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    OutputStream os = conn.getOutputStream();
                    os.write(json.getBytes("UTF-8"));
                    os.close();
                    // read the response
                    int res = conn.getResponseCode();
                    if (res == 200) //OK
                    {
                        Log.d(TAG, "Finalizado en el servidor -> " + res);
                    }
                    else
                    {
                        String response = conn.getResponseMessage();
                        Log.d(TAG, response);
                    }
                    conn.disconnect();
                }
                catch (Exception ex)
                {
                    Log.e(TAG, ex.getLocalizedMessage());
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CONSTANTES.ENTREGA_PARCIAL_INTENT)
        {
            if (resultCode == RESULT_OK)
            {
                ActualizaEstado(ESTADO_ENTREGA.ENTREGA_PARCIAL);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        recreate();
    }

    void finalizar()
    {
        Intent intent = new Intent();
        mRutaDeEntrega.setChecked(false);
        intent.putExtra(RUTA_DE_ENTREGA, mRutaDeEntrega);
        setResult(RESULT_OK, intent);
        finish();
    }
}
