package com.servicekerdit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.math.BigDecimal;
import java.math.MathContext;
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

    @Autowired
    BoletaService boletaService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    BoletaDetalleService boletaDetalleService;


    public Map<String, Object> sendBoleta( int nucomprobante) throws JsonProcessingException {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity;
        LinkedMultiValueMap<String, Object> map;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        BoletaKredit boletaKredit = boletaService.findBoletaKreditBySereid(nucomprobante);
        BoletaDetalleKredit boletaDetalleKredit = boletaDetalleService.findBySerieidAndComprobanteid(boletaKredit.getBoletaKreditIdentity().getSerieid(), boletaKredit.getBoletaKreditIdentity().getComprobanteid());
        ArrayList<DetalleSunat> detalleSunats= new ArrayList<>();
        System.out.print(nucomprobante+"\n");
        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setDocumento_tipo_comprobante("03");
        boletaSunat.setDocumento_serie("B"+boletaKredit.getBoletaKreditIdentity().getSerieid());
        boletaSunat.setDocumento_numero(boletaKredit.getBoletaKreditIdentity().getComprobanteid());
        boletaSunat.setDocumento_fecha_emision(df.format(boletaKredit.getFechaimp()));
        boletaSunat.setDocumento_hora_emision(boletaKredit.getHoregistro());
        boletaSunat.setDocumento_fecha_vencimiento(null);
        boletaSunat.setDocumento_tipo_moneda("PEN");

        boletaSunat.setCliente_tipo_documento(Integer.toString(boletaKredit.getClienteid().getDocumentotipo()));
        boletaSunat.setCliente_numero_documento(boletaKredit.getClienteid().getDocumento());
        boletaSunat.setCliente_denominacion(boletaKredit.getClienteid().getApellidop()+" "+boletaKredit.getClienteid().getApellidom()+" "+boletaKredit.getClienteid().getNombre());
        boletaSunat.setCliente_direccion(boletaKredit.getClienteid().getDireccion());
        boletaSunat.setCliente_email(null);

        //boletaSunat.setTotal_venta_exportacion(null);
        boletaSunat.setTotal_venta_gravadas(convertDecimal(boletaKredit.getSubtotal()));
        //boletaSunat.setTotal_venta_inafectas(null);
        //boletaSunat.setTotal_venta_exoneradas(null);
        //boletaSunat.setTotal_venta_gratuitas(null);
        //boletaSunat.setTotal_imp_ope_grat(null);
        //boletaSunat.setTotal_descuento(null);
        boletaSunat.setTotal_igv(convertDecimal(boletaKredit.getToigv()));

        //boletaSunat.setTotal_isc(null);
        boletaSunat.setTotal_otros_tributos(null);
        boletaSunat.setDescuentos_globales((null));
        boletaSunat.setTotal_otros_cargos(null);

        boletaSunat.setDescuentos_globales(null);
        boletaSunat.setTotal_otros_cargos(null);
        boletaSunat.setImporte_total(convertDecimal(boletaKredit.getValortotal()));
        boletaSunat.setTipo_operacion("01");
        boletaSunat.setTipo_nota_credito(null);
        boletaSunat.setTipo_nota_debito(null);

        boletaSunat.setDocumento_afectado_serie(null);
        boletaSunat.setDocumento_afectado_numero(null);
        boletaSunat.setDocumento_afectado_tipo_comprobante(null);
        boletaSunat.getDocumento_afectado_motivo_nota();
        boletaSunat.setOrden_compra(null);
        boletaSunat.setDetraccion(null);
        boletaSunat.setPorcentaje_detraccion(null);

        //boletaSunat.setDocumento_fecha_emision(DATE_FORMAT.format(boletaKredit.getFechaimp()));

        //"total_imp_ope_grat": null,


        //"detraccion": "N",
        DetalleSunat detalleSunat = new DetalleSunat();
        //detalleSunat.setTextAreaDescription(false);
        detalleSunat.setUnidad_medida(NIU);
        detalleSunat.setCantidad(CANTIDAD);
        detalleSunat.setTipo_afectacion_igv(TIGV);
        detalleSunat.setCodigo_producto_interno(boletaDetalleKredit.getProducto().getProductoid());
        detalleSunat.setDescripcion(boletaDetalleKredit.getProducto().getNomcorto());
        detalleSunat.setIgv(convertDecimal(boletaDetalleKredit.getVaigv()));
        detalleSunat.setValor_unitario(convertDecimal(boletaDetalleKredit.getVatotalpago()));
        detalleSunat.setMonto_base_igv(convertDecimal(boletaDetalleKredit.getVatotalpago()));
        detalleSunat.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVatotalpago()));
        detalleSunat.setPrecio_venta_unitario(convertDecimal(boletaDetalleKredit.getVatotalpago()));
        detalleSunat.setPorcentaje_igv(PIGV);
        // "valor_referencial_unitario": null,
        detalleSunat.setValor_venta(convertDecimal(boletaDetalleKredit.getVatotalpago()-boletaDetalleKredit.getVaigv()));
        detalleSunat.setValor_unitario(convertDecimal(boletaDetalleKredit.getVatotalpago()-boletaDetalleKredit.getVaigv()));
        detalleSunats.add(detalleSunat);

        boletaSunat.setItems(detalleSunats);
        System.out.print(" BOLETA SUNAT \n");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(boletaSunat);
        System.out.print(json);
        System.out.print("\n");
        String url = "https://beta.certifakt.com.pe/back/api/comprobantes-pago";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<BoletaSunat> requestEntities = new HttpEntity<BoletaSunat>(boletaSunat, getHttpHeaders());
        ResponseEntity<ResponseMultinet> response = restTemplate.exchange(urlApiEnvComprobante, HttpMethod.POST, requestEntities,ResponseMultinet.class);
        System.out.print(response.getBody());

        if(response.getStatusCodeValue()==200){
            boletaKredit.setEstadoenv(1);
            if(response.getBody().getEstado()){
                boletaKredit.setEstadomnsag(1);
                boletaKredit.setMnsagsunat(response.getBody().getMensaje());
                boletaKredit.setLinkticket(response.getBody().getUrlPdfTicket());
                boletaKredit.setLinkpdf(response.getBody().getUrlPdfA4());
                boletaKredit.setLinkxml(response.getBody().getUrlXml());
            }else {
                boletaKredit.setEstadomnsag(2);
                boletaKredit.setMnsagsunat(response.getBody().getMensaje());
            }

            boletaKredit.setBoletaKreditIdentity(boletaKredit.getBoletaKreditIdentity());

            boletaService.save(boletaKredit);
        }
        String numstr = Integer.toString(nucomprobante);
        //restTemplate.getForEntity("/api/endpoint", ResponseDTO.class).getBody();
        return ImmutableMap.of("objeto",response);
    }
    private BigDecimal convertDecimal(double entero){
        return new BigDecimal(entero, MathContext.DECIMAL64);
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

    @GetMapping(value = "/revisaBoleta",produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> revisaboleta(@RequestParam("clienteid") String clienteid, HttpServletResponse responsehhttp) throws ParseException, JsonProcessingException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        List<Map<String, Object>> maps =null;
        List<BoletaKredit> boletaKredits = boletaService.revisaBoletaKredit(clienteid);
        for(BoletaKredit boletaKredit: boletaKredits){
            maps.add(sendBoleta(boletaKredit.getBoletaKreditIdentity().getComprobanteid()));
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
