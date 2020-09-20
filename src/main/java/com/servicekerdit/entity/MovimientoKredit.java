package com.servicekerdit.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Table(name = "pptc_movimiento")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovimientoKredit implements Serializable {

  //  @EmbeddedId
 //   private BoletaKreditIdentity boletaKreditIdentity;
    @Id
    @Column(name = "co_movimiento")
    private long idmovimeinto;

    //@Valid
    @ManyToOne
    @JoinColumn(name="co_tipo_movimiento")
    private TipoMovimientoKredit tipomovimiento;



}
