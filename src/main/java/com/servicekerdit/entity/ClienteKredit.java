package com.servicekerdit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "pstc_cliente")
@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClienteKredit implements Serializable {

    @Id
    @Column(name = "co_cliente")
    private String clienteidp;


    @Column(name = "co_empresa")
    private String empresa;

    @Column(name = "de_apepat")
    private String apellidop;

    @Column(name = "de_apemat")
    private String apellidom;

    @Column(name = "de_nombre")
    private String nombre;

    @Column(name = "nu_documento")
    private String documento;

    @Column(name = "co_tipodocumento")
    private int documentotipo;

    @Column(name = "de_direccion")
    private String direccion;



}
