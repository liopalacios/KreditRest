package com.servicekerdit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.model.BoletaSunat;
import com.servicekerdit.model.DetalleSunat;
import com.servicekerdit.model.ResponseMultinet;
import com.servicekerdit.service.MovimientoCasaCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CasaCambioController {

    private static final int TDNI = 1;
    private static final int TRUC = 6;
    private static final String NRUC = "1010101010";
    private static final String NOBRERAZON = "CERTIKREDIT";
    private static final String DIRECCION = "Dean Valdivia 148";

    @Autowired
    MovimientoCasaCambioService movimientoCasaCambioService;

    @GetMapping(value = "/revisaCambios",produces = "application/json")
    @ResponseBody
    public Map<String, Object> revisaCambios(@RequestParam("userid") String userid, HttpServletResponse responsehhttp) throws ParseException, JsonProcessingException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
       // Map<String, Object> maps =new Map<String, Object>();

        List<MovimientoCasaCambio> movimientoCasaCambios = movimientoCasaCambioService.findNoSend();

        for(MovimientoCasaCambio movimientoCasaCambio: movimientoCasaCambios){
            sendCasacambio(movimientoCasaCambio);
        }

        return ImmutableMap.of("listcasacambio",movimientoCasaCambios);
    }

    private void sendCasacambio(MovimientoCasaCambio movimientoCasaCambio) {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity;
        LinkedMultiValueMap<String, Object> map;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //BoletaKredit boletaKredit = boletaService.findBoletaKreditBySereid(nucomprobante);
        BoletaKredit boletaKredit1 = new BoletaKredit();

        Date date = new Date();

        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

        //BoletaDetalleKredit boletaDetalleKredit = boletaDetalleService.findBySerieidAndComprobanteid(boletaKredit.getSerieid(), boletaKredit.getComprobanteid());
        ArrayList<DetalleSunat> detalleSunats= new ArrayList<>();
        //System.out.print(boletaKredit.toString()+"\n");

        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setDocumento_tipo_comprobante("03");
        boletaSunat.setDocumento_serie("B"+movimientoCasaCambio.getNuticket());
        boletaSunat.setDocumento_numero(movimientoCasaCambio.getNuoperacion());
        boletaSunat.setDocumento_fecha_emision(df.format(movimientoCasaCambio.getFechareg()));
        boletaSunat.setDocumento_hora_emision(new java.sql.Time(date.getTime()));
        boletaSunat.setDocumento_fecha_vencimiento(null);
        boletaSunat.setDocumento_tipo_moneda(movimientoCasaCambio.getSigno());

        boletaSunat.setCliente_tipo_documento(Integer.toString(TRUC));
        boletaSunat.setCliente_numero_documento(NRUC);
        boletaSunat.setCliente_denominacion(NOBRERAZON);
        boletaSunat.setCliente_direccion(DIRECCION);
        boletaSunat.setCliente_email(null);

        //boletaSunat.setTotal_venta_exportacion(null);
       // boletaSunat.setTotal_venta_gravadas(convertDecimal(boletaKredit.getSubtotal()));
        //boletaSunat.setTotal_venta_inafectas(null);
        //boletaSunat.setTotal_venta_exoneradas(null);
        //boletaSunat.setTotal_venta_gratuitas(null);
        //boletaSunat.setTotal_imp_ope_grat(null);
        //boletaSunat.setTotal_descuento(null);
       // boletaSunat.setTotal_igv(convertDecimal(boletaKredit.getToigv()));

        //boletaSunat.setTotal_isc(null);
        boletaSunat.setTotal_otros_tributos(null);
        boletaSunat.setDescuentos_globales((null));
        boletaSunat.setTotal_otros_cargos(null);

        boletaSunat.setDescuentos_globales(null);
        boletaSunat.setTotal_otros_cargos(null);
       // boletaSunat.setImporte_total(convertDecimal(boletaKredit.getValortotal()));
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
      /*  detalleSunat.setUnidad_medida(NIU);
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
        detalleSunats.add(detalleSunat);*/

        boletaSunat.setItems(detalleSunats);
        System.out.print(" BOLETA SUNAT \n");
        ObjectMapper mapper = new ObjectMapper();
    //    String json = mapper.writeValueAsString(boletaSunat);
  //      System.out.print(json);
        System.out.print("\n");
        String url = "https://beta.certifakt.com.pe/back/api/comprobantes-pago";
        RestTemplate restTemplate = new RestTemplate();
 /*       HttpEntity<BoletaSunat> requestEntities = new HttpEntity<BoletaSunat>(boletaSunat, getHttpHeaders());
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

            // boletaKredit.setBoletaKreditIdentity(boletaKredit.getBoletaKreditIdentity());

            boletaService.save(boletaKredit);
        }
*/

        //restTemplate.getForEntity("/api/endpoint", ResponseDTO.class).getBody();
       // return ImmutableMap.of("objeto",response);
       // return ImmutableMap.of("objeto","");;
    }

    @GetMapping(value = "/listaCasaCambios",produces = "application/json")
    @ResponseBody
    public Map<String, Object> listHouseChange(@RequestParam("userid") String userid,
                                               @RequestParam("page") int page,
                                               @RequestParam("limite") int limite,
                                               HttpServletResponse responsehhttp) throws ParseException, JsonProcessingException {
        responsehhttp.setContentType("application/json");
        responsehhttp.setCharacterEncoding("UTF-8");
        responsehhttp.setHeader("Access-Control-Allow-Origin", "*");
        // Map<String, Object> maps =new Map<String, Object>();

        String numdni = "45833540";

        List<MovimientoCasaCambio> movimientoCasaCambios = movimientoCasaCambioService.findFecReg(page,limite);

        int cantidad = movimientoCasaCambioService.countCasacambioidByDate(numdni);

        return ImmutableMap.of("listcasacambio",movimientoCasaCambios, "count",cantidad);

    }
}
