package com.dynamicsoftware.pocho.logistica;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraFacturas;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraItemFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraRutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Controladoras.Descarga.DownloadCallback;
import com.dynamicsoftware.pocho.logistica.Controladoras.Fecha;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Modelo.CONDICION_VENTA;
import com.dynamicsoftware.pocho.logistica.Modelo.ESTADO_ENTREGA;
import com.dynamicsoftware.pocho.logistica.Modelo.Factura;
import com.dynamicsoftware.pocho.logistica.Modelo.ItemFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.RutaDeEntrega;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

public class DescargaClientesActivity extends AppCompatActivity implements DownloadCallback
{
    TextView tvProgreso;
    TextView tvProgresoSecundario;
    TextView tvProgresoTerciario;
    ProgressBar descarga_progreso;
    Button btnTryAgain;
    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;
    private DownloadTask mDownloadTask;

    private String usuario;
    private String fechaActual;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_clientes);
        usuario = SaveSharedPreferences.getUserName(DescargaClientesActivity.this);
        bindUI();
//        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), CONSTANTES.URL_DESCARGA + SaveSharedPreferences.getUserName(DescargaClientes.this));
        intentaDescargar();
    }

    private void intentaDescargar()
    {
        if (getActiveNetworkInfo() == null)
        {
            cambiaVisibilidad(descarga_progreso, GONE);
            cambiaVisibilidad(tvProgresoSecundario, GONE);
            cambiaVisibilidad(tvProgresoTerciario, GONE);
            tvProgreso.setText(R.string.no_connection);
            cambiaVisibilidad(btnTryAgain, VISIBLE);
        }
        else
        {
            startDownload();
        }
    }

    private void bindUI()
    {
        tvProgreso = (TextView) findViewById(R.id.tvProgreso);
        tvProgresoSecundario = (TextView) findViewById(R.id.tvProgresoSecundario);
        tvProgresoTerciario = (TextView) findViewById(R.id.tvProgresoTerciario);
        descarga_progreso = (ProgressBar) findViewById(R.id.descarga_progress);
        btnTryAgain = (Button) findViewById(R.id.btn_try_again);
        btnTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cambiaVisibilidad(btnTryAgain, GONE);
                cambiaVisibilidad(descarga_progreso, VISIBLE);
                intentaDescargar();
            }
        });
    }

    @Override
    public void updateFromDownload(Object result)
    {
        if (result != null)
        {
            String res = (String) result;
            tvProgreso.setText(res);
            cambiaVisibilidad(tvProgresoSecundario, GONE);
            cambiaVisibilidad(tvProgresoTerciario, GONE);
//            try
//            {
//                Thread.sleep(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            finally
//            {
//            finish();
//            }
            AlertDialog.Builder builder = new AlertDialog.Builder(DescargaClientesActivity.this);
            builder.setMessage(res);
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });
            builder.show();
        }

    }

    @Override
    public NetworkInfo getActiveNetworkInfo()
    {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishDownloading()
    {
        mDownloading = false;
        cancelDownload();
        SaveSharedPreferences.setLastDownload(DescargaClientesActivity.this, Fecha.obtenerFechaActual(), usuario);
    }

    @Override
    public void onBackPressed()
    {

    }

    private void startDownload()
    {
        if (!mDownloading)
        {
            // Execute the async download.
            cancelDownload();
            String lastDownload = SaveSharedPreferences.getLastDownload(DescargaClientesActivity.this, usuario);
            fechaActual = Fecha.obtenerFechaActual();
            if (!lastDownload.equals(fechaActual))
            {
                ControladoraItemFactura controladoraItemFactura = new ControladoraItemFactura(DescargaClientesActivity.this);
                controladoraItemFactura.limpiar(usuario);
                ControladoraFacturas controladoraFacturas = new ControladoraFacturas(DescargaClientesActivity.this);
                controladoraFacturas.limpiar(usuario);
                ControladoraRutaDeEntrega controladoraRutaDeEntrega = new ControladoraRutaDeEntrega(DescargaClientesActivity.this);
                controladoraRutaDeEntrega.limpiar(usuario);
            }
            mDownloadTask = new DownloadTask(DescargaClientesActivity.this);
            mDownloadTask.execute(CONSTANTES.URL_DESCARGA + usuario);
            mDownloading = true;
        }
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload()
    {
        if (mDownloadTask != null)
        {
            mDownloadTask.cancel(true);
        }
    }

    @Override
    public void onDestroy()
    {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }

    private class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result>
    {
        private static final String TAG = "DownloadTask";
        ControladoraRutaDeEntrega controladoraRutaDeEntrega;
        ControladoraFacturas controladoraFacturas;
        ControladoraItemFactura controladoraItemFactura;
        private DownloadCallback<String> mCallback;

        DownloadTask(DownloadCallback<String> callback)
        {
            setCallback(callback);
        }

        void setCallback(DownloadCallback<String> callback)
        {
            mCallback = callback;
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute()
        {
            if (mCallback != null)
            {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE))
                {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected DownloadTask.Result doInBackground(String... urls)
        {
            DownloadTask.Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0)
            {
                String urlString = urls[0];
                try
                {
                    URL url = new URL(urlString);
                    String resultString = downloadUrl(url);
                    if (resultString != null)
                    {
                        ArrayList<RutaDeEntrega> rutaDeEntregaArrayList = new ArrayList<>();
                        publishProgress(Progress.STREAM_DOWNLOAD_COMPLETE);
                        controladoraRutaDeEntrega = new ControladoraRutaDeEntrega((Context) mCallback);
                        controladoraFacturas = new ControladoraFacturas((Context) mCallback);
                        controladoraItemFactura = new ControladoraItemFactura((Context) mCallback);
                        JSONArray array = new JSONArray(resultString);
                        int dimension = array.length();
                        if (dimension > 0)
                        {
                            publishProgress(Progress.PROCESS_PARSE_STREAM_STARTED);
                            for (int i = 0; i < dimension; i++)
                            {
                                JSONObject row = array.getJSONObject(i);
                                RutaDeEntrega rutaDeEntrega = parseRutaDeEntregaCompleto(row);
                                rutaDeEntregaArrayList.add(rutaDeEntrega);
                                double progreso = ((double) i / (double) dimension) * 100;
                                publishProgress(Progress.PROCESS_PARSE_STREAM_IN_PROGRESS, (int) progreso);
                            }
                            int iRutas = 0;
                            int cantRutas = rutaDeEntregaArrayList.size();
                            for (RutaDeEntrega rutaDeEntrega : rutaDeEntregaArrayList)
                            {
                                controladoraRutaDeEntrega.comenzarTransaccion();
                                for (Factura factura : rutaDeEntrega.getFacturas())
                                {
                                    int iFacturas = 0;
                                    int cantFacturas = rutaDeEntrega.getFacturas().size();
                                    controladoraFacturas.comenzarTransaccion();
                                    long idFactura = controladoraFacturas.insertOrUpdate(factura);
                                    iFacturas++;
                                    //Progreso facturas
                                    publishProgress(Progress.GRABANDO_FACTURA, ((int) (((double) iFacturas / (double) cantFacturas) * 100)));
                                    if (idFactura != -1)
                                    {
                                        controladoraItemFactura.comenzarTransaccion();
                                        int iItemFacturas = 0;
                                        int cantItemsFactura = factura.getItems().size();
                                        for (ItemFactura itemFactura : factura.getItems())
                                        {
                                            itemFactura.setFactura((int) idFactura);
                                            controladoraItemFactura.insertOrUpdate(itemFactura);
                                            iItemFacturas++;
                                            //Progreso item facturas
                                            publishProgress(Progress.GRABANDO_ITEMS_FACTURA, ((int) (((double) iItemFacturas / (double) cantItemsFactura) * 100)));
                                        }
                                        controladoraItemFactura.transaccionExistosa();
                                        controladoraItemFactura.finalizarTransaccion();
                                    }
                                    controladoraFacturas.transaccionExistosa();
                                    controladoraFacturas.finalizarTransaccion();
                                }
                                controladoraRutaDeEntrega.insertOrUpdate(rutaDeEntrega);
                                controladoraRutaDeEntrega.transaccionExistosa();
                                controladoraRutaDeEntrega.finalizarTransaccion();
                                iRutas++;
                                //Progreso rutas
                                publishProgress(Progress.GRABANDO_RUTA, ((int) (((double) iRutas / (double) cantRutas) * 100)));
                            }
                            result = new DownloadTask.Result("Actualizado. " + iRutas + " elementos.");
                            finalizaDescarga();
                        }
                        else
                        {
                            result = new DownloadTask.Result("No hay datos para descargar.");
                        }
                    }
                    else
                    {
                        throw new IOException("No response received.");
                    }
                }
                catch (Exception e)
                {
                    result = new DownloadTask.Result(e);
                }
            }
            return result;
        }

        private RutaDeEntrega parseRutaDeEntregaCompleto(JSONObject row) throws JSONException
        {
            RutaDeEntrega rutaDeEntrega = new RutaDeEntrega();
            rutaDeEntrega.setFletero(usuario);
            rutaDeEntrega.setCliente(row.getString("cliente"));
            rutaDeEntrega.setNombre(row.getString("nombre"));
            rutaDeEntrega.setDomicilio(row.getString("domicilio"));
            rutaDeEntrega.setEfectivo(row.getDouble("efectivo"));
            rutaDeEntrega.setCtacte(row.getDouble("ctacte"));
            rutaDeEntrega.setEstadoEntrega(ESTADO_ENTREGA.parse(row.getInt("estadoEntrega")));
            rutaDeEntrega.setOrdenVisita(row.getInt("orden_Visita"));
            //Facturas
            JSONArray facturasJsonArray = row.getJSONArray("facturas");
            ArrayList<Factura> facturaArrayList = new ArrayList<>();
            if (facturasJsonArray != null)
            {
                int cantidadDeFacturas = facturasJsonArray.length();
                for (int f = 0; f < cantidadDeFacturas; f++)
                {
                    JSONObject facturaJSONObject = facturasJsonArray.getJSONObject(f);
                    Factura factura = new Factura();
                    factura.setCliente(rutaDeEntrega.getCliente());
                    factura.setEmpresa(facturaJSONObject.getString("empresa"));
                    factura.setReparto(facturaJSONObject.getString("reparto"));
                    factura.setTipo(facturaJSONObject.getString("tipo"));
                    factura.setNumero(facturaJSONObject.getString("numero"));
                    factura.setSubempresa(facturaJSONObject.getString("subEmpresa"));
                    factura.setAlicuotaIIBB(facturaJSONObject.getDouble("alicuotaIIBB"));
                    factura.setSubtotal(facturaJSONObject.getDouble("subtotal"));
                    factura.setIvaBasico(facturaJSONObject.getDouble("ivaBasico"));
                    factura.setIvaAdicional(facturaJSONObject.getDouble("ivaAdicional"));
                    factura.setImpuestoInterno(facturaJSONObject.getDouble("impuestoInterno"));
                    factura.setPercepcionIIBB(facturaJSONObject.getDouble("percepcionIIBB"));
                    factura.setTotal(facturaJSONObject.getDouble("final"));
                    factura.setCodigoRechazo(facturaJSONObject.getString("motivoRechazo"));
                    int condicionVenta = facturaJSONObject.getInt("condicionVenta");
                    if (condicionVenta == 1)
                    {
                        factura.setCondicionVenta(CONDICION_VENTA.EFECTIVO);
                    }
                    else
                    {
                        factura.setCondicionVenta(CONDICION_VENTA.CUENTA_CORRIENTE);
                    }
                    //ITEMS FACTURA
                    JSONArray itemsFacturaJsonArray = facturaJSONObject.getJSONArray("items");
                    ArrayList<ItemFactura> itemFacturaArrayList = new ArrayList<>();
                    if (itemsFacturaJsonArray != null)
                    {
                        int cantidadItemFacturas = itemsFacturaJsonArray.length();
                        for (int iif = 0; iif < cantidadItemFacturas; iif++)
                        {
                            JSONObject itemsFacturaJSONObject = itemsFacturaJsonArray.getJSONObject(iif);
                            ItemFactura itemFactura = new ItemFactura();
//                            itemFactura.setFactura((int) idFactura);
                            itemFactura.setArticulo(itemsFacturaJSONObject.getString("articulo"));
                            itemFactura.setCodigoBarraBulto(itemsFacturaJSONObject.getString("codigoBarraBulto"));
                            itemFactura.setCodigoBarraUnidad(itemsFacturaJSONObject.getString("codigoBarraUnidad"));
                            itemFactura.setDescripcion(itemsFacturaJSONObject.getString("descripcion"));
                            itemFactura.setPrecioNetoUnitario(itemsFacturaJSONObject.getDouble("precioNetoUnitario"));
                            itemFactura.setPrecioFinalUnitario(itemsFacturaJSONObject.getDouble("precioFinalUnitario"));
                            itemFactura.setTasaIva(itemsFacturaJSONObject.getDouble("tasaIva"));
                            itemFactura.setImpuestoInternoUnitario(itemsFacturaJSONObject.getDouble("impuestoInternoUnitario"));
                            itemFactura.setDescuento1(itemsFacturaJSONObject.getDouble("descuento1"));
                            itemFactura.setDescuento2(itemsFacturaJSONObject.getDouble("descuento2"));
                            itemFactura.setDescuento3(itemsFacturaJSONObject.getDouble("descuento3"));
                            itemFactura.setDescuento4(itemsFacturaJSONObject.getDouble("descuento4"));
                            itemFactura.setCantidad(itemsFacturaJSONObject.getDouble("cantidad"));
                            itemFactura.setImporteFinal(itemsFacturaJSONObject.getDouble("importeFinal"));
                            itemFactura.setUnidadesPorBulto(itemsFacturaJSONObject.getInt("unidadesPorBulto"));
                            itemFactura.setMinimoVenta(itemsFacturaJSONObject.getInt("minimoVenta"));
                            itemFacturaArrayList.add(itemFactura);
                        }
                    }
                    factura.setItems(itemFacturaArrayList);
                    facturaArrayList.add(factura);
                }
            }
            rutaDeEntrega.setFacturas(facturaArrayList);
            return rutaDeEntrega;
        }

        private void finalizaDescarga() throws IOException
        {
            HttpURLConnection conn;
            URL url = new URL(CONSTANTES.URL_FINALIZA_DESCARGA + usuario);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // read the response
            int res = conn.getResponseCode();
            if (res == 200) //OK
            {
                Log.d(TAG, "Finalizado descarga -> " + res);
            }
            else
            {
                String response = conn.getResponseMessage();
                Log.d(TAG, "Error finalizando descarga -> " + res + " - " + response);
            }
            conn.disconnect();
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(DownloadTask.Result result)
        {
            if (result != null && mCallback != null)
            {
                if (result.mException != null)
                {
                    mCallback.updateFromDownload(result.mException.getMessage());
                }
                else if (result.mResultValue != null)
                {
                    mCallback.updateFromDownload(result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            int progressCode = values[0];
            int porcentaje;
            switch (progressCode)
            {
                // You can add UI behavior for progress updates here.
                case DownloadCallback.Progress.ERROR:
                    tvProgreso.setText(R.string.error);
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case DownloadCallback.Progress.CONNECT_SUCCESS:
                    tvProgreso.setText(R.string.connecting);
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS:
                    tvProgreso.setText(R.string.stream_download_complete);
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                    porcentaje = values[1];
                    descarga_progreso.setIndeterminate(false);
                    descarga_progreso.setProgress(porcentaje);
                    tvProgreso.setText("Descargando | " + porcentaje + "% completado.");
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case DownloadCallback.Progress.STREAM_DOWNLOAD_COMPLETE:
                    descarga_progreso.setIndeterminate(true);
                    tvProgreso.setText(R.string.download_complete);
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case DownloadCallback.Progress.PROCESS_PARSE_STREAM_STARTED:
                    descarga_progreso.setIndeterminate(true);
                    tvProgreso.setText(R.string.parse_started);
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case DownloadCallback.Progress.PROCESS_PARSE_STREAM_IN_PROGRESS:
                    porcentaje = values[1];
                    descarga_progreso.setIndeterminate(false);
                    descarga_progreso.setProgress(porcentaje);
                    tvProgreso.setText("Decodificando | " + porcentaje + "%");
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
                case Progress.PROCESS_PARSE_STREAM_COMPLETED:
                    descarga_progreso.setIndeterminate(true);
                    tvProgreso.setText(R.string.decodificacion_completa);
                    cambiaVisibilidad(tvProgresoSecundario, GONE);
                    cambiaVisibilidad(tvProgresoTerciario, GONE);
                    break;
//                case Progress.PROCESS_INCORPORANDO_FACTURAS:
//                    porcentaje = values[1];
//                    descarga_progreso.setIndeterminate(false);
//                    descarga_progreso.setProgress(porcentaje);
//                    break;
                case Progress.GRABANDO_RUTA:
                    porcentaje = values[1];
                    descarga_progreso.setIndeterminate(false);
                    descarga_progreso.setProgress(porcentaje);
                    tvProgreso.setText("Grabando clientes | " + porcentaje + "%");
                    cambiaVisibilidad(tvProgresoSecundario, VISIBLE);
                    cambiaVisibilidad(tvProgresoTerciario, VISIBLE);
                    break;
                case Progress.GRABANDO_FACTURA:
                    porcentaje = values[1];
                    descarga_progreso.setIndeterminate(false);
                    descarga_progreso.setProgress(porcentaje);
                    tvProgresoSecundario.setText("Grabando facturas | " + porcentaje + "%");
                    cambiaVisibilidad(tvProgresoSecundario, VISIBLE);
                    cambiaVisibilidad(tvProgresoTerciario, VISIBLE);
                    break;
                case Progress.GRABANDO_ITEMS_FACTURA:
                    porcentaje = values[1];
                    descarga_progreso.setIndeterminate(false);
                    descarga_progreso.setProgress(porcentaje);
                    tvProgresoTerciario.setText("Grabando items de facturas | " + porcentaje + "%");
                    cambiaVisibilidad(tvProgresoSecundario, VISIBLE);
                    cambiaVisibilidad(tvProgresoTerciario, VISIBLE);
                    break;
            }
            super.onProgressUpdate(values);
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(DownloadTask.Result result)
        {
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private String downloadUrl(URL url) throws IOException
        {
            InputStream stream = null;
            HttpURLConnection connection = null;
            String result = null;
            try
            {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(60 * 1000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(60 * 1000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK)
                {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null)
                {
                    // Converts Stream to String with max length of 500.
                    int length = connection.getContentLength();
                    result = readStream(stream, length);
                }
            }
            finally
            {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null)
                {
                    stream.close();
                }
                if (connection != null)
                {
                    connection.disconnect();
                }
            }
            return result;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        private String readStream(InputStream stream, int maxLength) throws IOException
        {
            String result = null;
            // Read InputStream using the UTF-8 charset.
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            // Create temporary buffer to hold Stream data with specified max length.
            char[] buffer = new char[maxLength];
            // Populate temporary buffer with Stream data.
            int numChars = 0;
            int readSize = 0;
            while (numChars < maxLength && readSize != -1)
            {
                numChars += readSize;
                int pct = (100 * numChars) / maxLength;
                publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS, pct);
                readSize = reader.read(buffer, numChars, buffer.length - numChars);
            }
            if (numChars != -1)
            {
                // The stream was not empty.
                // Create String that is actual length of response body if actual length was less than
                // max length.
                numChars = Math.min(numChars, maxLength);
                result = new String(buffer, 0, numChars);
            }
            return result;
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        class Result
        {
            String mResultValue;
            Exception mException;

            Result(String resultValue)
            {
                mResultValue = resultValue;
            }

            Result(Exception exception)
            {
                mException = exception;
            }
        }
    }
}
