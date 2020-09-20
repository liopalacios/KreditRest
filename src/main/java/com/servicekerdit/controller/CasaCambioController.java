package com.servicekerdit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteCasaCambio;
import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.model.BoletaSunat;
import com.servicekerdit.model.CampoAdicional;
import com.servicekerdit.model.DetalleSunat;
import com.servicekerdit.model.ResponseMultinet;
import com.servicekerdit.service.ClienteCasaCambioService;
import com.servicekerdit.service.MovimientoCasaCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api")
public class CasaCambioController {

    private static final int TDNI = 1;
    private static final int TRUC = 6;
    private static final String NIU = "ZZ";
    private static final String NRUC = "1010101010";
    private static final String NOBRERAZON = "CERTIKREDIT";
    private static final String NOMBREDIRECCION = "LIMA";
    private static final String DIRECCION = "Dean Valdivia 148";
    private static final Integer CANTIDAD = 1;
    private static final String TIGV = "30";
    private static final String CODPRODINT = "84121603";
    private String CASACAMBIO = "CAMBIO DE MONEDA";
    private static final String CASACAMBIADO = "CAMBIO DE MONEDA";
    private static final int PIGV = 18;
    private static final String TIPOCCAMBIO = "0101";
    private static final int NVALOR = 0;
    private static final BigDecimal TOTINAF = new BigDecimal(0);
    private static final String ZZ = "ZZ";
    HashMap<Integer, String> fieldsTypeOperation;
    HashMap<Integer, String> fieldsTypeMoney;
    @Value("${storage.auth}")
    private String authtorization;
    @Value("${api.envio.comprobante}")
    private String urlApiEnvComprobante;
    @Autowired
    MovimientoCasaCambioService movimientoCasaCambioService;

    @Autowired
    ClienteCasaCambioService clienteCasaCambioService;

    public CasaCambioController() {
        fieldsTypeOperation = new HashMap<Integer, String>();
        fieldsTypeOperation.put(10,"COMPRA DE MONEDA");
        fieldsTypeOperation.put(11,"VENTA DE MONEDA");
        fieldsTypeOperation.put(15,"TRANS-E-CUENTA VENTA");
        fieldsTypeOperation.put(14,"TRANS-E-CUENTA COMPRA");
        fieldsTypeMoney = new HashMap<Integer, String>();
        fieldsTypeMoney.put(14,"SOLES PERUANOS");
        fieldsTypeMoney.put(15,"DOLARES AMERICANOS");
    }

    @GetMapping(value = "/revisaCambios",produces = "application/json")
    @ResponseBody
    public Map<String, Object> revisaCambios(@RequestParam("userid") String userid, HttpServletResponse responsehhttp) throws Exception {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
       // Map<String, Object> maps =new Map<String, Object>();

        List<MovimientoCasaCambio> movimientoCasaCambios = movimientoCasaCambioService.findNoSend();
        System.out.println(movimientoCasaCambios.size());
        for(MovimientoCasaCambio movimientoCasaCambio: movimientoCasaCambios){

            System.out.println(movimientoCasaCambio.getNroTicket());
            //9183
            //if(movimientoCasaCambio.getNroTicket().equals("0000 - 9424")){

                sendCasacambio(movimientoCasaCambio);

            //}

        }
        return ImmutableMap.of("listcasacambio",movimientoCasaCambios);
    }

    private void sendCasacambio(MovimientoCasaCambio movimientoCasaCambio) throws Exception {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity;
        LinkedMultiValueMap<String, Object> map;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        BoletaKredit boletaKredit1 = new BoletaKredit();
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        ArrayList<DetalleSunat> detalleSunats= new ArrayList<>();
        ArrayList<CampoAdicional> fieldsSunats= new ArrayList<>();
        String mil = movimientoCasaCambio.getNroTicket();
        String[] milarray = mil.split("-");
        int milInt = Integer.valueOf(milarray[0].trim());
        int cienInt = Integer.valueOf(milarray[1].trim());

        String monedaOrigen="";
        String monedaDestino="";
        String usuariocasacambio="";
        String oficinacasacambio="";
        String seriecasacambio="";

        if(movimientoCasaCambio.getIdusuario()==3163){
            usuariocasacambio="VITALIA GALVEZ";
            oficinacasacambio="OFICINA TRUJILLO";
            seriecasacambio="B005";

        }else{
            oficinacasacambio="OFICINA LIMA";
            usuariocasacambio="PAMELA DEL CARPIO";
            seriecasacambio="B003";
            cienInt = cienInt-9075;
        }

        if(movimientoCasaCambio.getIdMonedaOrigen()==15){
            monedaOrigen = "USD";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==16){
            monedaOrigen = "EUR";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==31){
            monedaOrigen = "CAD";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==32){
            monedaOrigen = "AUD";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==17){
            monedaOrigen = "CLP";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==28){
            monedaOrigen = "MXN";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==29){
            monedaOrigen = "ARS";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==19){
            monedaOrigen = "COP";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==23){
            monedaOrigen = "BRL";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==21){
            monedaOrigen = "GBP";
        }else if(movimientoCasaCambio.getIdMonedaOrigen()==22){
            monedaOrigen = "Bs";
        }else{
            monedaOrigen = "PEN";
        }

        if(movimientoCasaCambio.getIdMonedaDestino()==15){
            monedaDestino = "USD";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==16){
            monedaDestino = "EUR";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==17){
            monedaDestino = "CLP";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==31){
            monedaDestino = "CAD";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==32){
            monedaDestino = "AUD";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==28){
            monedaDestino = "MXN";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==29){
            monedaDestino = "ARS";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==19){
            monedaDestino = "COP";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==23){
            monedaOrigen = "BRL";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==21){
            monedaOrigen = "GBP";
        }else if(movimientoCasaCambio.getIdMonedaDestino()==22){
            monedaOrigen = "Bs";
        }else{
            monedaDestino = "PEN";
        }

        if(movimientoCasaCambio.getTipoNroDocCli()==6){
            movimientoCasaCambio.setTipoNroDocCli(4);
        }else if(movimientoCasaCambio.getTipoNroDocCli()==3){
            movimientoCasaCambio.setTipoNroDocCli(6);
        }


        if(movimientoCasaCambio.getIdTipoOperacion()==10){
            CASACAMBIO = "CAMBIO DE MONEDA EXTRANJERA";
        }else{
            CASACAMBIO = "CAMBIO DE MONEDA NACIONAL";
        }

        System.out.println(monedaOrigen);
        System.out.println();

        List<ClienteCasaCambio> clienteCasaCambio = clienteCasaCambioService.findByNumdocu(movimientoCasaCambio.getNumDocu());

        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setDocumento_tipo_comprobante("03");
        boletaSunat.setDocumento_serie(seriecasacambio);
        boletaSunat.setDocumento_numero(cienInt);
        boletaSunat.setDocumento_fecha_emision(df.format(movimientoCasaCambio.getFechaReg()));
        boletaSunat.setDocumento_fecha_vencimiento(null);
        boletaSunat.setDocumento_hora_emision(new java.sql.Time(movimientoCasaCambio.getFechaDefault().getTime()));
        boletaSunat.setDocumento_tipo_moneda("PEN");
        boletaSunat.setCliente_tipo_documento(Integer.toString(movimientoCasaCambio.getTipoNroDocCli()));
        boletaSunat.setCliente_numero_documento(movimientoCasaCambio.getNumDocu().replace("-",""));
        boletaSunat.setCliente_denominacion(clienteCasaCambio.get(0).getRazonSocial());
        boletaSunat.setCliente_direccion(null);
        boletaSunat.setCliente_email(null);
        boletaSunat.setTotal_venta_gravadas(null);

        boletaSunat.setTotal_igv(null);
        boletaSunat.setDescuentos_globales(null);
        boletaSunat.setImporte_total(TOTINAF);
        boletaSunat.setTipo_operacion(TIPOCCAMBIO);

            DetalleSunat detalleSunat = new DetalleSunat();
            detalleSunat.setUnidad_medida(ZZ);
            detalleSunat.setCantidad(new BigDecimal(CANTIDAD));
            detalleSunat.setDescripcion(CASACAMBIO);
            detalleSunat.setValor_unitario(new BigDecimal(NVALOR));
            detalleSunat.setValor_venta(new BigDecimal(NVALOR));
            detalleSunat.setDescuento(null);
            detalleSunat.setPrecio_venta_unitario(new BigDecimal(NVALOR));
            detalleSunat.setValor_referencial_unitario(null);
            detalleSunat.setMonto_base_inafecto(null);
            detalleSunat.setTipo_afectacion_igv(TIGV);
            detalleSunats.add(detalleSunat);

        CampoAdicional fieldsSunat = null;

        HashMap<String, String> fieldsAddPayment = new HashMap<String, String>();
        fieldsAddPayment.put("TIPO DE OPERACION",fieldsTypeOperation.get(movimientoCasaCambio.getIdTipoOperacion()));
        fieldsAddPayment.put("MONTO",monedaOrigen+" "+(movimientoCasaCambio.getCantidadCambiar().setScale(2, RoundingMode.HALF_UP)));
        fieldsAddPayment.put("TIPO CAMBIO",String.valueOf(movimientoCasaCambio.getTipoCambio()));
        fieldsAddPayment.put("TOTAL",monedaDestino+" "+ (movimientoCasaCambio.getClienteRecibe().setScale(2, RoundingMode.HALF_UP)));
        fieldsAddPayment.put("CASA",oficinacasacambio);
        fieldsAddPayment.put("CAJERO",usuariocasacambio);

        for (String i : fieldsAddPayment.keySet()) {
            fieldsSunat = new CampoAdicional();
            fieldsSunat.setNombre_campo(i);
            fieldsSunat.setValor_campo(fieldsAddPayment.get(i));
            fieldsSunats.add(fieldsSunat);
        }


        boletaSunat.setItems(detalleSunats);
        boletaSunat.setCampos_adicionales(fieldsSunats);
        ObjectMapper mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();
        System.out.println(boletaSunat);
        try {
            HttpEntity<BoletaSunat> requestEntities = new HttpEntity<BoletaSunat>(boletaSunat, getHttpHeaders());
            ResponseEntity<ResponseMultinet> response = restTemplate.exchange(urlApiEnvComprobante, HttpMethod.POST, requestEntities,ResponseMultinet.class);
            System.out.println(response);
            System.out.print(response.getBody());
            if(response.getStatusCodeValue()==200){
                movimientoCasaCambio.setEstadoenv(1);
                if(response.getBody().getEstado()){
                    movimientoCasaCambio.setEstadomnsag(1);
                    movimientoCasaCambio.setMnsagsunat(response.getBody().getMensaje());
                    movimientoCasaCambio.setLinkticket(response.getBody().getUrlPdfTicket());
                    movimientoCasaCambio.setLinkpdf(response.getBody().getUrlPdfA4());
                    movimientoCasaCambio.setLinkxml(response.getBody().getUrlXml());
                    movimientoCasaCambio.setIdvoucher(boletaSunat.getDocumento_numero());
                    movimientoCasaCambioService.save(movimientoCasaCambio);
                }else if(!response.getBody().getEstado()){
                    if(response.getBody().getMensaje().contains("ya ha sido registrado")){
                        System.out.println("SI ES VALIDO YA EXISTE ");
                    }else {
                        System.out.println("NUEVOOOOOOOO");
                        movimientoCasaCambio.setEstadomnsag(2);
                        movimientoCasaCambio.setMnsagsunat(response.getBody().getMensaje());
                        movimientoCasaCambioService.save(movimientoCasaCambio);
                    }

                }
                //movimientoCasaCambioService.save(movimientoCasaCambio);
            }
        }catch (HttpClientErrorException e){
            System.out.println(e.getMessage());
        }




    }
    private HttpHeaders getHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer "+ "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNDUiLCJpYXQiOjE1NjUyOTU5MzUsImV4cCI6MTY1MTY5NTkzNX0.pf77DWtxfsryQe3WpldLNNxQ4lBiD7mCJcaZMMdQ0BJJcid__Th1PbL9hXGHiltAqJT1HBTDEEXOEim5q7FNNw");

        return headers;
    }
    @GetMapping(value = "/listaCasaCambios",produces = "application/json")
    @ResponseBody
    public Map<String, Object> listHouseChange(@RequestParam("userid") int userid,
                                               @RequestParam("page") int page,
                                               @RequestParam("limite") int limite,
                                               HttpServletResponse responsehhttp) throws Exception {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        // Map<String, Object> maps =new Map<String, Object>();

        String numdni = "45833540";

        List<MovimientoCasaCambio> movimientoCasaCambios = movimientoCasaCambioService.findFecReg(page,limite,userid);

        int cantidad = movimientoCasaCambioService.countCasacambioidByDate(userid);

        return ImmutableMap.of("listcasacambio",movimientoCasaCambios, "count",cantidad);

    }
}
