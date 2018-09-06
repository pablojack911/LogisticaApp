package com.dynamicsoftware.pocho.logistica;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraFacturas;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraItemFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraMotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraPosGPS;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraRutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Controladoras.Utiles;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.Factura;
import com.dynamicsoftware.pocho.logistica.Modelo.ItemFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.RutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial.ActivityListadoFactura;
import com.dynamicsoftware.pocho.logistica.Vista.PedidoRechazado.PedidoRechazado;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.dynamicsoftware.pocho.logistica.CONSTANTES.CAMBIA_ESTADO_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.ID_RUTA_DE_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.MOTIVO_RECHAZO_FACTURA_CERRADO;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.MOTIVO_RECHAZO_FACTURA_SIN_VISITAR;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.RESTABLECE_ENTREGA;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.RUTA_DE_ENTREGA;

public class VisitaCliente extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, AsyncResponse
{
    private static final String TAG = "VisitaCliente";
    RutaDeEntrega mRutaDeEntrega;
    //    ArrayList<MotivoRechazoFactura> motivoRechazoFacturas;

    ControladoraRutaDeEntrega controladoraRutaDeEntrega;
    ControladoraPosGPS controladoraPosGPS;
    ControladoraMotivoRechazoFactura controladoraMotivoRechazoFactura;
    ControladoraFacturas controladoraFacturas;
    ControladoraItemFactura controladoraItemFactura;
    boolean cambiaEstado;
    ESTADO_ENTREGA estadoSeleccionado;
    String usuario;

    Button btnEntregaTotal;
    Button btnEntregaParcial;
    Button btnRechazado;
    Button btnCerrado;
    Button btnSinVisitar;
    Button btnVolverLuego;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_cliente);
        controladoraRutaDeEntrega = new ControladoraRutaDeEntrega(VisitaCliente.this);
        controladoraPosGPS = new ControladoraPosGPS(VisitaCliente.this);
        controladoraMotivoRechazoFactura = new ControladoraMotivoRechazoFactura(VisitaCliente.this);
        controladoraFacturas = new ControladoraFacturas(VisitaCliente.this);
        controladoraItemFactura = new ControladoraItemFactura(VisitaCliente.this);
        usuario = SaveSharedPreferences.getUserName(VisitaCliente.this);
        Intent intent = getIntent();
        if (intent.hasExtra(RUTA_DE_ENTREGA))
        {
            mRutaDeEntrega = intent.getParcelableExtra(RUTA_DE_ENTREGA);
        }
        bindUI();
        if (intent.hasExtra(CAMBIA_ESTADO_ENTREGA))
        {
            cambiaEstado = intent.getBooleanExtra(CAMBIA_ESTADO_ENTREGA, false);
            if (cambiaEstado)
            {
                if (intent.hasExtra(RESTABLECE_ENTREGA))
                {
                    boolean restableceEntrega = intent.getBooleanExtra(RESTABLECE_ENTREGA, false);
                    if (restableceEntrega)
                    {
                        estadoSeleccionado = ESTADO_ENTREGA.A_VISITAR;
                        preparaEntrega("", ESTADO_ENTREGA.ENTREGA_TOTAL, true);//mando entrega total aunque en la bd se guarda como A_VISITAR.
                    }
                }
                else
                {
                    btnVolverLuego.setVisibility(View.GONE);
                }
            }
            else
            {
                btnVolverLuego.setVisibility(View.VISIBLE);
            }
        }


        //        motivoRechazoFacturas = controladoraMotivoRechazoFactura.obtenerMotivos(null, null);
        //        //CHECK IN SE HACE EN FragmentVisitar unicamente
        //        controladoraPosGPS.creaIntentGrabar(VisitaCliente.this, mRutaDeEntrega.getCliente(), usuario, mRutaDeEntrega.getEstado());
    }

    @Override
    protected void onDestroy()
    {
        //        controladoraRutaDeEntrega.cerrar();
        //        controladoraPosGPS.cerrar();
        dismissProgressDialog();
        super.onDestroy();
    }

    private void bindUI()
    {
        setTitle(mRutaDeEntrega.getCliente() + " - " + mRutaDeEntrega.getNombre());
        btnEntregaTotal = findViewById(R.id.btn_entrega_total);
        btnEntregaTotal.setOnClickListener(this);
        btnEntregaParcial = findViewById(R.id.btn_entrega_parcial);
        btnEntregaParcial.setOnClickListener(this);
        btnRechazado = findViewById(R.id.btn_rechazado);
        btnRechazado.setOnClickListener(this);
        btnCerrado = findViewById(R.id.btn_cerrado);
        btnCerrado.setOnClickListener(this);
        btnSinVisitar = findViewById(R.id.btn_sin_visitar);
        btnSinVisitar.setOnClickListener(this);
        btnVolverLuego = findViewById(R.id.btn_volver_luego);
        btnVolverLuego.setOnClickListener(this);
        progressDialog = new ProgressDialog(VisitaCliente.this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    void ActualizaEstado(ESTADO_ENTREGA estado_entrega)
    {
        controladoraRutaDeEntrega.actualizaEstado(estado_entrega, mRutaDeEntrega.getId());
        //CHECK OUT
        controladoraPosGPS.creaIntentGrabar(mRutaDeEntrega.getCliente(), usuario, estado_entrega);
        finalizar();
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
                estadoSeleccionado = ESTADO_ENTREGA.VOLVER_LUEGO;
                ActualizaEstado(estadoSeleccionado);
                break;
            default:
                break;
        }
    }

    void ConfiguraSinVisitar()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            estadoSeleccionado = ESTADO_ENTREGA.SIN_VISITAR;
            preparaEntrega(MOTIVO_RECHAZO_FACTURA_SIN_VISITAR, estadoSeleccionado);
            //            ActualizaEstado(ESTADO_ENTREGA.SIN_VISITAR);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar("NOGPS", usuario, ESTADO_ENTREGA.SIN_VISITAR);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraCerrado()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            estadoSeleccionado = ESTADO_ENTREGA.LOCAL_CERRADO;
            preparaEntrega(MOTIVO_RECHAZO_FACTURA_CERRADO, estadoSeleccionado);
            //            ActualizaEstado(ESTADO_ENTREGA.LOCAL_CERRADO);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar("NOGPS", usuario, ESTADO_ENTREGA.LOCAL_CERRADO);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraEntregaTotal()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            //no necesita llamar a preparaEntrega para grabar en detDescarga
            estadoSeleccionado = ESTADO_ENTREGA.ENTREGA_TOTAL;
            preparaEntrega("", estadoSeleccionado);
            //            ActualizaEstado(ESTADO_ENTREGA.ENTREGA_TOTAL);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar("NOGPS", usuario, ESTADO_ENTREGA.ENTREGA_TOTAL);
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
            controladoraPosGPS.creaIntentGrabar("NOGPS", usuario, ESTADO_ENTREGA.ENTREGA_PARCIAL);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void ConfiguraEntregaRechazado()
    {
        if (Utiles.GPSActivado(VisitaCliente.this))
        {
            //            AlertDialog.Builder builder = new AlertDialog.Builder(VisitaCliente.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
            //            builder.setTitle(R.string.rechazar_todo);
            //            MotivoRechazoFacturaSingleChoiceItemAdapter motivosAdapter = new MotivoRechazoFacturaSingleChoiceItemAdapter(motivoRechazoFacturas, VisitaCliente.this);
            //            int seleccionado = -1;
            //            builder.setSingleChoiceItems(motivosAdapter, seleccionado, new DialogInterface.OnClickListener()
            //            {
            //                @Override
            //                public void onClick(DialogInterface dialog, int which)
            //                {
            //
            //                }
            //            });
            //            builder.setPositiveButton(R.string.rechazar, new DialogInterface.OnClickListener()
            //            {
            //                public void onClick(DialogInterface dialog, int id)
            //                {
            //                    ListView lw = ((AlertDialog) dialog).getListView();
            //                    MotivoRechazoFactura motivoSeleccionado = (MotivoRechazoFactura) lw.getAdapter().getItem(lw.getCheckedItemPosition());
            //                    if (motivoSeleccionado != null)
            //                    {
            //                        estadoSeleccionado = ESTADO_ENTREGA.RECHAZADO;
            //                        preparaEntrega(motivoSeleccionado.getCodigo(), estadoSeleccionado);
            ////                        ActualizaEstado(ESTADO_ENTREGA.RECHAZADO);
            //                    }
            //                }
            //            });
            //            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener()
            //            {
            //                @Override
            //                public void onClick(DialogInterface dialog, int which)
            //                {
            //                }
            //            });
            //            builder.setOnDismissListener(VisitaCliente.this);
            //            AlertDialog alertDialog = builder.create();
            //            alertDialog.show();
            Intent intent = new Intent(VisitaCliente.this, PedidoRechazado.class);
            intent.putExtra(CONSTANTES.CLIENTE, mRutaDeEntrega.getCliente());
            intent.putExtra(CONSTANTES.NOMBRE_CLIENTE, mRutaDeEntrega.getNombre());
            startActivityForResult(intent, CONSTANTES.PEDIDO_RECHAZADO_INTENT);
        }
        else
        {
            controladoraPosGPS.creaIntentGrabar("NOGPS", usuario, ESTADO_ENTREGA.RECHAZADO);
            Utiles.displayPromptForEnablingGPS(VisitaCliente.this);
        }
    }

    void preparaEntrega(String codigoMotivoSeleccionado, ESTADO_ENTREGA estado, Boolean... eliminaRechazos)
    {
        controladoraFacturas.actualizarFacturas(mRutaDeEntrega.getCliente(), codigoMotivoSeleccionado);
        ArrayList<Factura> mFacturas = controladoraFacturas.obtenerFacturas(mRutaDeEntrega.getCliente());
        for (Factura factura : mFacturas)
        {
            if (eliminaRechazos != null && eliminaRechazos.length > 0 && eliminaRechazos[0])
            {
                controladoraItemFactura.limpiarRechazos(factura);
            }
            ArrayList<ItemFactura> itemFacturaArrayList = controladoraItemFactura.obtenerItemsFactura(factura.getId());
            factura.setItems(itemFacturaArrayList);
        }
        mRutaDeEntrega.setFacturas(mFacturas);
        mRutaDeEntrega.setEstadoEntrega(estado);
//        if (eliminaRechazos != null && eliminaRechazos.length > 0 && eliminaRechazos[0])
//        {
//            for (Factura fac : mRutaDeEntrega.getFacturas())
//            {
//                controladoraItemFactura.limpiarRechazos(fac);
//            }
//        }
        enviarRechazo();
    }

    private void enviarRechazo()
    {
        new EnviarRechazoAsyncTask(mRutaDeEntrega, this).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CONSTANTES.ENTREGA_PARCIAL_INTENT)
        {
            if (resultCode == RESULT_OK)
            {
                estadoSeleccionado = ESTADO_ENTREGA.ENTREGA_PARCIAL;
                ActualizaEstado(estadoSeleccionado);
            }
        }
        else
        {
            if (requestCode == CONSTANTES.PEDIDO_RECHAZADO_INTENT)
            {
                if (resultCode == RESULT_OK)
                {
                    String motivo = data.getStringExtra(CONSTANTES.MOTIVO_RECHAZADO_SELECCIONADO);
                    estadoSeleccionado = ESTADO_ENTREGA.RECHAZADO;
                    preparaEntrega(motivo, estadoSeleccionado);
                }
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

    public void processFinish(Boolean output)
    {
        if (!output)
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VisitaCliente.this);
            builder.setCancelable(false);
            builder.setMessage("No se logró grabar los cambios en el servidor.");
            builder.setPositiveButton("reintentar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    enviarRechazo();
                }
            });
            builder.setNegativeButton("omitir", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ActualizaEstado(estadoSeleccionado);
                }
            });
            builder.create().show();
        }
        else
        {
            ActualizaEstado(estadoSeleccionado);
        }
    }

    private void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class EnviarRechazoAsyncTask extends AsyncTask<Void, String, Boolean>
    {
        AsyncResponse delegate;
        RutaDeEntrega mRutaDeEntrega;

        EnviarRechazoAsyncTask(RutaDeEntrega rutaDeEntrega, AsyncResponse delegate)
        {
            this.delegate = delegate;
            this.mRutaDeEntrega = rutaDeEntrega;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Boolean res;
            Gson gson = new Gson();
            String json = gson.toJson(mRutaDeEntrega, RutaDeEntrega.class);
            HttpURLConnection conn;
            try
            {
                publishProgress("Procesando petición...");
                URL url = new URL(CONSTANTES.URL_FINALIZA_ENTREGA);
                conn = (HttpURLConnection) url.openConnection();
                publishProgress("Conectando con el servidor...");
                conn.setConnectTimeout(CONSTANTES.TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();
                // read the response
                int responseCode = conn.getResponseCode();
                String response;
                if (responseCode == 200) //OK
                {
                    res = true;
                    response = "Grabando en el servidor -> OK.";
                    Log.d(TAG, response);
                    publishProgress(response);
                }
                else
                {
                    res = false;
                    response = conn.getResponseMessage();
                    Log.d(TAG, response);
                    publishProgress(response);
                }
                conn.disconnect();
            }
            catch (Exception ex)
            {
                res = false;
                Log.e(TAG, ex.getLocalizedMessage());
                publishProgress(ex.getLocalizedMessage());
            }
            return res;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            dismissProgressDialog();
            delegate.processFinish(result);
            //todo: error cuando intenta hacer dismiss
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            if (values.length > 0)
            {
                progressDialog.setMessage(values[0]);
            }
        }
    }
}
