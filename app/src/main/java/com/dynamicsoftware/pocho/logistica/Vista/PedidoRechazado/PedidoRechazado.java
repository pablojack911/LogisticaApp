package com.dynamicsoftware.pocho.logistica.Vista.PedidoRechazado;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraMotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.MotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.R;
import com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial.MotivoRechazoFacturaSingleChoiceItemAdapter;

import java.util.ArrayList;

public class PedidoRechazado extends AppCompatActivity
{
    ArrayList<MotivoRechazoFactura> motivoRechazoFacturas;
    MotivoRechazoFactura motivoSeleccionado;
    ControladoraMotivoRechazoFactura controladoraMotivoRechazoFactura;

    ListView lvMotivos;
    private MotivoRechazoFacturaSingleChoiceItemAdapter adapter;
    private String cliente;
    private String nombreCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_rechazado);
        Intent intent = getIntent();
        cliente = intent.getStringExtra(CONSTANTES.CLIENTE);
        nombreCliente = intent.getStringExtra(CONSTANTES.NOMBRE_CLIENTE);
        controladoraMotivoRechazoFactura = new ControladoraMotivoRechazoFactura(PedidoRechazado.this);
        motivoRechazoFacturas = controladoraMotivoRechazoFactura.obtenerMotivos();
        bindUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        setTitle(cliente + " - " + nombreCliente);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pedido_rechazado_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_finalizar_pedido_rechazado:
                if (motivoSeleccionado != null)
                {
                    Intent intent = new Intent();
                    intent.putExtra(CONSTANTES.MOTIVO_RECHAZADO_SELECCIONADO, motivoSeleccionado.getCodigo());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Snackbar.make(this.getCurrentFocus(),"Debe seleccionar un motivo", BaseTransientBottomBar.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindUI()
    {
        lvMotivos = findViewById(R.id.lv_motivos);
        adapter = new MotivoRechazoFacturaSingleChoiceItemAdapter(motivoRechazoFacturas, PedidoRechazado.this);
        lvMotivos.setAdapter(adapter);
        lvMotivos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                motivoSeleccionado = (MotivoRechazoFactura)parent.getItemAtPosition(position);
            }
        });
        lvMotivos.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }
}
