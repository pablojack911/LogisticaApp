package com.dynamicsoftware.pocho.logistica.Vista.EntregaParcial;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dynamicsoftware.pocho.logistica.CONSTANTES;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraFacturas;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraItemFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.ControladoraMotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.Controladoras.SaveSharedPreferences;
import com.dynamicsoftware.pocho.logistica.Modelo.Factura;
import com.dynamicsoftware.pocho.logistica.Modelo.ItemFactura;
import com.dynamicsoftware.pocho.logistica.Modelo.MotivoRechazoFactura;
import com.dynamicsoftware.pocho.logistica.R;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.dynamicsoftware.pocho.logistica.CONSTANTES.FINALIZA_ENTREGA_PARCIAL_INTENT;
import static com.dynamicsoftware.pocho.logistica.Controladoras.Utiles.cambiaVisibilidad;

public class ActivityListadoFactura extends AppCompatActivity implements DialogInterface.OnDismissListener
{
    private static String TAG = "ActivityListadoFactura";
    String usuario;
    String cliente;
    String nombreCliente;
    //    int id_ruta;
    TextView no_hay_datos;
    //    ListView lv;
    ExpandableListView lv;
    FacturasExpandableListAdapter adapter;
    //    FacturasArrayListAdapter adapter;
    ControladoraFacturas controladoraFacturas;
    ControladoraMotivoRechazoFactura controladoraMotivoRechazoFactura;
    ControladoraItemFactura controladoraItemFactura;
    List<MotivoRechazoFactura> motivoRechazoFacturas;
    ArrayList<Factura> mFacturas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_factura);
        Intent intent = getIntent();
        cliente = intent.getStringExtra(CONSTANTES.CLIENTE);
        nombreCliente = intent.getStringExtra(CONSTANTES.NOMBRE_CLIENTE);
        controladoraFacturas = new ControladoraFacturas(ActivityListadoFactura.this);
        controladoraMotivoRechazoFactura = new ControladoraMotivoRechazoFactura(ActivityListadoFactura.this);
        controladoraItemFactura = new ControladoraItemFactura(ActivityListadoFactura.this);
        motivoRechazoFacturas = controladoraMotivoRechazoFactura.obtenerMotivos(null, null);
        mFacturas = controladoraFacturas.obtenerFacturas(cliente);
        for (Factura factura : mFacturas)
        {
            ArrayList<ItemFactura> itemFacturaArrayList = controladoraItemFactura.obtenerItemsFactura(factura.getId());
            factura.setItems(itemFacturaArrayList);
        }
        usuario = SaveSharedPreferences.getUserName(ActivityListadoFactura.this);
        bindUI();
    }

    private void bindUI()
    {
        setTitle(cliente + " - " + nombreCliente);
        no_hay_datos = (TextView) findViewById(R.id.no_hay_datos);
        lv = (ExpandableListView) findViewById(R.id.list_facturas);
        configuraListView();
    }

    private void configuraListView()
    {
        try
        {
            if (mFacturas.size() > 0)
            {
                cambiaVisibilidad(no_hay_datos, GONE);
                cambiaVisibilidad(lv, VISIBLE);
                adapter = new FacturasExpandableListAdapter(ActivityListadoFactura.this, mFacturas);
            }
            else
            {
                cambiaVisibilidad(no_hay_datos, View.VISIBLE);
                cambiaVisibilidad(lv, GONE);
            }
            lv.setAdapter(adapter);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    long packedPosition = lv.getExpandableListPosition(position);

                    int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                    int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                    int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                    /*  if group item clicked */
                    if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP)
                    {
                        onGroupLongClick(groupPosition);
                    }

                    /*  if child item clicked */
                    else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
                    {
                        onChildLongClick(groupPosition, childPosition);
                    }
                    return false;
                }
            });
            lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
            {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id)
                {
                    final Factura factura = (Factura) adapter.getGroup(groupPosition);
                    final ItemFactura itemFactura = (ItemFactura) adapter.getChild(groupPosition, childPosition);
                    //TODO: IdRowRefRechazo indica que es un item generado en base a un rechazo de pedido
                    if (itemFactura.getIdRowRefRechazo() > 0)
                    {
                        //TODO: MOSTRAR alert que permita descartar el rechazo
//                        Factura fact = (Factura) adapter.getGroup(groupPosition);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListadoFactura.this);
                        builder.setMessage("¿Desea eliminar este rechazo?");
                        builder.setPositiveButton("eliminar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                factura.getItems().remove(itemFactura);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener()
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
                        Dialog dialog;
                        if (factura.getEmpresa().equals(CONSTANTES.EMPRESA_MAYORISTA))
                        {
                            dialog = crearViewModificaCantidadMayorista(factura, itemFactura);
                        }
                        else
                        {
                            dialog = crearDialogModificaCantidad(factura, itemFactura);
                        }
                        dialog.show();
                    }
                    return false;
                }
            });
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    private Dialog crearDialogModificaCantidad(final Factura factura, final ItemFactura itemFactura)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListadoFactura.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_detalle_item_factura, null);

        builder.setTitle("Rechaza item");

        EditText etCodigoArticulo = (EditText) view.findViewById(R.id.etCodigoArticulo);
        EditText etDescripcion = (EditText) view.findViewById(R.id.etDescripcion);
        final EditText etDescuento1 = (EditText) view.findViewById(R.id.etDescuento1);
        EditText etDescuento2 = (EditText) view.findViewById(R.id.etDescuento2);
        EditText etDescuento3 = (EditText) view.findViewById(R.id.etDescuento3);
        EditText etDescuento4 = (EditText) view.findViewById(R.id.etDescuento4);
        EditText etCantidad = (EditText) view.findViewById(R.id.etCantidad);

        final com.travijuu.numberpicker.library.NumberPicker etCantidadRechazada = (com.travijuu.numberpicker.library.NumberPicker) view.findViewById(R.id.etCantidadRechazada);
        final EditText etPrecioFinal = (EditText) view.findViewById(R.id.etPrecioFinal);
        EditText etPrecioUnitarioFinal = (EditText) view.findViewById(R.id.etPrecioUnitarioFinal);
        Spinner spMotivosRechazo = (Spinner) view.findViewById(R.id.spMotivosRechazo);

        etCodigoArticulo.setText(itemFactura.getArticulo());
        etDescripcion.setText(itemFactura.getDescripcion());
        if (itemFactura.getDescuento1() > 0)
        {
            cambiaVisibilidad(etDescuento1, VISIBLE);
            etDescuento1.setText(String.valueOf(itemFactura.getDescuento1()) + " %");
        }
        if (itemFactura.getDescuento2() > 0)
        {
            cambiaVisibilidad(etDescuento2, VISIBLE);
            etDescuento2.setText(String.valueOf(itemFactura.getDescuento2()) + " %");
        }
        if (itemFactura.getDescuento3() > 0)
        {
            cambiaVisibilidad(etDescuento3, VISIBLE);
            etDescuento3.setText(String.valueOf(itemFactura.getDescuento3()) + " %");
        }
        if (itemFactura.getDescuento4() > 0)
        {
            cambiaVisibilidad(etDescuento4, VISIBLE);
            etDescuento4.setText(String.valueOf(itemFactura.getDescuento4()) + " %");
        }
        double cantidad = itemFactura.getCantidad();
        etCantidad.setText(String.valueOf((int) cantidad));
        etCantidadRechazada.setMax((int) itemFactura.getCantidad());
        etCantidadRechazada.setValue(0);
        etPrecioUnitarioFinal.setText(String.format("$ %.2f", itemFactura.getPrecioFinalUnitario()));
        etPrecioFinal.setText(String.format("$ %.2f", 0.00));

        ArrayAdapter<MotivoRechazoFactura> spinnerAdapter = new MotivoRechazoSpinnerAdapter(ActivityListadoFactura.this, android.R.layout.simple_spinner_dropdown_item, motivoRechazoFacturas);
        spMotivosRechazo.setAdapter(spinnerAdapter);
        spMotivosRechazo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                MotivoRechazoFactura motivoRechazoFactura = (MotivoRechazoFactura) parent.getAdapter().getItem(position);
                itemFactura.setMotivoRechazo(motivoRechazoFactura.getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        etCantidadRechazada.setValueChangedListener(new ValueChangedListener()
        {
            @Override
            public void valueChanged(int value, ActionEnum action)
            {
                double pf = itemFactura.getPrecioFinalUnitario() * value;
                etPrecioFinal.setText(String.format("$ %.2f", pf));
            }
        });

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (etCantidadRechazada.getValue() > 0)
                {
                    //TODO: en caso de ya haber modificado el itemFactura, buscarlo en la lista de items y modificarlo o eliminarlo, en el caso que se cancele el rechazo del item
                    for (ItemFactura item : factura.getItems())
                    {
                        if (item.getIdRowRefRechazo() == itemFactura.getId())
                        {
                            factura.getItems().remove(item);
                            break;
                        }
                    }

                    //TODO: crear un nuevo registro en base al itemFactura modificado pero con cantidad en negativo
                    ItemFactura nuevoItem = new ItemFactura(itemFactura);
                    nuevoItem.setCantidad(etCantidadRechazada.getValue() * -1);
//                                    double iva = 1 + nuevoItem.getTasaIva() / 100;
//                                    double pnu = nuevoItem.getPrecioNetoUnitario();
//                                    double pfu = (pnu * iva) + nuevoItem.getImpuestoInternoUnitario();
//                                    double pfu_dto1 = pfu - (pfu * (nuevoItem.getDescuento1() / 100));
//                                    double pfu_dto2 = pfu_dto1 - (pfu_dto1 * (nuevoItem.getDescuento2() / 100));
//                                    double pfu_dto3 = pfu_dto2 - (pfu_dto2 * (nuevoItem.getDescuento3() / 100));
//                                    double pfu_dto4 = pfu_dto3 - (pfu_dto3 * (nuevoItem.getDescuento4() / 100));
//                                    nuevoItem.setPrecioFinalUnitario(pfu_dto4);
//                                    nuevoItem.setImporteFinal(pfu_dto4 * nuevoItem.getCantidad());
                    nuevoItem.setImporteFinal(nuevoItem.getPrecioFinalUnitario() * nuevoItem.getCantidad());
                    factura.getItems().add(nuevoItem);
                    Collections.sort(factura.getItems(), ItemFactura.comparator);
                    adapter.notifyDataSetChanged();
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

        builder.setView(view);
        return builder.create();
    }

    private Dialog crearViewModificaCantidadMayorista(final Factura factura, final ItemFactura itemFactura)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListadoFactura.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_detalle_item_factura_mayorista, null);

        builder.setTitle("Rechaza item mayorista");

        EditText etCodigoArticulo = (EditText) view.findViewById(R.id.etCodigoArticulo);
        EditText etDescripcion = (EditText) view.findViewById(R.id.etDescripcion);
        final EditText etDescuento1 = (EditText) view.findViewById(R.id.etDescuento1);
        EditText etDescuento2 = (EditText) view.findViewById(R.id.etDescuento2);
        EditText etDescuento3 = (EditText) view.findViewById(R.id.etDescuento3);
        EditText etDescuento4 = (EditText) view.findViewById(R.id.etDescuento4);
        final EditText etCantidadBultosOriginal = (EditText) view.findViewById(R.id.etCantidadBultosOriginal);
        EditText etCantidadUnidadesOriginal = (EditText) view.findViewById(R.id.etCantidadUnidadesOriginal);

        final com.travijuu.numberpicker.library.NumberPicker etCantidadBultosRechazados = (com.travijuu.numberpicker.library.NumberPicker) view.findViewById(R.id.etCantidadBultosRechazados);
        final com.travijuu.numberpicker.library.NumberPicker etCantidadUnidadesRechazadas = (com.travijuu.numberpicker.library.NumberPicker) view.findViewById(R.id.etCantidadUnidadesRechazadas);
        final EditText etImporteRechazado = (EditText) view.findViewById(R.id.etImporteRechazado);
        Spinner spMotivosRechazo = (Spinner) view.findViewById(R.id.spMotivosRechazo);

        etCodigoArticulo.setText(itemFactura.getArticulo());
        etDescripcion.setText(itemFactura.getDescripcion());
        if (itemFactura.getDescuento1() > 0)
        {
            cambiaVisibilidad(etDescuento1, VISIBLE);
            etDescuento1.setText(String.valueOf(itemFactura.getDescuento1()) + " %");
        }
        if (itemFactura.getDescuento2() > 0)
        {
            cambiaVisibilidad(etDescuento2, VISIBLE);
            etDescuento2.setText(String.valueOf(itemFactura.getDescuento2()) + " %");
        }
        if (itemFactura.getDescuento3() > 0)
        {
            cambiaVisibilidad(etDescuento3, VISIBLE);
            etDescuento3.setText(String.valueOf(itemFactura.getDescuento3()) + " %");
        }
        if (itemFactura.getDescuento4() > 0)
        {
            cambiaVisibilidad(etDescuento4, VISIBLE);
            etDescuento4.setText(String.valueOf(itemFactura.getDescuento4()) + " %");
        }

        final int unidadPorBulto = itemFactura.getUnidadesPorBulto();
        final int minimoVenta = itemFactura.getMinimoVenta();

        //si unidadPorBulto y minimoVenta son iguales, no se puede desarmar un bulto. Se oculta el control.
        if (unidadPorBulto == minimoVenta)
        {
            cambiaVisibilidad(etCantidadUnidadesOriginal, GONE);
            cambiaVisibilidad(etCantidadUnidadesRechazadas, GONE);
            etCantidadUnidadesRechazadas.setValue(0);
        }

        //presentacion variables de cantidades
        final double cantidadFactura = itemFactura.getCantidad();
        final int bultosOriginal = (int) cantidadFactura;
        int unidadesOriginal = (int) ((cantidadFactura - bultosOriginal) * 100);
        final int unidadesOrignialTotal = (bultosOriginal * unidadPorBulto) + unidadesOriginal;

        //presentacion variables de precios
        final double pfuBulto = itemFactura.getPrecioFinalUnitario();
        final double pfuUnidad = pfuBulto / unidadPorBulto;

        etCantidadBultosOriginal.setText(String.valueOf(bultosOriginal));
        etCantidadUnidadesOriginal.setText(String.valueOf(unidadesOriginal));

        //bultos rechazados
        etCantidadBultosRechazados.setMin(0);
        etCantidadBultosRechazados.setMax(bultosOriginal); //tope de cantidad de bultos para rechazar.
        etCantidadBultosRechazados.setValue(0);
        etCantidadBultosRechazados.setValueChangedListener(new ValueChangedListener()
        {
            @Override
            public void valueChanged(int value, ActionEnum action)
            {
                int cantidadUnidadesRechazadas = etCantidadUnidadesRechazadas.getValue();
                int cantidadUnidadesRechazadasTotalParcial = value * unidadPorBulto + cantidadUnidadesRechazadas;
                if (unidadesOrignialTotal >= cantidadUnidadesRechazadasTotalParcial)
                {
                    etCantidadBultosRechazados.setValue(value);
                }
                else
                {
                    etCantidadBultosRechazados.setValue(bultosOriginal);
                    etCantidadUnidadesRechazadas.setValue(0);
                }

                double pfRechazadoBulto, pfRechazadoUnitario, pfRechazado;
                pfRechazadoBulto = pfuBulto * etCantidadBultosRechazados.getValue();
                pfRechazadoUnitario = pfuUnidad * etCantidadUnidadesRechazadas.getValue();
                pfRechazado = pfRechazadoBulto + pfRechazadoUnitario;
                etImporteRechazado.setText(String.format("$ %.2f", pfRechazado));
            }
        });

        //unidades rechazadas
        etCantidadUnidadesRechazadas.setMin(0); //--> minimo cambia cuando bultos == 0, uRechazadas.Min=minimaVenta
//        etCantidadUnidadesRechazadas.setMax(unidadPorBulto); //tope de unidades permitido -> cantidad maxima de unidades en un bulto
        etCantidadUnidadesRechazadas.setValue(0);
        etCantidadUnidadesRechazadas.setValueChangedListener(new ValueChangedListener()
        {
            @Override
            public void valueChanged(int value, ActionEnum action)
            {
                int cantidadBultosRechazados = etCantidadBultosRechazados.getValue();
                int cantidadUnidadesRechazadasTotalParcial = cantidadBultosRechazados * unidadPorBulto + value;
                if (unidadesOrignialTotal >= cantidadUnidadesRechazadasTotalParcial)
                {
                    //SI
                    if (value == unidadPorBulto)
                    {
                        //verificar que no se rechace un bulto superior a la cantidad comprada ni la cantidad de unidades total
                        etCantidadBultosRechazados.increment();
                        etCantidadUnidadesRechazadas.setValue(0);
                    }
                    else
                    {
                        etCantidadUnidadesRechazadas.setValue(value);
                        //incrementar unidades normal
                    }
                }
                else
                {
                    etCantidadUnidadesRechazadas.setValue(value - 1);
                    Snackbar.make(view, "Maximo de bultos y unidades rechazados alcanzado", BaseTransientBottomBar.LENGTH_LONG).show();
                    //NO
                }

                double pfRechazadoBulto, pfRechazadoUnitario, pfRechazado;
                pfRechazadoBulto = pfuBulto * etCantidadBultosRechazados.getValue();
                pfRechazadoUnitario = pfuUnidad * etCantidadUnidadesRechazadas.getValue();
                pfRechazado = pfRechazadoBulto + pfRechazadoUnitario;
                etImporteRechazado.setText(String.format("$ %.2f", pfRechazado));
            }
        });


        etImporteRechazado.setText(String.format("$ %.2f", 0.00));

        ArrayAdapter<MotivoRechazoFactura> spinnerAdapter = new MotivoRechazoSpinnerAdapter(ActivityListadoFactura.this, android.R.layout.simple_spinner_dropdown_item, motivoRechazoFacturas);
        spMotivosRechazo.setAdapter(spinnerAdapter);
        spMotivosRechazo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                MotivoRechazoFactura motivoRechazoFactura = (MotivoRechazoFactura) parent.getAdapter().getItem(position);
                itemFactura.setMotivoRechazo(motivoRechazoFactura.getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                double pfRechazadoBulto, pfRechazadoUnitario, pfRechazado;
                double cantidadBultosRechazados = etCantidadBultosRechazados.getValue();
                double cantidadUnidadesRechazadas = etCantidadUnidadesRechazadas.getValue();
                double cantidadRechazadaTotal = cantidadBultosRechazados + (cantidadUnidadesRechazadas / 100);
                pfRechazadoBulto = pfuBulto * cantidadBultosRechazados;
                pfRechazadoUnitario = pfuUnidad * cantidadUnidadesRechazadas;
                pfRechazado = pfRechazadoBulto + pfRechazadoUnitario;

                if (cantidadRechazadaTotal > 0)
                {
                    //busco si ya se creó un rechazo de este item y lo elimino, para crear uno nuevo.
                    for (ItemFactura item : factura.getItems())
                    {
                        if (item.getIdRowRefRechazo() == itemFactura.getId())
                        {
                            factura.getItems().remove(item);
                            break;
                        }
                    }

                    //TODO: crear un nuevo registro en base al itemFactura modificado pero con cantidad en negativo
                    ItemFactura nuevoItem = new ItemFactura(itemFactura);
                    nuevoItem.setCantidad(cantidadRechazadaTotal * -1);
                    nuevoItem.setImporteFinal(pfRechazado * -1);
                    factura.getItems().add(nuevoItem);
                    Collections.sort(factura.getItems(), ItemFactura.comparator);
                    adapter.notifyDataSetChanged();
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

        builder.setView(view);
        return builder.create();
    }

    private boolean onChildLongClick(int groupPosition, int childPosition)
    {
        ItemFactura itemFactura = (ItemFactura) adapter.getChild(groupPosition, childPosition);
        return false;
    }

    private boolean onGroupLongClick(int groupPosition)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListadoFactura.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        builder.setTitle(R.string.rechazar_factura_completa);

        MotivoRechazoFacturaSingleChoiceItemAdapter motivosAdapter = new MotivoRechazoFacturaSingleChoiceItemAdapter(motivoRechazoFacturas, ActivityListadoFactura.this);
        int seleccionado = -1;
        final Factura factura = (Factura) adapter.getGroup(groupPosition);
        if (factura != null)
        {
            String codigoRechazo = factura.getCodigoRechazo();
            seleccionado = motivosAdapter.getPosition(codigoRechazo);
        }
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
                    factura.setCodigoRechazo(motivoSeleccionado.getCodigo());
                    ArrayList<ItemFactura> itemsRechazadosABorrar = new ArrayList<ItemFactura>();
                    for (ItemFactura itemFactura : factura.getItems())
                    {
                        if (itemFactura.getIdRowRefRechazo() > 0)
                        {
                            itemsRechazadosABorrar.add(itemFactura);
                        }
                        itemFactura.setMotivoRechazo(motivoSeleccionado.getCodigo());
                    }
                    factura.getItems().removeAll(itemsRechazadosABorrar);
                    adapter.notifyDataSetChanged();
//                    controladoraFacturas.actualizar(factura);
//                    controladoraItemFactura.actualizarMotivoRechazo(factura.getId(), motivoSeleccionado.getCodigo());
                }
                // FIRE ZE MISSILES!
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        builder.setNeutralButton("Deshacer", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                factura.setCodigoRechazo("");
//                controladoraFacturas.actualizar(factura);
            }
        });
        builder.setOnDismissListener(ActivityListadoFactura.this);
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }

    @Override
    public void onDestroy()
    {
        //TODO: CERRAR
//        controladoraFacturas.cerrar();
//        controladoraMotivoRechazoFactura.cerrar();
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
//        recreate();
        configuraListView();
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.entrega_parcial_menu, menu);
//        configuraBuscador(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuSearch:
                break;
            case R.id.menu_continuar_parcial:
                Intent intent = new Intent(ActivityListadoFactura.this, FinalizaEntregaParcial.class);
                intent.putExtra(CONSTANTES.CLIENTE, cliente);
                intent.putExtra(CONSTANTES.NOMBRE_CLIENTE, nombreCliente);
                intent.putExtra(CONSTANTES.FACTURAS_PARCIALES, mFacturas);
                //TODO: cuando se envíe la informacion de los rechazos, unicamente enviar los que tienen itemRuta.idRowRef <> 0
                startActivityForResult(intent, FINALIZA_ENTREGA_PARCIAL_INTENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == FINALIZA_ENTREGA_PARCIAL_INTENT)
        {
            if (resultCode == RESULT_OK)
            {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//
//    private void configuraBuscador(Menu menu)
//    {
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
//        if (null != searchView)
//        {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//            searchView.setIconifiedByDefault(false);
//        }
//
//        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
//        {
//            public boolean onQueryTextChange(String newText)
//            {
//                if (TextUtils.isEmpty(newText))
//                {
//                    resetSearch(); // reset
//                }
//                else
//                {
//                    beginSearch(newText); // busqueda
//                }
//                Log.e("Text", newText);
//                return false;
//            }
//
//            public boolean onQueryTextSubmit(String query)
//            {
//                return false;
//            }
//        };
//        searchView.setOnQueryTextListener(queryTextListener);
//    }

    //implementar buscador en menu
//    public void beginSearch(String query)
//    {
//        Log.e("QueryFragment", query);
//        if (adapter != null)
//        {
//            adapter.getFilter().filter(query);
//        }
//    }
//
//    //implementar buscador en menu
//    public void resetSearch()
//    {
//        if (adapter != null)
//        {
//            adapter.getFilter().filter("");
//        }
//    }

}
