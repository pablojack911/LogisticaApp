package com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraFacturas;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraItemFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Modelo.CONDICION_VENTA;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.Factura;
import com.dynamicsoftware.pocho.logistica.Modelo.ItemFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.RutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.R;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.CLIENTE;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.NOMBRE_CLIENTE;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

public class FinalizaEntregaParcial extends AppCompatActivity
{
    private static final String TAG = "FinalizaEntregaParcial";
    ControladoraFacturas controladoraFacturas;
    ControladoraItemFactura controladoraItemFactura;
    String cliente;
    String nombreCliente;
    String usuario;
    ListView lvFacturas;
    TextView tvPesosACobrar;
    TextView tvCtacteACobrar;
    FacturasArrayListAdapter adapter;
    double pesosAcobrar = 0;
    double ctacteAcobrar = 0;
    ArrayList<Factura> mFacturas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_entrega_parcial);
        usuario = SaveSharedPreferences.getUserName(FinalizaEntregaParcial.this);
        Intent intent = getIntent();
        cliente = intent.getStringExtra(CLIENTE);
        nombreCliente = intent.getStringExtra(NOMBRE_CLIENTE);
        mFacturas = intent.getParcelableArrayListExtra(CONSTANTES.FACTURAS_PARCIALES);
        controladoraFacturas = new ControladoraFacturas(FinalizaEntregaParcial.this);
        controladoraItemFactura = new ControladoraItemFactura(FinalizaEntregaParcial.this);
        bindUI();

    }

    private void bindUI()
    {
        setTitle(cliente + " - " + nombreCliente);
        lvFacturas = (ListView) findViewById(R.id.lv_facturas);
        for (Factura factura : mFacturas)
        {
            if (factura.getCondicionVenta() == CONDICION_VENTA.EFECTIVO)
            {
                double rechazado = 0;
                for (ItemFactura itemFactura : factura.getItems())
                {
                    if (itemFactura.getIdRowRefRechazo() > 0)
                    {
                        rechazado += itemFactura.getImporteFinal();
                    }
                }
                factura.setTotal_rechazado(rechazado);
                if (factura.getCodigoRechazo() != null && factura.getCodigoRechazo().equals(""))
                {
                    pesosAcobrar += factura.getTotal();
                }
                pesosAcobrar += rechazado;
            }
            else //ctacte
            {
                double rechazado = 0;
                for (ItemFactura itemFactura : factura.getItems())
                {
                    if (itemFactura.getIdRowRefRechazo() > 0)
                    {
                        rechazado += itemFactura.getImporteFinal();
                    }
                }
                factura.setTotal_rechazado(rechazado);
                if (factura.getCodigoRechazo() != null && factura.getCodigoRechazo().equals(""))
                {
                    ctacteAcobrar += factura.getTotal();
                }
                ctacteAcobrar += rechazado;
            }

        }
        adapter = new FacturasArrayListAdapter(mFacturas, R.layout.item_factura_finaliza_entrega_parcial, FinalizaEntregaParcial.this)
        {
            @Override
            public void onItemAdded(Object item, View view)
            {
                TextView tipoNumero_tv = (TextView) view.findViewById(R.id.tipo_numero);
                TextView pesos_tv = (TextView) view.findViewById(R.id.pesos);
                TextView pesosRechazados_tv = (TextView) view.findViewById(R.id.pesos_rechazados);
                Factura factura = (Factura) item;
                String tipoNumero = factura.getTipo() + " " + factura.getNumero();
                tipoNumero_tv.setText(tipoNumero);
                if (factura.getCodigoRechazo() != null && factura.getCodigoRechazo().equals(""))
                {
                    pesos_tv.setText(String.format("$ %.2f", factura.getTotal()));
                    pesos_tv.setTextColor(ContextCompat.getColor(FinalizaEntregaParcial.this, android.R.color.black));
                    pesos_tv.setTextSize(24);
                    if (factura.getTotal_rechazado() < 0)
                    {
                        cambiaVisibilidad(pesosRechazados_tv, VISIBLE);
                        pesosRechazados_tv.setText(String.format("$ %.2f", factura.getTotal_rechazado()));
                    }
                    else
                    {
                        cambiaVisibilidad(pesosRechazados_tv, GONE);
                    }
                }
                else
                {
                    pesos_tv.setText(R.string.factura_rechazada);
                    pesos_tv.setTextColor(ContextCompat.getColor(FinalizaEntregaParcial.this, R.color.error));
                    pesos_tv.setTextSize(18);
                    cambiaVisibilidad(pesosRechazados_tv, GONE);
                }
            }
        };
        lvFacturas.setAdapter(adapter);
        tvPesosACobrar = (TextView) findViewById(R.id.efectivo_a_cobrar);
        tvCtacteACobrar = (TextView) findViewById(R.id.ctacte_pesos);
        tvPesosACobrar.setText(String.format("$ %.2f", pesosAcobrar));
        tvCtacteACobrar.setText(String.format("$ %.2f", ctacteAcobrar));
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        //TODO: CERRAR
//        controladoraFacturas.cerrar();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.finaliza_entrega_parcial_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_finalizar_entrega_parcial:
                finalizarEntregaPendiente();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finalizarEntregaPendiente()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                RutaDeEntrega rutaDeEntrega = new RutaDeEntrega();
                rutaDeEntrega.setCliente(cliente);
                rutaDeEntrega.setEstadoEntrega(ESTADO_ENTREGA.ENTREGA_PARCIAL);
                ArrayList<Factura> facturaArrayList = new ArrayList<>();
                //actualmente (2017/07/26) al servicio se le envian los items rechazados en el caso de entrega parcial
                for (Factura factura : mFacturas)
                {
                    ArrayList<ItemFactura> itemFacturaArrayList = new ArrayList<>();
                    for (ItemFactura itemFactura : factura.getItems())
                    {
                        if (itemFactura.getIdRowRefRechazo() > 0)
                        {
                            itemFacturaArrayList.add(itemFactura);
                        }
                    }
                    if (itemFacturaArrayList.size() > 0)
                    {
                        factura.getItems().clear();
                        factura.getItems().addAll(itemFacturaArrayList);
                        facturaArrayList.add(factura);
                        //grabo en la db los items rechazados, para luego contar qué se entregó y que volvió a la empresa
                        for (ItemFactura itemFactura : itemFacturaArrayList)
                        {
                            controladoraItemFactura.insertar(itemFactura);
                        }
                    }
                    else
                        //TODO: REVISAR SI EL CODIGO MOTIVO RECHAZO DE LA FACTURA ENTRA
                    {
                        if (!factura.getCodigoRechazo().equals(""))
                        {
                            facturaArrayList.add(factura);
                            long res = controladoraFacturas.actualizar(factura);
                        }
                    }
                }
                rutaDeEntrega.setFacturas(facturaArrayList);
                Gson gson = new Gson();
                String json = gson.toJson(rutaDeEntrega, RutaDeEntrega.class);
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
}
