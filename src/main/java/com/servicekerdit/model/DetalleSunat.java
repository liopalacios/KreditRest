package com.servicekerdit.model;

import lombok.*;



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
    private int cantidad;
    private String descripcion;
    private String codigo_producto_interno;
    private String codigo_producto_sunat;
    private String codigo_producto_gs1;
    private double valor_unitario;
    private double valor_venta;
    private double descuento;
    private String codigo_descuento;
    private double precio_venta_unitario;
    private double valor_referencial_unitario;
    private double monto_base_igv;
    private double monto_base_ivap;
    private double monto_base_exportacion;
    private double monto_base_exonerado;
    private double monto_base_inafecto;
    private double monto_base_gratuito;
    private double monto_base_isc;
    private double monto_base_otros_trib;
    private double igv;
    private double isc;
    private double ivap;
    private double imp_vta_grat;
    private double imp_otros_trib;
    private double porcentaje_igv;
    private double procentaje_ivap;
    private double porcentaje_isc;
    private double porcentaje_otros_trib;
    private double porcentaje_trib_vta_grat;
    private String tipo_afectacion_igv;
    private String tipo_isc;


}
