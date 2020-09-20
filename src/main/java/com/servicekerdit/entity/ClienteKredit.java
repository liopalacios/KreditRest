package com.servicekerdit.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "pstc_cliente")
@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteKredit implements Serializable {

    @Id
    @Column(name = "co_cliente")
    private String clienteid;

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
    private String documentotipo;

    @Column(name = "de_direccion")
    private String direccion;


    public ClienteKredit(String clienteid) {
        this.clienteid=clienteid;
    }
}
