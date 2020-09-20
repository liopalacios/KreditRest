package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovimientoCasaCambio {

    private int idMovimientoCasacambio;
    private int idCaja;
    private int idusuario;
    private int idTipoOperacion;
    private BigDecimal cantidadCambiar;
    private double fisico;
    private BigDecimal clienteRecibe;
    private int idOperacionPadre;
    private int idMonedaDestino;
    private int idMonedaOrigen;
    private String nroTicket;
    private String numDocu;
    private Date fechaReg;
    private Date fechaDefault;
    private double tipoCambio;
    private int idUsuario;
    private int estadoenv;
    private int estadomnsag;
    private String mnsagsunat;
    private String linkticket;
    private String linkpdf;
    private String linkxml;
    private String operacion;
    private int idvoucher;
    private int tipoNroDocCli;
}
