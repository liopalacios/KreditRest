package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;


@Table(name = "pstd_comprobante_pago")
@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoletaDetalleKredit implements Serializable {

    @Column(name = "co_empresa")
    private String empresa;

    @Column(name = "co_serie")
    private String serieid;

    @Id
    @Column(name = "nu_comprobante")
    private Integer comprobanteid;

    @Column(name = "co_tipo_doc_pago")
    private String tipopago;

    @Column(name = "nu_detalle")
    private Integer nudetalle;

    @Valid
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="co_producto", insertable=false,updatable=false)
    private ProductoKredit producto;


    @Column(name = "va_igv")
    private double vaigv;
    // "igv": 27.45762711864407,
    //"valor_venta": 152.54237288135593,
    //"valor_unitario": 152.54237288135593,

    @Column(name = "va_interes")
    private double vainteres;

    @Column(name = "va_amortizado")
    private double vaamortizado;

    @Column(name = "va_gasto_admin")
    private double vagastoadmin;

    @Column(name = "va_trans_custodia")
    private double vatranscustodia;

    @Column(name = "va_mora")
    private double vamora;

    @Column(name = "va_descuento")
    private double vadescuento;

    @Column(name = "va_total_pago")
    private double vatotalpago;

    @Column(name = "va_capital_deuda")
    private double vacapital;

    @Column(name = "fe_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechavencimiento;

    @Column(name = "va_comision")
    private double vacomision;

    @Column(name = "va_prima")
    private double vaprima;

    @Column(name = "va_otrointeres")
    private double otrointeres;
    //"precio_total": 180,
    //"precio_venta_unitario": 180,


    // "isTextAreaDescription": false,
    // "inc_igv": true,
    // "unidad_medida": "NIU",
    // "cantidad": 1,
    // "descripcion": "PANTALON",
    // "tipo_igv": "10"

}
