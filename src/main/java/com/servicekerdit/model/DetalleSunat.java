package com.servicekerdit.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DetalleSunat
{
    private boolean isTextAreaDescription;
    private boolean inc_igv;
    private String unidad_medida;
    private int cantidad;
    private String descripcion;

    private String codigo_producto_interno;
    private double valor_unitario;
    private double precio_total;
    private double valor_venta;
    private double precio_venta_unitario;

    private double igv;
    private String tipo_igv;
}
