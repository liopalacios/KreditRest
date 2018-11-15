package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.Entity;
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
    private int tipooperacion;
    private double cantcambio;
    private double recibecli;
    private int nuoperacion;
    private String nuticket;
    private Date fechareg;
    private String login;
    private String tdescripcion;
    private String signo;
}
