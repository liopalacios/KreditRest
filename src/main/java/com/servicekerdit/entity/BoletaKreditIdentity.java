package com.servicekerdit.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class BoletaKreditIdentity implements Serializable {


    @Column(name = "co_serie")
    private String serieid;


    @Column(name = "nu_comprobante")
    private Integer comprobanteid;


    @Column(name = "co_empresa")
    private String empresa;


    @Column(name = "co_oficina")
    private String oficina;


    @Column(name = "co_producto")
    private String producto;



    @Override
    public boolean equals(Object obj) {
        try {

            if (this == obj) return true;

            return serieid.equals(((BoletaKredit)obj).getSerieid()) &&

                    comprobanteid.equals(((BoletaKredit)obj).getComprobanteid()) &&

                    empresa.equals(((BoletaKredit)obj).getEmpresa())&&

                    oficina.equals(((BoletaKredit)obj).getOficina())&&

                    producto.equals(((BoletaKredit)obj).getProducto());



        } catch (Throwable ignored) { //catch NP & Cast Exceptions

            return false;

        }

    }

    @Override
    public int hashCode() {

        return Objects.hash(serieid + comprobanteid + empresa + oficina + producto);
    }
}
