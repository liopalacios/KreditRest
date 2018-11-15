package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "pstc_comprobante_pago")
@IdClass(BoletaKreditIdentity.class)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoletaKredit implements Serializable {

  //  @EmbeddedId
 //   private BoletaKreditIdentity boletaKreditIdentity;
    @Id
    @Column(name = "co_empresa")
    private String empresa;

    @Id
    @Column(name = "co_oficina")
    private String oficina;

    @Id
    @Column(name = "co_producto")
    private String producto;

    @Id
    @Column(name = "co_serie")
    private String serieid;

    @Id
    @Column(name = "nu_comprobante")
    private Integer comprobanteid;

    @Valid
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="co_tipo_doc_pago", insertable=false,updatable=false)
    private TipoMovimientoKredit tipopago;


    //@Valid
    @ManyToOne
    @JoinColumn(name="co_cliente")
    private ClienteKredit clienteid;

    @Column(name = "nu_contrato")
    private String nucontrato;

    @Column(name = "fe_impresion")
    @Temporal(TemporalType.DATE)
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

    @Column(name = "estadomnsag")
    private int estadomnsag;

    @Column(name = "linkticket")
    private String linkticket;

    @Column(name = "linkpdf")
    private String linkpdf;

    @Column(name = "linkxml")
    private String linkxml;

    @Column(name = "mnsagsunat")
    private String mnsagsunat;

}
