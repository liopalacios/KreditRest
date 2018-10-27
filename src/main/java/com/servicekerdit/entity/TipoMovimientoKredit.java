package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "pptm_tipo_movimiento")
@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TipoMovimientoKredit implements Serializable {

    @Id
    @Column(name = "co_tipo_movimiento")
    private String codigo;

    @Column(name = "de_tipo_movimiento")
    private String detalle;

    @Column(name = "es_tipo_movimiento")
    private String estado;
}
