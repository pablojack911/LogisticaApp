package com.dynamicsoftware.pocho.logistica;

/**
 * Created by Pocho on 11/04/2017.
 */

public class CONSTANTES
{

    public static final String USUARIO = "usuario";
    public static final String USUARIO_CONECTADO = "usuario-conectado";
    public static final String LAST_DOWNLOAD = "last-download";
    public static final String LAST_KNOWN_LOCATION = "last-known-location";
    public static final String PREFERENCIAS = "preference_file_key";
    public static final String RUTA_DE_ENTREGA = "ruta-de-entrega-key";
    public static final String CAMBIA_ESTADO_ENTREGA = "cambia-estado-entrega-key";
    public static final String POSICION_GPS_KEY = "posicion-gps-key";
    //REAL
//    public static final String URL_DESCARGA = "http://mhergo.ddns.net:8888/hergomobile/api/fletero/";
//    public static final String URL_ENVIA_POS = "http://mhergo.ddns.net:8888/hergomobile/api/fletero/";
//    public static final String URL_FINALIZA_REPARTO = "http://mhergo.ddns.net:8888/hergomobile/api/fletero/finalizar/";
//    public static final String URL_FINALIZA_ENTREGA = "http://mhergo.ddns.net:8888/hergomobile/api/fletero/finalizarEntrega/";
//    public static final String URL_FINALIZA_DESCARGA = "http://mhergo.ddns.net:8888/hergomobile/api/fletero/RecibioOK/";
    //TEST
    public static final String URL_LOGIN = "http://mhergo.ddns.net:8888/hergomobiletest/api/loginfletero/";
    public static final String URL_DESCARGA = "http://mhergo.ddns.net:8888/hergomobiletest/api/fletero/";
    public static final String URL_ENVIA_POS = "http://mhergo.ddns.net:8888/hergomobiletest/api/fletero/TrackingGPS";
    public static final String URL_FINALIZA_REPARTO = "http://mhergo.ddns.net:8888/hergomobiletest/api/fletero/finalizar/";
    public static final String URL_FINALIZA_ENTREGA = "http://mhergo.ddns.net:8888/hergomobiletest/api/fletero/finalizarEntrega/";
    public static final String URL_FINALIZA_DESCARGA = "http://mhergo.ddns.net:8888/hergomobiletest/api/fletero/RecibioOK/";
    //
    public static final int REQUEST_PERMISSION_CODE = 2;
    public static final String CLIENTE = "cliente-key";
    public static final String NOMBRE_CLIENTE = "nombre-cliente-key";
    public static final int ENTREGA_PARCIAL_INTENT = 1;
    public static final int FINALIZA_ENTREGA_PARCIAL_INTENT = 2;
    public static final int CLIENTES_VISITA_MULTIPLE_INTENT = 3;
    public static final int PEDIDO_RECHAZADO_INTENT = 4;
    public static final String ID_RUTA_DE_ENTREGA = "ruta-entrega-id-key";
    public static final String CLIENTES_VISITA_MULTIPLE = "multi-clientes-visita";

    public static final String FACTURAS_PARCIALES = "facturas-parciales-key";
    public static final String EMPRESA_MAYORISTA = "03";
    public static final String MOTIVO_RECHAZADO_SELECCIONADO = "motivo-seleccionado-key";

    public static final int TIMEOUT = 3600*1000;
}
