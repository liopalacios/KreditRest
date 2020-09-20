package com.servicekerdit.model;

import lombok.*;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DetalleSunat
{
    //private boolean isTextAreaDescription;
    //private boolean inc_igv;

    private String unidad_medida;
    private BigDecimal cantidad;
    private String descripcion;
    private String codigo_producto_interno;
    private String codigo_producto_sunat;
    private String codigo_producto_gs1;
    private BigDecimal valor_unitario;
    private BigDecimal valor_venta;
    private BigDecimal descuento;
    private String codigo_descuento;
    private BigDecimal precio_venta_unitario;
    private BigDecimal valor_referencial_unitario;
    private BigDecimal monto_base_igv;
    private BigDecimal monto_base_ivap;
    private BigDecimal monto_base_exportacion;
    private BigDecimal monto_base_exonerado;
    private BigDecimal monto_base_inafecto;
    private BigDecimal monto_base_gratuito;
    private BigDecimal monto_base_isc;
    private BigDecimal monto_base_otros_trib;
    private BigDecimal igv;
    private BigDecimal isc;
    private BigDecimal ivap;
    private BigDecimal imp_vta_grat;
    private BigDecimal imp_otros_trib;
    private BigDecimal porcentaje_igv;
    private BigDecimal procentaje_ivap;
    private BigDecimal porcentaje_isc;
    private BigDecimal porcentaje_otros_trib;
    private BigDecimal porcentaje_trib_vta_grat;
    private String tipo_afectacion_igv;
    private String tipo_isc;
    private Boolean habilitarTipo;
    private Boolean inc_igv;
    private Boolean mostraPorc;
    private BigDecimal precio_total;
    private Boolean isTextAreaDescription;
    private String mostrarTransporte;
}
