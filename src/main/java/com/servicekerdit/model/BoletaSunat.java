package com.servicekerdit.model;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoletaSunat {

    private String cliente_tipo_documento;
    private String cliente_numero_documento;
    private String cliente_denominacion;
    private String cliente_direccion;
    private String cliente_email;

    private String tipo_operacion;
    private String documento_serie;
    private int documento_numero;
    private Date documento_fecha_emision;
    private Date documento_fecha_vencimiento;

    private Time documento_hora_emision;
    private String documento_tipo_moneda;
    private double total_venta_gravadas;
    private double total_venta_inafectas;
    private double total_venta_exoneradas;

    private double total_venta_gratuitas;
    private double total_descuento;
    private double total_igv;
    private double total_otros_cargos;
    private double importe_total;

    private String documento_afectado_tipo_comprobante;
    private String documento_afectado_serie;
    private String documento_afectado_numero;
    private String tipo_nota_credito;
    private String tipo_nota_debito;

    private String documento_afectado_motivo_nota;
    private double detraccion;
    private double porcentaje_detraccion;
    private String orden_compra;



    private String documento_tipo_comprobante;
    private boolean editClient;
}
