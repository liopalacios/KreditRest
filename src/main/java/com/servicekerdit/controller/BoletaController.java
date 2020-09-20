package com.servicekerdit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.model.BoletaSunat;
import com.servicekerdit.model.CampoAdicional;
import com.servicekerdit.model.DetalleSunat;
import com.servicekerdit.model.ResponseMultinet;
import com.servicekerdit.service.BoletaDetalleService;
import com.servicekerdit.service.BoletaService;
import com.servicekerdit.service.ClienteService;
import com.google.common.collect.ImmutableMap;
import com.servicekerdit.service.MovimientoCasaCambioService;
import com.servicekerdit.service.impl.BoletaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class BoletaController {
    private static final String  TIPODOCUMENTO = "03";
    HashMap<String, String> fieldsProducts;
    public BoletaController() {
        fieldsProducts = new HashMap<String, String>();
        fieldsProducts.put("01","CONTRATO PIGNORATICIO");
        fieldsProducts.put("02","DSCTO. POR PLANILLA");
        fieldsProducts.put("03","CONTRATO POR CONSUMO");
        fieldsProducts.put("04","PRESTAMO VEHICULAR");
        fieldsProducts.put("05","PRESTAMO MICRO");
        fieldsProducts.put("06","PRESTAMO INDIVIDUAL C/GARANTIA");
        fieldsProducts.put("07","PRESTAMO INDIVIDUAL S/GARANTIA");
        fieldsProducts.put("99","VARIOS");
    }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String NIU = "ZZ";
    private static final int CANTIDAD = 1;
    private static final String TIGV = "10";
    private static final int PIGV = 18;

    @Value("${storage.auth}")
    private String authtorization;

    @Value("${storage.server}")
    private String urlApiStorage;

    @Value("${storage.port}")
    private String portApiStorage;

    @Value("${apiserviceweb.server}")
    private String urlApiServiceWeb;

    @Value("${api.envio.comprobante}")
    private String urlApiEnvComprobante;

    @Value("${api.recibo.numero}")
    private String urlApiReciboNumero;

    @Autowired
    BoletaService boletaService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    BoletaDetalleService boletaDetalleService;

    @Autowired
    MovimientoCasaCambioService movimientoCasaCambioService;



    public Map<String, Object> sendBoleta( BoletaKredit boletaKredit) throws JsonProcessingException {
        String moneda = "";
        String monedaSmbl = "";
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity;
        LinkedMultiValueMap<String, Object> map;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<CampoAdicional> fieldsSunats= new ArrayList<>();
        //BoletaKredit boletaKredit = boletaService.findBoletaKreditBySereid(nucomprobante);
        BoletaKredit boletaKredit1 = new BoletaKredit();
        String cajero = "";
        String detalleproducto = "";
        if( Integer.valueOf(boletaKredit.getIdcrearegistro() )== 7){
            cajero = " VITALIA GALVEZ ";
        }else{
            cajero = "PAMELA DEL CARPIO ";
        }

        BoletaDetalleKredit boletaDetalleKredit = boletaDetalleService.findBySerieidAndComprobanteid(boletaKredit.getSerieid(), boletaKredit.getComprobanteid(),boletaKredit.getCotipopago());
        ArrayList<DetalleSunat> detalleSunats= new ArrayList<>();
        System.out.print(boletaKredit.toString()+"\n");
        if(Integer.parseInt(boletaKredit.getComoneda())>1){
            moneda = "USD";
            monedaSmbl = "$";
        }else{
            moneda = "PEN";
            monedaSmbl = "S/";
        }
        int resta = 0;
        System.out.println(" OFICINA "+boletaKredit.getOficina());
        if(Integer.parseInt(boletaKredit.getOficina())>1){
            resta = 38096;
        }else{
            resta = 45619;
        }
        String tipoDoc = "4";
        if(Integer.valueOf(boletaKredit.getClienteid().getDocumentotipo().trim())==6){
            tipoDoc="4";
        }else if(Integer.valueOf(boletaKredit.getClienteid().getDocumentotipo().trim())==3){
            tipoDoc="6";
        }else if(Integer.valueOf(boletaKredit.getClienteid().getDocumentotipo().trim())==1){
            tipoDoc="1";
        }
        Double dbs = (boletaDetalleKredit.getVaamortizado());
        if (dbs <0){
            detalleproducto = " "+ boletaDetalleKredit.getVaamortizado();
        }
        double subtotal = boletaDetalleKredit.getVainteres()+boletaDetalleKredit.getVagastoadmin()+boletaDetalleKredit.getVatranscustodia()+boletaDetalleKredit.getVamora()-boletaDetalleKredit.getVadescuento();
        double igvglobal = subtotal*0.18;
        double subtotaligv = subtotal+igvglobal;
        double totalglobal = subtotal+igvglobal;
        double totalglobalsub = totalglobal+boletaDetalleKredit.getVaprima()+boletaDetalleKredit.getOtrointeres()+boletaDetalleKredit.getVacomision();
        int num = getNumeroSiguiente(TIPODOCUMENTO,"B"+boletaKredit.getSerieid());
        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setDocumento_tipo_comprobante("03");
        boletaSunat.setDocumento_serie("B"+boletaKredit.getSerieid());
        boletaSunat.setDocumento_numero(num);
        boletaSunat.setDocumento_fecha_emision(df.format(boletaKredit.getFechaimp()));
        boletaSunat.setDocumento_hora_emision(boletaKredit.getHoregistro());

        boletaSunat.setDocumento_fecha_vencimiento(df.format(boletaDetalleKredit.getFechavencimiento()));
        boletaSunat.setDocumento_tipo_moneda(moneda);

        boletaSunat.setCliente_tipo_documento(tipoDoc);
        boletaSunat.setCliente_numero_documento(boletaKredit.getClienteid().getDocumento());
        boletaSunat.setCliente_denominacion(boletaKredit.getClienteid().getApellidop()+" "+boletaKredit.getClienteid().getApellidom()+" "+boletaKredit.getClienteid().getNombre());
        boletaSunat.setCliente_direccion(boletaKredit.getClienteid().getDireccion());
        boletaSunat.setCliente_email(null);

        boletaSunat.setTotal_venta_exoneradas(null);
        boletaSunat.setTotal_venta_gravadas(convertDecimal(subtotal));
        boletaSunat.setTotal_igv(convertDecimal(igvglobal));
        //boletaSunat.setTotal_venta_exoneradas(convertDecimal(boletaKredit.getToamortizado()));

        //boletaSunat.setTotal_isc(null);
        boletaSunat.setTotal_otros_tributos(null);
        boletaSunat.setTotal_descuento(convertDecimal(boletaDetalleKredit.getVadescuento()));
        boletaSunat.setTotal_otros_cargos(null);

        boletaSunat.setTotal_otros_cargos(null);
        boletaSunat.setImporte_total(convertDecimal(totalglobal ));
        boletaSunat.setTipo_operacion("01");
        boletaSunat.setTipo_nota_credito(null);
        boletaSunat.setTipo_nota_debito(null);

        boletaSunat.setDocumento_afectado_serie(null);
        boletaSunat.setDocumento_afectado_numero(0);
        boletaSunat.setDocumento_afectado_tipo_comprobante(null);
        boletaSunat.getDocumento_afectado_motivo_nota();
        boletaSunat.setOrden_compra(null);
        boletaSunat.setDetraccion(null);
        boletaSunat.setPorcentaje_detraccion(null);
        //boletaSunat.setDocumento_fecha_emision(DATE_FORMAT.format(boletaKredit.getFechaimp()));
        //"total_imp_ope_grat": null,
        //"detraccion": "N",
        DetalleSunat detalleSunat = new DetalleSunat();
        DetalleSunat detalleSunatInteres = new DetalleSunat();
        DetalleSunat detalleSunatAdmin = new DetalleSunat();
        DetalleSunat detalleSunatTransporte = new DetalleSunat();
        DetalleSunat detalleSunatMora = new DetalleSunat();
        //detalleSunat.setTextAreaDescription(false);
        /*
        detalleSunat.setUnidad_medida(NIU);
        detalleSunat.setCantidad(new BigDecimal(CANTIDAD));
        detalleSunat.setTipo_afectacion_igv(TIGV);
        detalleSunat.setCodigo_producto_interno(boletaDetalleKredit.getProducto().getProductoid());
        detalleSunat.setDescripcion("PAGO "+boletaDetalleKredit.getProducto().getNomcorto());
        detalleSunat.setIgv(convertDecimal(boletaDetalleKredit.getVaigv()));
        detalleSunat.setValor_unitario(convertDecimal(boletaDetalleKredit.getVatotalpago()-boletaKredit.getToamortizado()-boletaDetalleKredit.getVaigv()));
        detalleSunat.setMonto_base_igv(convertDecimal(boletaDetalleKredit.getVaigv()));
        detalleSunat.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVatotalpago()-boletaKredit.getToamortizado()));
        detalleSunat.setValor_venta(convertDecimal(boletaDetalleKredit.getVatotalpago()-boletaKredit.getToamortizado()-boletaDetalleKredit.getVaigv()));
        detalleSunat.setPorcentaje_igv(new BigDecimal(PIGV));
        //detalleSunat.setMonto_base_exonerado(convertDecimal(boletaDetalleKredit.getVaamortizado()));
        detalleSunat.setMonto_base_exonerado(null);*/

        detalleSunatInteres.setCantidad(new BigDecimal(CANTIDAD));
        detalleSunatInteres.setCodigo_producto_interno(boletaDetalleKredit.getProducto().getProductoid());
        detalleSunatInteres.setDescripcion("INTERESES ");
        detalleSunatInteres.setHabilitarTipo(true);
        detalleSunatInteres.setIgv(convertDecimal(boletaDetalleKredit.getVainteres()*0.18));
        detalleSunatInteres.setMonto_base_igv(convertDecimal(boletaDetalleKredit.getVainteres()));
        detalleSunatInteres.setInc_igv(false);
        detalleSunatInteres.setMonto_base_inafecto(null);
        detalleSunatInteres.setMostraPorc(false);
        detalleSunatInteres.setPrecio_total(convertDecimal(boletaDetalleKredit.getVainteres()+boletaDetalleKredit.getVainteres()*0.18));
        detalleSunatInteres.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVainteres()+boletaDetalleKredit.getVainteres()*0.18));
        detalleSunatInteres.setTipo_afectacion_igv(TIGV);
        detalleSunatInteres.setUnidad_medida(NIU);
        detalleSunatInteres.setValor_unitario(convertDecimal(boletaDetalleKredit.getVainteres()));
        detalleSunatInteres.setValor_venta(convertDecimal(boletaDetalleKredit.getVainteres()));
        detalleSunatInteres.setImp_vta_grat(null);
        detalleSunatInteres.setIsTextAreaDescription(false);
        detalleSunatInteres.setMonto_base_exonerado(null);
        detalleSunatInteres.setMonto_base_gratuito(null);
        detalleSunatInteres.setMostrarTransporte("01");
        detalleSunatInteres.setPorcentaje_igv(new BigDecimal(18));
        detalleSunatInteres.setPorcentaje_trib_vta_grat(null);
        detalleSunatInteres.setValor_referencial_unitario(null);
        detalleSunats.add(detalleSunatInteres);

        detalleSunatAdmin.setCantidad(new BigDecimal(CANTIDAD));
        detalleSunatAdmin.setCodigo_producto_interno("02");
        detalleSunatAdmin.setDescripcion("ADMINISTRATIVOS ");
        detalleSunatAdmin.setHabilitarTipo(true);
        detalleSunatAdmin.setIgv(convertDecimal(boletaDetalleKredit.getVagastoadmin()*.018));
        detalleSunatAdmin.setMonto_base_igv(convertDecimal(boletaDetalleKredit.getVagastoadmin()));
        detalleSunatAdmin.setInc_igv(false);
        detalleSunatAdmin.setMonto_base_inafecto(null);
        detalleSunatAdmin.setMostraPorc(false);
        detalleSunatAdmin.setPrecio_total(convertDecimal(boletaDetalleKredit.getVagastoadmin()+boletaDetalleKredit.getVagastoadmin()*0.18));
        detalleSunatAdmin.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVagastoadmin()+boletaDetalleKredit.getVagastoadmin()*0.18));
        detalleSunatAdmin.setTipo_afectacion_igv(TIGV);
        detalleSunatAdmin.setUnidad_medida(NIU);
        detalleSunatAdmin.setValor_unitario(convertDecimal(boletaDetalleKredit.getVagastoadmin()));
        detalleSunatAdmin.setValor_venta(convertDecimal(boletaDetalleKredit.getVagastoadmin()));
        detalleSunatAdmin.setImp_vta_grat(null);
        detalleSunatAdmin.setIsTextAreaDescription(false);
        detalleSunatAdmin.setMonto_base_exonerado(null);
        detalleSunatAdmin.setMonto_base_gratuito(null);
        detalleSunatAdmin.setMostrarTransporte("01");
        detalleSunatAdmin.setPorcentaje_igv(new BigDecimal(18));
        detalleSunatAdmin.setPorcentaje_trib_vta_grat(null);
        detalleSunatAdmin.setValor_referencial_unitario(null);
        detalleSunats.add(detalleSunatAdmin);

        detalleSunatTransporte.setCantidad(new BigDecimal(CANTIDAD));
        detalleSunatTransporte.setCodigo_producto_interno("03");
        detalleSunatTransporte.setDescripcion("TRANSP. CUSTODIA ");
        detalleSunatTransporte.setHabilitarTipo(true);
        detalleSunatTransporte.setIgv(convertDecimal(boletaDetalleKredit.getVatranscustodia()*0.18));
        detalleSunatTransporte.setMonto_base_igv(convertDecimal(boletaDetalleKredit.getVatranscustodia()));
        detalleSunatTransporte.setInc_igv(false);
        detalleSunatTransporte.setMonto_base_inafecto(null);
        detalleSunatTransporte.setMostraPorc(false);
        detalleSunatTransporte.setPrecio_total(convertDecimal(boletaDetalleKredit.getVatranscustodia()+boletaDetalleKredit.getVatranscustodia()*0.18));
        detalleSunatTransporte.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVatranscustodia()+boletaDetalleKredit.getVatranscustodia()*0.18));
        detalleSunatTransporte.setTipo_afectacion_igv(TIGV);
        detalleSunatTransporte.setUnidad_medida(NIU);
        detalleSunatTransporte.setValor_unitario(convertDecimal(boletaDetalleKredit.getVatranscustodia()));
        detalleSunatTransporte.setValor_venta(convertDecimal(boletaDetalleKredit.getVatranscustodia()));
        detalleSunatTransporte.setImp_vta_grat(null);
        detalleSunatTransporte.setIsTextAreaDescription(false);
        detalleSunatTransporte.setMonto_base_exonerado(null);
        detalleSunatTransporte.setMonto_base_gratuito(null);
        detalleSunatTransporte.setMostrarTransporte("01");
        detalleSunatTransporte.setPorcentaje_igv(new BigDecimal(18));
        detalleSunatTransporte.setPorcentaje_trib_vta_grat(null);
        detalleSunatTransporte.setValor_referencial_unitario(null);
        detalleSunats.add(detalleSunatTransporte);

        detalleSunatMora.setCantidad(new BigDecimal(CANTIDAD));
        detalleSunatMora.setCodigo_producto_interno("04");
        detalleSunatMora.setDescripcion("MORA");
        detalleSunatMora.setHabilitarTipo(true);
        detalleSunatMora.setIgv(convertDecimal(boletaDetalleKredit.getVamora()*0.18));
        detalleSunatMora.setMonto_base_igv(convertDecimal(boletaDetalleKredit.getVamora()));
        detalleSunatMora.setInc_igv(false);
        detalleSunatMora.setMonto_base_inafecto(null);
        detalleSunatMora.setMostraPorc(false);
        detalleSunatMora.setPrecio_total(convertDecimal(boletaDetalleKredit.getVamora()+boletaDetalleKredit.getVamora()*0.18));
        detalleSunatMora.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVamora()+boletaDetalleKredit.getVamora()*0.18));
        detalleSunatMora.setTipo_afectacion_igv(TIGV);
        detalleSunatMora.setUnidad_medida(NIU);
        detalleSunatMora.setValor_unitario(convertDecimal(boletaDetalleKredit.getVamora()));
        detalleSunatMora.setValor_venta(convertDecimal(boletaDetalleKredit.getVamora()));
        detalleSunatMora.setImp_vta_grat(null);
        detalleSunatMora.setIsTextAreaDescription(false);
        detalleSunatMora.setMonto_base_exonerado(null);
        detalleSunatMora.setMonto_base_gratuito(null);
        detalleSunatMora.setMostrarTransporte("01");
        detalleSunatMora.setPorcentaje_igv(new BigDecimal(18));
        detalleSunatMora.setPorcentaje_trib_vta_grat(null);
        detalleSunatMora.setValor_referencial_unitario(null);
        detalleSunats.add(detalleSunatMora);


        CampoAdicional fieldsSunat = null;

        HashMap<String, String> fieldsAddPayment = new HashMap<String, String>();
        fieldsAddPayment.put("TIPO DE PRESTAMO",fieldsProducts.get(boletaKredit.getProducto()));
        fieldsAddPayment.put("CAJERO",cajero);
        fieldsAddPayment.put("F. VENCIMIENTO",df.format(boletaDetalleKredit.getFechavencimiento()));
        fieldsAddPayment.put("PAGO TOTAL PROCESADO ",monedaSmbl+" "+(convertDecimal(totalglobalsub + boletaKredit.getToamortizado())).toString());
        if (boletaKredit.getToamortizado()>0){
            fieldsAddPayment.put("AMORTIZACION",monedaSmbl+" "+boletaKredit.getToamortizado());
        }
        if (boletaDetalleKredit.getVacomision()>0){
            fieldsAddPayment.put("COMISION",monedaSmbl+" "+boletaDetalleKredit.getVacomision());
        }
        if (boletaDetalleKredit.getVaprima()>0){
            fieldsAddPayment.put("PRIMA",monedaSmbl+" "+boletaDetalleKredit.getVaprima());
        }
        fieldsAddPayment.put("TIPO DE PAGO",boletaKredit.getTipopago().getDetalle());
        fieldsAddPayment.put("CONTRATO",boletaKredit.getNucontrato());
        if (boletaKredit.getCuota()>0){
            fieldsAddPayment.put("N° CUOTA",boletaKredit.getCuota()==0?"":Integer.toString(boletaKredit.getCuota()));
        }

        fieldsAddPayment.put("SALDO",(convertDecimal(boletaDetalleKredit.getVacapital() - boletaDetalleKredit.getVaamortizado())).toString());

        for (String i : fieldsAddPayment.keySet()) {
            fieldsSunat = new CampoAdicional();
            fieldsSunat.setNombre_campo(i);
            fieldsSunat.setValor_campo(fieldsAddPayment.get(i));
            fieldsSunats.add(fieldsSunat);
        }

        boletaSunat.setCampos_adicionales(fieldsSunats);
        boletaSunat.setItems(detalleSunats);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(boletaSunat);
        System.out.print(json);
        System.out.print("\n");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<BoletaSunat> requestEntities = new HttpEntity<BoletaSunat>(boletaSunat, getHttpHeaders());
        ResponseEntity<ResponseMultinet> response = restTemplate.exchange(urlApiEnvComprobante, HttpMethod.POST, requestEntities,ResponseMultinet.class);



        System.out.print(response.getBody());

        if(response.getStatusCodeValue()==200){
            boletaKredit.setEstadoenv(1);
            if(response.getBody().getUrlPdfTicket()!=null){
                if(response.getBody().getUrlPdfTicket()!=""){
                    boletaKredit.setEstadomnsag(1);
                    boletaKredit.setMnsagsunat(response.getBody().getMensaje());
                    boletaKredit.setLinkticket(response.getBody().getUrlPdfTicket());
                    boletaKredit.setLinkpdf(response.getBody().getUrlPdfA4());
                    boletaKredit.setLinkxml(response.getBody().getUrlXml());
                    boletaKredit.setIdvoucher(boletaSunat.getDocumento_numero());
                }else {
                    String cadena = response.getBody().getMensaje();
                    int resultado = cadena.indexOf("registrado");

                    if(resultado != -1) {
                        boletaKredit.setEstadomnsag(2);
                        boletaKredit.setMnsagsunat(response.getBody().getMensaje());
                    }else {

                    }

                }
            }else {
                String cadena2 = response.getBody().getMensaje();
                int resultado = cadena2.indexOf("registrado");

                if(resultado != -1) {
                    boletaKredit.setEstadomnsag(2);
                    boletaKredit.setMnsagsunat(response.getBody().getMensaje());
                }else {

                }

            }


           // boletaKredit.setBoletaKreditIdentity(boletaKredit.getBoletaKreditIdentity());

            boletaService.save(boletaKredit);
        }


        //restTemplate.getForEntity("/api/endpoint", ResponseDTO.class).getBody();
        return ImmutableMap.of("objeto",response);
    }

    private Integer getNumeroSiguiente(String tipodocumento, String s) {
        RestTemplate restTemplate = new RestTemplate();
        String urlSiguiente = urlApiReciboNumero+"/"+tipodocumento+"/"+s;
        HttpEntity requestEntities = new HttpEntity(getHttpHeaders());
        ResponseEntity<Integer> response = restTemplate.exchange(urlSiguiente, HttpMethod.GET, requestEntities,Integer.class);
        System.out.println("SIGUIENTE NUMERO " + response.getBody());
        return response.getBody();
    }

    private BigDecimal convertDecimal(double entero){
        return new BigDecimal(entero, MathContext.DECIMAL64).setScale(2, RoundingMode.HALF_UP);
    }
    private HttpHeaders getHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "bearer "+ "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNDUiLCJpYXQiOjE1NjUyOTU5MzUsImV4cCI6MTY1MTY5NTkzNX0.pf77DWtxfsryQe3WpldLNNxQ4lBiD7mCJcaZMMdQ0BJJcid__Th1PbL9hXGHiltAqJT1HBTDEEXOEim5q7FNNw");

        return headers;
    }
    @GetMapping(value = "/pagosBoleta",produces = "application/json")
    @ResponseBody
    public Map<String, Object>  pagosboleta(@RequestParam("clienteid") String clienteid,
                                          @RequestParam("pagenumber") int pagenumber,
                                          @RequestParam("perpage") int perpage,
                                          HttpServletResponse responsehhttp) {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        List<BoletaKredit> boletaKredit = boletaService.findBoletaKreditByClienteid(clienteid,pagenumber,perpage);
        long cantidad = boletaService.countByClienteid(clienteid);
        return ImmutableMap.of("notices",boletaKredit,
                                "count",cantidad);
    }

    @GetMapping(value = "/pagosBoletaDni",produces = "application/json")
    @ResponseBody
    public Map<String, Object>  pagosboletaDni(@RequestParam("clienteid") String clienteid,
                                            @RequestParam("pagenumber") int pagenumber,
                                            @RequestParam("perpage") int perpage,
                                            HttpServletResponse responsehhttp) {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");

        List<BoletaKredit> boletaKredit = boletaService.findBoletaKreditByClienteiddni(clienteid,pagenumber,perpage);
        long cantidad = boletaService.countByClientedni(clienteid);
        return ImmutableMap.of("notices",boletaKredit,
                "count",cantidad);
    }
    @GetMapping(value = "/pagosBoletaContratos",produces = "application/json")
    @ResponseBody
    public Map<String, Object>  pagosboletaContratos(
                                                @RequestParam("productoid") String productoid,
                                                @RequestParam("contratoid") String contratoid,
                                                @RequestParam("monedaid") String monedaid,
                                                @RequestParam("pagenumber") int pagenumber,
                                                @RequestParam("perpage") int perpage,
                                               HttpServletResponse responsehhttp) {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");

        List<BoletaKredit> boletaKredit = boletaService.findBoletaKreditByProductoidAndContratoidAndMonedaid(productoid,contratoid,monedaid,pagenumber,perpage);
        long cantidad = boletaService.countByProductoidAndContratoidAndMonedaid(productoid,contratoid,monedaid);
        return ImmutableMap.of("notices",boletaKredit,
                "count",cantidad);
    }

    @GetMapping(value = "/revisaBoleta",produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> revisaboleta(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) throws ParseException, JsonProcessingException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        List<Map<String, Object>> maps =new ArrayList<Map<String, Object>>();
        List<BoletaKredit> boletaKredits = boletaService.revisaBoletaKredit(clienteid);
        //sendBoleta(37119);
        if(boletaKredits.size()==0){
            maps.add(ImmutableMap.of("objeto",new Object()));
        }
        try {
            for(BoletaKredit boletaKredit: boletaKredits){
                System.out.println("CAMBIOS COMPROBANTE");
                System.out.println(boletaKredit.getComprobanteid());
                //if (boletaKredit.getComprobanteid()==38691){
                maps.add(sendBoleta(boletaKredit));
                //}
                // maps.add(sendBoleta(boletaKredit));
            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }finally {
            sendday();
        }

        return maps;
    }


    private void sendday() throws ParseException, JsonProcessingException {
        List<BoletaKredit> boletaKredits = boletaService.revisaBoletaKreditDay();

        for(BoletaKredit boletaKredit: boletaKredits){
            System.out.println("CAMBIOS COMPROBANTE");
            System.out.println(boletaKredit.getComprobanteid());
            //if (boletaKredit.getComprobanteid()==38691){
            sendBoleta(boletaKredit);
            //}
            // maps.add(sendBoleta(boletaKredit));
        }

    }

    @GetMapping(value = "/revisaBoletaDni",produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> revisaboletadni(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) throws ParseException, JsonProcessingException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        List<Map<String, Object>> maps =new ArrayList<Map<String, Object>>();
        List<BoletaKredit> boletaKredits = boletaService.revisaBoletaKreditdni(clienteid);
        //sendBoleta(37119);
        System.out.println(" DNI "+clienteid);
        if(boletaKredits.size()==0){
            maps.add(ImmutableMap.of("objeto",new Object()));
        }
        for(BoletaKredit boletaKredit: boletaKredits){
            maps.add(sendBoleta(boletaKredit));
        }
        return maps;
    }
    @GetMapping(value = "/revisaBoletasContrato",produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> revisaBoletasContrato(@RequestParam("productId") String productoid,@RequestParam("contratoId") String contratoId,@RequestParam("monedaId") String monedaId, HttpServletResponse responsehhttp) throws ParseException, JsonProcessingException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        List<Map<String, Object>> maps =new ArrayList<Map<String, Object>>();
        List<BoletaKredit> boletaKredits = boletaService.revisaBoletaKreditProductContratoMoneda(productoid, contratoId,monedaId);
        //sendBoleta(37119);

        if(boletaKredits.size()==0){
            maps.add(ImmutableMap.of("objeto",new Object()));
        }
        for(BoletaKredit boletaKredit: boletaKredits){
            maps.add(sendBoleta(boletaKredit));
        }
        return maps;
    }

    @GetMapping(value = "/cliente",produces = "application/json")
    @ResponseBody
    public ClienteKredit loadclient(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");


      //  List<MovimientoCasaCambio> movimientoCasaCambios = movimientoCasaCambioService.findAll();

      //  System.out.print(movimientoCasaCambios.toString());

        return clienteService.findByClienteidp(clienteid);
    }

}
