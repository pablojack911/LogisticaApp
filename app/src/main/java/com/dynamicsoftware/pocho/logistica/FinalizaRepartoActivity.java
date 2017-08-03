package com.dynamicsoftware.pocho.logistica;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraRutaDeEntrega;
import com.dynamicsoftware.pocho.logistica.Controladoras.Descarga.DownloadCallback;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;

import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

public class FinalizaRepartoActivity extends AppCompatActivity implements DownloadCallback
{
    private static final String TAG = "FinalizaReparto";
    TextView tvProgreso;
    ProgressBar descarga_progreso;
    Button btnTryAgain;
    ControladoraRutaDeEntrega controladoraRuta;
    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;
    private FinalizaTask mFinalizaTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_reparto);
        bindUI();
        controladoraRuta = new ControladoraRutaDeEntrega(FinalizaRepartoActivity.this);
        intentaFinalizar();
    }

    private void bindUI()
    {
        tvProgreso = (TextView) findViewById(R.id.tvProgreso);
        descarga_progreso = (ProgressBar) findViewById(R.id.descarga_progress);
        btnTryAgain = (Button) findViewById(R.id.btn_try_again);
        btnTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cambiaVisibilidad(btnTryAgain, GONE);
                cambiaVisibilidad(descarga_progreso, VISIBLE);
                intentaFinalizar();
            }
        });
    }

    private void intentaFinalizar()
    {
        if (getActiveNetworkInfo() == null)
        {
            cambiaVisibilidad(descarga_progreso, GONE);
            tvProgreso.setText(R.string.no_connection);
            cambiaVisibilidad(btnTryAgain, VISIBLE);
        }
        else
        {
            startDownload();
        }
    }

    @Override
    public void updateFromDownload(Object result)
    {
        if (result != null)
        {
            String res = (String) result;
            tvProgreso.setText(res);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                finish();
            }
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
    }

    private void startDownload()
    {
        if (!mDownloading)
        {
            // Execute the async download.
            cancelDownload();
            mFinalizaTask = new FinalizaTask();
            mFinalizaTask.execute((Void) null);
            mDownloading = true;
        }
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload()
    {
        if (mFinalizaTask != null)
        {
            mFinalizaTask.cancel(true);
        }
    }

    @Override
    public void onDestroy()
    {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        //TODO: CERRAR
//        controladoraRuta.cerrar();
        super.onDestroy();
    }


    public class FinalizaTask extends AsyncTask<Void, String, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            publishProgress("Finalizando el reparto...");
            HttpURLConnection conn;
            try
            {
                String mUsuario = SaveSharedPreferences.getUserName(getApplicationContext());
                URL url = new URL(CONSTANTES.URL_FINALIZA_REPARTO + mUsuario);
                conn = (HttpURLConnection) url.openConnection();
                publishProgress("Conectando con el servidor...");
                conn.setConnectTimeout(60 * 1000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                // read the response
                int res = conn.getResponseCode();
                if (res == 200) //OK
                {
                    publishProgress("Conexión exitosa.");
                    Log.d(TAG, "Finalizado en el servidor -> " + res);
                    controladoraRuta.finalizarReparto(mUsuario);
                    publishProgress("Finalizado.");
                }
                else
                {
                    String response = conn.getResponseMessage();
                    publishProgress("Error. " + response);
                }
                conn.disconnect();
            }
            catch (Exception ex)
            {
                publishProgress(ex.getLocalizedMessage());
                Log.e(TAG, ex.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            mFinalizaTask = null;
            finish();
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            if (values.length > 0)
            {
                tvProgreso.setText(values[0]);
            }
        }

        @Override
        protected void onCancelled()
        {
            mFinalizaTask = null;
        }
    }
}
