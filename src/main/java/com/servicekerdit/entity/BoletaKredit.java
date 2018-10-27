package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;

import java.sql.Time;
import java.util.Date;

@Table(name = "pstc_comprobante_pago")
@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoletaKredit implements Serializable {

    @Valid
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="co_cliente", insertable=false,updatable=false)
    private ClienteKredit clienteid;

    @Column(name = "co_serie")
    private String serieid;

    @Id
    @Column(name = "nu_comprobante")
    private Integer comprobanteid;

    @Column(name = "co_empresa")
    private String empresa;

    @Column(name = "co_oficina")
    private String oficina;

    @Column(name = "co_producto")
    private String producto;

    @Valid
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="co_tipo_doc_pago", insertable=false,updatable=false)
    private TipoMovimientoKredit tipopago;

    @Column(name = "nu_contrato")
    private String nucontrato;

    @Column(name = "fe_impresion")
    private Date fechaimp;

    @Column(name = "ho_crea_registro")
    private Time horegistro;

    @Column(name = "va_subtotal")
    private double subtotal;

    @Column(name = "va_toigv")
    private double toigv;

    @Column(name = "va_total1")
    private double valortotal;

    @Column(name = "va_topagado")
    private double topagado;

    @Column(name = "estadoenv")
    private int estadoenv;


}
