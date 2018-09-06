package com.dynamicsoftware.pocho.logistica.DAO.DbSchema;

import com.dynamicsoftware.pocho.logistica.DAO.Contracts.FacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.MotivoRechazoFacturaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.RutaDeEntregaContract;
import com.dynamicsoftware.pocho.logistica.DAO.Contracts.UsuariosContract;

/**
 * Created by Pocho on 14/06/2017.
 */

public class MockData
{
    public static String Usuarios = "INSERT INTO " + UsuariosContract.Usuarios.TABLE_NAME + " (" +
            UsuariosContract.Usuarios._USUARIO + "," +
            UsuariosContract.Usuarios._CONTRASEÑA + ") VALUES (" +
            "'F0032','')";

    public static String RutaDeEntrega = "insert into " + RutaDeEntregaContract.RutaDeEntrega.TABLE_NAME + "(" +
            RutaDeEntregaContract.RutaDeEntrega._FLETERO + "," +
            RutaDeEntregaContract.RutaDeEntrega._CLIENTE + "," +
            RutaDeEntregaContract.RutaDeEntrega._NOMBRE + "," +
            RutaDeEntregaContract.RutaDeEntrega._DOMICILIO + "," +
            RutaDeEntregaContract.RutaDeEntrega._EFECTIVO + "," +
            RutaDeEntregaContract.RutaDeEntrega._CTACTE + "," +
            RutaDeEntregaContract.RutaDeEntrega._ESTADO + "," +
            RutaDeEntregaContract.RutaDeEntrega._ORDEN_VISITA + ") values " +
            "('F0032','03864','RAMIREZ PEDRO FRANCISCO','Constitucion Av.  5716',308.79,0,0,0)," +
            "('F0032','09952','MEMAIVI S.R.L.','Constitucion  6385',599.64,0,0,0)," +
            "('F0032','12814','ARROYO MALVINA SOLEDAD','AV. CONSTITUCION    5680',433.78,0,0,0)," +
            "('F0032','12966','HENRIK PABLO PEDRO','Acevedo  6108',6730.81,0,0,0)," +
            "('F0032','16972','GUERRERO PEVE MARILUZ MIRELLA','Constitucion Av.  5680',449.06,0,0,0)," +
            "('F0032','18205','ROMANO ROMAN','MARTINEZ ZUVIRIA  1283',743.11,0,0,0)," +
            "('F0032','19271','CHIN JINDI','CARBALLO 2190',5191.88,0,0,0)," +
            "('F0032','26219','PORFIRI MARTIN ANDRES','Acevedo  6116',894.85,0,0,0)," +
            "('F0032','59013','BIDEGAIN MARTIN','Constitucion Av.  6693',0,7268.63,0,0)," +
            "('F0032','70084','PALAVECINO SEGUNDA GRACIELA','Constitucion Av.  5215',216.67,0,0,0)," +
            "('F0032','70305','BURGOS ROBERTO JAVIER','Constitucion Av.  6635',2245.13,0,0,0)," +
            "('F0032','75182','COPPOLA SERGIO MARCELO','Constitucion  6584',241.32,0,0,0)," +
            "('F0032','86092','PERSICO EDGARDO DANIEL','CONSTITUCION AV.  6110',1742.8,0,0,0)," +
            "('F0032','97524','OLIVERIO OLGA NELIDA','CARBALLO 508',2269.87,0,0,0)," +
            "('F0032','A6312','BRICENO MANZANARE ISABET KARIN','Constitucion Av.  6878',432.91,0,0,0)," +
            "('F0032','B7104','GARASA MARCELA VANESA','Constitucion Nº 5455',7163.52,0,0,0)," +
            "('F0032','B8245','MERLOTTO AGUSTIN','Gonzalez J.V. Nº 1879',4098.39,0,0,0)," +
            "('F0032','B8800','GOMEZ MARTIN RICARDO','Carballo Nº 589',3861.4,0,0,0)," +
            "('F0032','B9606','BATISTA DE LOS SANTOS ELDA','Constitucion Nº 6313 Piso 0 Dp',2144.4,0,0,0)," +
            "('F0032','B9612','CALDIE BEER SRL','Constitucion Nº 5614',2219.74,0,0,0)," +
            "('F0032','C4193','HUANG XIUPING','Constitucion  6745',4450.35,0,0,0)," +
            "('F0032','C4420','NIETO GRACIELA','Constitucion Av.  6866',195.99,0,0,0)," +
            "('F0032','C5226','BENITO JORGE EDUARDO','Constitucion Av.  5130',352.79,0,0,0)," +
            "('F0032','C7416','LOGISTICA ARAVIS','Constitucion Av.  6213',4276.92,0,0,0)," +
            "('F0032','C8895','OCARANZA MARIANO ALBERTO','Constitucion  5402',3497.35,0,0,0)";

    public static String Facturas = "insert into " + FacturaContract.Factura.TABLE_NAME + " (" +
            FacturaContract.Factura._CLIENTE + "," +
            FacturaContract.Factura._TIPO + "," +
            FacturaContract.Factura._NUMERO + "," +
            FacturaContract.Factura._EMPRESA + "," +
            FacturaContract.Factura._PERCEPCION_IIBB_TOTAL + "," +
            FacturaContract.Factura._IVA_BASICO_TOTAL + "," +
            FacturaContract.Factura._IVA_ADICIONAL_TOTAL + "," +
            FacturaContract.Factura._IMPUESTO_INTERNO_TOTAL + "," +
            FacturaContract.Factura._REPARTO + "," +
            FacturaContract.Factura._CONDICION_VENTA + "," +
            FacturaContract.Factura._SUBTOTAL + "," +
            FacturaContract.Factura._TOTAL + ") values " +
            "('03864','FA','0021-00049148','01',0,2.08,0,0,'00282075',1,9.92,12)," +
            "('09952','FA','0021-00049149','01',7.38,99.97,0,16.26,'00282075',1,476.03,599.64)," +
            "('12814','FA','0021-00049150','01',17.29,70.73,0,8.98,'00282075',1,336.79,433.78)," +
            "('12966','FB','0013-00012875','01',0,35.48,0,6.12,'00282075',1,168.94,210.54)," +
            "('16972','FA','0021-00049151','01',12.69,73.78,0,11.23,'00282075',1,351.35,449.06)," +
            "('19271','FA','0021-00049152','01',39.78,229.29,0,44.72,'00282075',1,1091.85,1405.64)," +
            "('26219','FB','0021-00045564','01',25.56,139.01,0,68.3,'00282075',1,661.97,894.85)," +
            "('59013','FA','0021-00049153','01',0,868.41,0,415.03,'00282075',1,4135.27,5418.7)," +
            "('70084','FB','0021-00045565','01',6.13,35.48,0,6.12,'00282075',1,168.94,216.67)," +
            "('70305','FA','0021-00049154','01',31.34,185.43,0,12.5,'00282075',1,882.99,1112.26)," +
            "('75182','FB','0021-00045566','01',6.82,39.66,0,5.99,'00282075',1,188.85,241.32)," +
            "('86092','FB','0021-00045567','01',25.55,141.25,0,57.37,'00282075',1,672.64,896.82)," +
            "('97524','FB','0013-00012876','01',0,144.31,0,7.14,'00282075',1,687.19,838.64)," +
            "('97524','FB','0013-00012877','01',0,76.37,0,0,'00282075',1,363.66,440.03)," +
            "('B7104','FA','0021-00049155','01',15.88,211.61,0,50.86,'00282075',1,1007.67,1286.02)," +
            "('B8245','FB','0021-00045568','01',15,86.54,0,16.58,'00282075',1,412.11,530.24)," +
            "('B8800','FB','0021-00045569','01',77.04,404.22,0,276.32,'00282075',1,1924.83,2682.41)," +
            "('C4193','FA','0021-00049157','01',57.16,341.27,0,8.16,'00282075',1,1625.08,2031.67)," +
            "('C4420','FB','0021-00045570','01',7.82,31.84,0,4.73,'00282075',1,151.6,195.99)," +
            "('C7416','FA','0021-00049158','01',45.45,267.04,0,27.07,'00282075',1,1271.62,1611.19)," +
            "('C8895','FA','0021-00049159','01',45.14,151.92,0,28.93,'00282075',0,723.41,949.4)," +
            "('03864','FA','0003-00009331','10',11.93,46.3,0,18.09,'00282100',1,220.47,296.79)," +
            "('12966','FB','0003-00009851','10',0,432.92,0,211.13,'00282100',1,2061.51,2705.55)," +
            "('12966','FB','0003-00009852','10',0,166.89,0,69.11,'00282100',1,794.74,1030.75)," +
            "('12966','FB','0003-00009853','10',0,423.48,0,343.92,'00282100',1,2016.57,2783.97)," +
            "('18205','FB','0003-00009855','10',17.8,106.78,0,0,'00282100',1,508.5,633.08)," +
            "('18205','FB','0003-00009856','10',3.13,17.34,0,6.99,'00282100',1,82.57,110.03)," +
            "('19271','FA','0003-00009332','10',7.56,45.35,0,0,'00282100',1,215.94,268.85)," +
            "('19271','FA','0003-00009333','10',47.51,263.44,0,103.07,'00282100',1,1254.49,1668.52)," +
            "('19271','FA','0003-00009334','10',18.14,99.75,0,43.28,'00282100',1,475,636.17)," +
            "('19271','FA','0003-00009335','10',34.65,188,0,94.82,'00282100',1,895.23,1212.7)," +
            "('59013','FA','0003-00009336','10',0,140.66,0,63.12,'00282100',1,669.81,873.58)," +
            "('59013','FA','0003-00009337','10',0,157.29,0,70.04,'00282100',1,749.01,976.35)," +
            "('70305','FA','0003-00009338','10',17.69,92.94,0,62.93,'00282100',1,442.55,616.11)," +
            "('70305','FA','0003-00009339','10',14.74,80.97,0,35.46,'00282100',1,385.59,516.76)," +
            "('86092','FB','0003-00009857','10',21.22,118.4,0,42.41,'00282100',1,563.83,745.86)," +
            "('86092','FB','0003-00009858','10',2.86,15.52,0,7.83,'00282100',1,73.91,100.12)," +
            "('97524','FB','0003-00009854','10',0,160.61,0,65.8,'00282100',1,764.8,991.2)," +
            "('A6312','FB','0003-00009859','10',20.74,66.44,0,29.36,'00282100',1,316.37,432.91)," +
            "('B7104','FA','0003-00009340','10',33.14,424.78,0,186.29,'00282100',1,2022.76,2666.97)," +
            "('B7104','FA','0003-00009341','10',6.26,80.59,0,33.38,'00282100',1,383.77,504)," +
            "('B7104','FA','0003-00009342','10',27.37,350.52,0,155.5,'00282100',1,1669.14,2202.53)," +
            "('B7104','FA','0003-00009343','10',6.26,80.59,0,33.38,'00282100',1,383.77,504)," +
            "('B8245','RM','0001-00006687','10',0,0,0,0,'00282100',1,0,0)," +
            "('B8245','FB','0003-00009860','10',67.09,369.39,0,157.75,'00282100',1,1759,2353.22)," +
            "('B8245','FB','0003-00009861','10',34.65,190.2,0,84.36,'00282100',1,905.72,1214.93)," +
            "('B8800','FB','0003-00009862','10',23.39,123.19,0,81.59,'00282100',1,586.6,814.76)," +
            "('B8800','FB','0003-00009863','10',10.24,61.44,0,0,'00282100',1,292.56,364.23)," +
            "('B9606','FB','0003-00009864','10',20.87,116.65,0,40.68,'00282100',1,555.49,733.68)," +
            "('B9606','FB','0003-00009865','10',18.01,100.74,0,34.94,'00282100',1,479.73,633.43)," +
            "('B9606','FB','0003-00009866','10',22.17,121.62,0,54.34,'00282100',1,579.15,777.29)," +
            "('C4193','FA','0003-00009344','10',19.08,104.54,0,47.48,'00282100',1,497.8,668.91)," +
            "('C4193','FA','0003-00009345','10',31.19,172.9,0,67.85,'00282100',1,823.32,1095.25)," +
            "('C4193','FA','0003-00009346','10',6.79,40.72,0,0,'00282100',1,193.9,241.41)," +
            "('C4193','FA','0003-00009347','10',11.77,64.98,0,26.92,'00282100',1,309.44,413.11)," +
            "('C4193','FA','0003-00009348','10',0,0,0,0,'00282100',1,0,0)," +
            "('C5226','FB','0003-00009867','10',9.98,57.62,0,10.8,'00282100',1,274.39,352.79)," +
            "('C7416','FA','0003-00009349','10',14.1,76.69,0,37.64,'00282100',1,365.19,493.62)," +
            "('C7416','FA','0003-00009350','10',43.24,220.94,0,183.42,'00282100',1,1052.1,1499.71)," +
            "('C7416','FA','0003-00009351','10',19.31,101.33,0,69.23,'00282100',1,482.52,672.4)," +
            "('C8895','FA','0003-00009352','10',31.44,110.05,0,0,'00282100',1,524.03,665.52)," +
            "('C8895','FA','0003-00009353','10',88.93,311.27,0,0,'00282100',1,1482.23,1882.43)";

    public static String MotivoRechazoFactura = "INSERT INTO " + MotivoRechazoFacturaContract.MotivoRechazoFactura.TABLE_NAME + " (" +
                                                MotivoRechazoFacturaContract.MotivoRechazoFactura._CODIGO + "," +
                                                MotivoRechazoFacturaContract.MotivoRechazoFactura._DESCRIPCION +", "+
                                                MotivoRechazoFacturaContract.MotivoRechazoFactura._PRIORIDAD + ") VALUES " +
                                                "('002','No tiene dinero',1)," +
                                                "('666','No quiso recibir',2)," +
                                                "('001','No habia pedido',3)," +
                                                "('003','No era la mercaderia pedida',4)," +
                                                "('028','No es el precio acordado',5)," +
                                                "('021','Faltante - Sin stock',6)," +
                                                "('004','No estaba el responsable',7)," +
                                                "('012','No recibe por cambios',8)," +
                                                "('024','Disconforme por SOP',9)," +
                                                "('015','No tenia envases',10)," +
                                                "('009','Rotura de mercaderia',11)," +
                                                "('023','No llevo el sin cargo',12)," +
                                                "('008','Esta mal facturado',13)," +
                                                "('020','Error de impresion',14)";
//                                                "('011','Cerrado',15)," +
//                                                "('006','Sin visitar',16)";
    //"('016','Difiere razon social')," +
    //"('019','Error carga deposito')," +
    //"('010','Fuera de zona')," +
    //"('007','La direccion no existe')," +
    //"('027','No recibe, fecha corta')," +
    //"('018','Refacturacion')," +
    //"('022','Zona peligrosa')";
    //"('013','Difiere fecha de entrega')," +
    //"('014','Fuera de horario')," +
    //"('005','No recibe entregas parciales')," +
    //"('026','No retira, no tiene la mercaderia')," +
    //"('017','Pedido repetido')," +
    //"('025','Tiene mercaderia')," +
    //"('999','Por retiro de mercaderia')," +
}
