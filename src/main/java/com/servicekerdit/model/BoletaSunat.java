package com.servicekerdit.model;

import lombok.*;


import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoletaSunat {

    private String documento_tipo_comprobante;
    private String documento_serie;
    private Integer documento_numero;
    private String documento_fecha_emision;
    private Time documento_hora_emision;
    private String documento_fecha_vencimiento;
    private String documento_tipo_moneda;

    private String cliente_tipo_documento;
    private String cliente_numero_documento;
    private String cliente_denominacion;
    private String cliente_direccion;
    private String cliente_email;

    private BigDecimal total_venta_exportacion;
    private BigDecimal total_venta_gravadas;
    private BigDecimal total_venta_inafectas;
    private BigDecimal total_venta_exoneradas;
    private BigDecimal total_venta_gratuitas;

    private BigDecimal total_imp_ope_grat;
    private BigDecimal total_descuento;
    private BigDecimal total_igv;
    private BigDecimal total_isc;
    private BigDecimal total_otros_tributos;
    private BigDecimal descuentos_globales;

    private BigDecimal total_otros_cargos;
    private BigDecimal importe_total;
    private String tipo_operacion;
    private String tipo_nota_credito;
    private String tipo_nota_debito;

    private String documento_afectado_serie;
    private String documento_afectado_numero;
    private String documento_afectado_tipo_comprobante;
    private String documento_afectado_motivo_nota;

    private String orden_compra;
    private String detraccion;
    private BigDecimal porcentaje_detraccion;
    private String codigo_estado;
    private String tipo_estado;
    private String mensaje;
    private List<DetalleSunat> items;



    //private boolean editClient;
}
