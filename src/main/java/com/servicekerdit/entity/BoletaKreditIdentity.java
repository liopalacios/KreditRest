package com.servicekerdit.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class BoletaKreditIdentity implements Serializable {

    @Id
    @Column(name = "co_serie")
    private String serieid;

    @Id
    @Column(name = "nu_comprobante")
    private Integer comprobanteid;

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
    @Valid
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="co_tipo_doc_pago", insertable=false,updatable=false)
    private TipoMovimientoKredit tipopago;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoletaKreditIdentity)) return false;
        BoletaKreditIdentity that = (BoletaKreditIdentity) o;
        return Objects.equals(serieid, that.serieid) &&
                Objects.equals(comprobanteid, that.comprobanteid) &&
                Objects.equals(empresa, that.empresa) &&
                Objects.equals(oficina, that.oficina) &&
                Objects.equals(producto, that.producto) &&
                Objects.equals(tipopago, that.tipopago);
    }

    @Override
    public int hashCode() {

        return Objects.hash(serieid, comprobanteid, empresa, oficina, producto, tipopago);
    }
}
