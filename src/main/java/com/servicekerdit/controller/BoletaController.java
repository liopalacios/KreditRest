package com.servicekerdit.controller;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.model.BoletaSunat;
import com.servicekerdit.model.DetalleSunat;
import com.servicekerdit.model.ResponseMultinet;
import com.servicekerdit.service.BoletaDetalleService;
import com.servicekerdit.service.BoletaService;
import com.servicekerdit.service.ClienteService;
import com.google.common.collect.ImmutableMap;
import com.servicekerdit.service.impl.BoletaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BoletaController {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String NIU = "NIU";
    private static final int CANTIDAD = 1;
    private static final String TIGV = "10";

    @Value("${storage.auth}")
    private String authtorization;

    @Value("${storage.server}")
    private String urlApiStorage;

    @Value("${storage.port}")
    private String portApiStorage;

    @Value("${apiserviceweb.server}")
    private String urlApiServiceWeb;


    @Autowired
    BoletaService boletaService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    BoletaDetalleService boletaDetalleService;

    @GetMapping(value = "/boleta",produces = "application/json")
    @ResponseBody
    public ResponseMultinet index(@RequestParam("nucomprobante") int nucomprobante, HttpServletResponse responsehhttp){

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity;
        LinkedMultiValueMap<String, Object> map;

        BoletaKredit boletaKredit = boletaService.findBoletaKreditBySereid(nucomprobante);
        BoletaDetalleKredit boletaDetalleKredit = boletaDetalleService.findBySerieidAndComprobanteid(boletaKredit.getSerieid(), boletaKredit.getComprobanteid());
        ArrayList<DetalleSunat> detalleSunats= new ArrayList<>();
        System.out.print(nucomprobante+"\n");
        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setCliente_tipo_documento(Integer.toString(boletaKredit.getClienteid().getDocumentotipo()));
        boletaSunat.setCliente_numero_documento(boletaKredit.getClienteid().getDocumento());
        boletaSunat.setCliente_denominacion(boletaKredit.getClienteid().getApellidop()+" "+boletaKredit.getClienteid().getApellidom()+" "+boletaKredit.getClienteid().getNombre());
        boletaSunat.setCliente_direccion(boletaKredit.getClienteid().getDireccion());
        boletaSunat.setTipo_operacion("01");
        boletaSunat.setDocumento_serie("B"+boletaKredit.getSerieid());
        boletaSunat.setDocumento_numero(boletaKredit.getComprobanteid());
        //boletaSunat.setDocumento_fecha_emision(DATE_FORMAT.format(boletaKredit.getFechaimp()));
        boletaSunat.setDocumento_fecha_emision("2018-10-22");
        boletaSunat.setDocumento_hora_emision(boletaKredit.getHoregistro());
        boletaSunat.setDocumento_tipo_moneda("PEN");
        boletaSunat.setTotal_venta_gravadas(boletaKredit.getSubtotal());
        boletaSunat.setTotal_igv(boletaKredit.getToigv());
        //"total_imp_ope_grat": null,
        boletaSunat.setImporte_total(boletaKredit.getTopagado());
        boletaSunat.setDocumento_tipo_comprobante("03");
        //"detraccion": "N",
            DetalleSunat detalleSunat = new DetalleSunat();
            //detalleSunat.setTextAreaDescription(false);
            detalleSunat.setUnidad_medida(NIU);
            detalleSunat.setCantidad(CANTIDAD);
            detalleSunat.setTipo_afectacion_igv(TIGV);
            detalleSunat.setCodigo_producto_interno(boletaDetalleKredit.getProducto());
            detalleSunat.setDescripcion("Cuota de prestamo");
            detalleSunat.setIgv(boletaDetalleKredit.getVaigv());
            detalleSunat.setValor_unitario(boletaDetalleKredit.getVatotalpago());
            detalleSunat.setPrecio_venta_unitario(boletaDetalleKredit.getVatotalpago());
            detalleSunat.setPrecio_venta_unitario(boletaDetalleKredit.getVatotalpago());
            // "valor_referencial_unitario": null,
            detalleSunat.setValor_venta(boletaDetalleKredit.getVatotalpago()-boletaDetalleKredit.getVaigv());
            detalleSunat.setValor_unitario(boletaDetalleKredit.getVatotalpago()-boletaDetalleKredit.getVaigv());
        detalleSunats.add(detalleSunat);
        boletaSunat.setItems(detalleSunats);

        String url = "https://beta.certifakt.com.pe/back/api/comprobantes-pago";

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<BoletaSunat> requestEntities = new HttpEntity<BoletaSunat>(boletaSunat, getHttpHeaders());

        ResponseEntity<ResponseMultinet> response = restTemplate.exchange(url, HttpMethod.POST, requestEntities,ResponseMultinet.class);
        System.out.print(response.getBody());

        //restTemplate.getForEntity("/api/endpoint", ResponseDTO.class).getBody();

        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");

        return response.getBody();
    }
    public Map<String, Object> sendBoleta( int nucomprobante){

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity;
        LinkedMultiValueMap<String, Object> map;

        BoletaKredit boletaKredit = boletaService.findBoletaKreditBySereid(nucomprobante);
        BoletaDetalleKredit boletaDetalleKredit = boletaDetalleService.findBySerieidAndComprobanteid(boletaKredit.getSerieid(), boletaKredit.getComprobanteid());
        ArrayList<DetalleSunat> detalleSunats= new ArrayList<>();
        System.out.print(nucomprobante+"\n");
        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setCliente_tipo_documento(Integer.toString(boletaKredit.getClienteid().getDocumentotipo()));
        boletaSunat.setCliente_numero_documento(boletaKredit.getClienteid().getDocumento());
        boletaSunat.setCliente_denominacion(boletaKredit.getClienteid().getApellidop()+" "+boletaKredit.getClienteid().getApellidom()+" "+boletaKredit.getClienteid().getNombre());
        boletaSunat.setCliente_direccion(boletaKredit.getClienteid().getDireccion());
        boletaSunat.setTipo_operacion("01");
        boletaSunat.setDocumento_serie("B"+boletaKredit.getSerieid());
        boletaSunat.setDocumento_numero(boletaKredit.getComprobanteid());
        //boletaSunat.setDocumento_fecha_emision(DATE_FORMAT.format(boletaKredit.getFechaimp()));
        boletaSunat.setDocumento_fecha_emision("2018-10-22");
        boletaSunat.setDocumento_hora_emision(boletaKredit.getHoregistro());
        boletaSunat.setDocumento_tipo_moneda("PEN");
        boletaSunat.setTotal_venta_gravadas(boletaKredit.getSubtotal());
        boletaSunat.setTotal_igv(boletaKredit.getToigv());
        //"total_imp_ope_grat": null,
        boletaSunat.setImporte_total(boletaKredit.getTopagado());
        boletaSunat.setDocumento_tipo_comprobante("03");
        //"detraccion": "N",
        DetalleSunat detalleSunat = new DetalleSunat();
        //detalleSunat.setTextAreaDescription(false);
        detalleSunat.setUnidad_medida(NIU);
        detalleSunat.setCantidad(CANTIDAD);
        detalleSunat.setTipo_afectacion_igv(TIGV);
        detalleSunat.setCodigo_producto_interno(boletaDetalleKredit.getProducto());
        detalleSunat.setDescripcion("Cuota de prestamo");
        detalleSunat.setIgv(boletaDetalleKredit.getVaigv());
        detalleSunat.setValor_unitario(boletaDetalleKredit.getVatotalpago());
        detalleSunat.setPrecio_venta_unitario(boletaDetalleKredit.getVatotalpago());
        detalleSunat.setPrecio_venta_unitario(boletaDetalleKredit.getVatotalpago());
        // "valor_referencial_unitario": null,
        detalleSunat.setValor_venta(boletaDetalleKredit.getVatotalpago()-boletaDetalleKredit.getVaigv());
        detalleSunat.setValor_unitario(boletaDetalleKredit.getVatotalpago()-boletaDetalleKredit.getVaigv());
        detalleSunats.add(detalleSunat);
        boletaSunat.setItems(detalleSunats);
        String url = "https://beta.certifakt.com.pe/back/api/comprobantes-pago";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<BoletaSunat> requestEntities = new HttpEntity<BoletaSunat>(boletaSunat, getHttpHeaders());
        ResponseEntity<ResponseMultinet> response = restTemplate.exchange(url, HttpMethod.POST, requestEntities,ResponseMultinet.class);
        System.out.print(response.getBody());
        String numstr = Integer.toString(nucomprobante);
        //restTemplate.getForEntity("/api/endpoint", ResponseDTO.class).getBody();
        return ImmutableMap.of("objeto",response);
    }
    private HttpHeaders getHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer "+ authtorization);

        return headers;
    }
    @GetMapping(value = "/pagosBoleta",produces = "application/json")
    @ResponseBody
    public List<BoletaKredit> pagosboleta(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        return boletaService.findBoletaKreditByClienteid(clienteid);
    }

    @GetMapping(value = "/revisaBoleta",produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> revisaboleta(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) throws ParseException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        List<Map<String, Object>> maps =null;
        List<BoletaKredit> boletaKredits = boletaService.revisaBoletaKredit(clienteid);
        for(BoletaKredit boletaKredit: boletaKredits){
            maps.add(sendBoleta(boletaKredit.getComprobanteid()));
        }
        return maps;
    }
    @GetMapping(value = "/cliente",produces = "application/json")
    @ResponseBody
    public ClienteKredit loadclient(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        return clienteService.findByClienteidp(clienteid);
    }

}
