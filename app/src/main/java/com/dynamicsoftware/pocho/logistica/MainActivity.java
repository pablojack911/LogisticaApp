package com.dynamicsoftware.pocho.logistica;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Services.GPSLocationServiceDos;
import com.dynamicsoftware.pocho.logistica.Vista.EstadoMercaderia.EstadoMercaderiaActivity;
import com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega.FragmentVisitados;
import com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega.FragmentVisitar;
import com.dynamicsoftware.pocho.logistica.Vista.VPRutaDeEntrega.PageAdapter_RutaDeEntrega;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";
    ViewPager mViewPager;
    PageAdapter_RutaDeEntrega mPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!CheckPermissions())
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, CONSTANTES.REQUEST_PERMISSION_CODE);
        }
        else
        {
            startService(new Intent(this, GPSLocationServiceDos.class));
            //TODO: 2 pos? LoginActivity goto 127
        }
        setTitle(getResources().getString(R.string.app_name) + " - " + SaveSharedPreferences.getUserName(MainActivity.this));

        mPageAdapter = new PageAdapter_RutaDeEntrega(getSupportFragmentManager());
        mViewPager = findViewById(R.id.vp_ruta);
        mViewPager.setAdapter(mPageAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case CONSTANTES.REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    {
//                        startService(new Intent(this, GPSLocationServiceDos.class));
                        //TODO: 3 goto LoginActivity 127
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, CONSTANTES.REQUEST_PERMISSION_CODE);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        configuraBuscador(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void configuraBuscador(Menu menu)
    {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        if (null != searchView)
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText)
            {
                for (int i = 0; i < mViewPager.getAdapter().getCount(); i++)
                {
                    Fragment viewPagerFragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, i);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded())
                    {
                        if (viewPagerFragment instanceof FragmentVisitar)
                        {
                            FragmentVisitar fragmentVisitar = (FragmentVisitar) viewPagerFragment;
                            if (fragmentVisitar != null)
                            {
                                if (TextUtils.isEmpty(newText))
                                {
                                    fragmentVisitar.resetSearch(); // reset
                                }
                                else
                                {
                                    fragmentVisitar.beginSearch(newText); // busqueda
                                }
                            }
                        }
                        else if (viewPagerFragment instanceof FragmentVisitados)
                        {
                            FragmentVisitados fragmentVisitados = (FragmentVisitados) viewPagerFragment;
                            if (fragmentVisitados != null)
                            {
                                if (TextUtils.isEmpty(newText))
                                {
                                    fragmentVisitados.resetSearch(); // reset
                                }
                                else
                                {
                                    fragmentVisitados.beginSearch(newText); // busqueda
                                }
                            }
                        }
                    }
                }
                Log.e("Text", newText);
                return false;
            }

            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.menuSearch:
                break;
            case R.id.menu_logout:
                SaveSharedPreferences.LimpiarUsuario(MainActivity.this);
                finishAffinity();
                break;
            case R.id.menu_refresh:
                intent = new Intent(MainActivity.this, DescargaClientesActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_finalizar:
                finalizarReparto();
                break;
            case R.id.menu_estado_mercaderia:
                intent = new Intent(MainActivity.this, EstadoMercaderiaActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finalizarReparto()
    {
        Fragment viewPagerFragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        if (viewPagerFragment != null && viewPagerFragment.isAdded())
        {
            if (viewPagerFragment instanceof FragmentVisitar)
            {
                FragmentVisitar fragmentVisitar = (FragmentVisitar) viewPagerFragment;
                int cant = fragmentVisitar.cantidadElementos();
                if (cant > 0)
                {
                    //alert no se puede, tiene clientes pendientes
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder
                            .setMessage("No se puede finalizar el reparto porque aún tiene clientes pendientes de visita.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            });
                    builder.create().show();
                }
                else
                {
                    viewPagerFragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded())
                    {
                        if (viewPagerFragment instanceof FragmentVisitados)
                        {
                            FragmentVisitados fragmentVisitados = (FragmentVisitados) viewPagerFragment;
                            if (fragmentVisitados.cantidadElementos() > 0)
                            {
                                Intent intent = new Intent(MainActivity.this, FinalizaRepartoActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder
                                        .setMessage("No hay nada para enviar.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {

                                            }
                                        });
                                builder.create().show();
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    private boolean CheckPermissions()
    {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
