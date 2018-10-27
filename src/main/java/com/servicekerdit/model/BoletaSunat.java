package com.servicekerdit.model;

import lombok.*;


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

    private double total_venta_exportacion;
    private double total_venta_gravadas;
    private double total_venta_inafectas;
    private double total_venta_exoneradas;
    private double total_venta_gratuitas;

    private double total_imp_ope_grat;
    private double total_descuento;
    private double total_igv;
    private double total_isc;
    private double total_otros_tributos;
    private double descuentos_globales;

    private double total_otros_cargos;
    private double importe_total;
    private String tipo_operacion;
    private String tipo_nota_credito;
    private String tipo_nota_debito;

    private String documento_afectado_serie;
    private String documento_afectado_numero;
    private String documento_afectado_tipo_comprobante;
    private String documento_afectado_motivo_nota;

    private String orden_compra;
    private String detraccion;
    private double porcentaje_detraccion;
    private String codigo_estado;
    private String tipo_estado;
    private String mensaje;
    private List<DetalleSunat> items;



    //private boolean editClient;
}
