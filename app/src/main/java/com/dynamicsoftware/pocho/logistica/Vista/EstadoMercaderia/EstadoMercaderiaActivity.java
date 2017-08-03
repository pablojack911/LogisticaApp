package com.dynamicsoftware.pocho.logistica.Vista.EstadoMercaderia;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraFacturas;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraItemFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.R;

import java.util.ArrayList;
import java.util.List;

public class EstadoMercaderiaActivity extends AppCompatActivity
{
    ListView lvArticulosEnCamion;
    TextView tvPesosARendir;
    String usuario;
    ControladoraItemFactura controladoraItemFactura;
    ControladoraFacturas controladoraFacturas;
    List<MercaderiaEnCamion> mercaderiaEnCamionList;
    double efectivoEntregaTotal;
    double efectivoEntregaParcial;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_mercaderia);
        usuario = SaveSharedPreferences.getUserName(EstadoMercaderiaActivity.this);
        controladoraItemFactura = new ControladoraItemFactura(EstadoMercaderiaActivity.this);
        controladoraFacturas = new ControladoraFacturas(EstadoMercaderiaActivity.this);
        mercaderiaEnCamionList = new ArrayList<>();
        Cursor cursorMercaderiaEnCamion = controladoraItemFactura.obtenerCursorMercaderiaEnCamion(usuario);
        while (cursorMercaderiaEnCamion.moveToNext())
        {
            MercaderiaEnCamion mercaderiaEnCamion = new MercaderiaEnCamion();
            mercaderiaEnCamion.setArticulo(cursorMercaderiaEnCamion.getString(0));
            mercaderiaEnCamion.setDescripcion(cursorMercaderiaEnCamion.getString(1));
            mercaderiaEnCamion.setBultos(cursorMercaderiaEnCamion.getInt(2));
            mercaderiaEnCamionList.add(mercaderiaEnCamion);
        }
        efectivoEntregaTotal = controladoraFacturas.totalEfectivoEntregaTotalParaRendir(usuario);
        efectivoEntregaParcial = controladoraFacturas.totalEfectivoEntregaParcialParaRendir(usuario);
        bindUI();
    }

    private void bindUI()
    {
        setTitle(usuario + " - Rechazos");
        tvPesosARendir = (TextView) findViewById(R.id.efectivo_a_rendir);
        double total = efectivoEntregaTotal + efectivoEntregaParcial;
        tvPesosARendir.setText(String.format("$ %.2f", total));
        lvArticulosEnCamion = (ListView) findViewById(R.id.lv_articulos_en_camion);
        MercaderiaEnCamionAdapter adapter = new MercaderiaEnCamionAdapter(EstadoMercaderiaActivity.this, mercaderiaEnCamionList);
        lvArticulosEnCamion.setAdapter(adapter);
    }
}
