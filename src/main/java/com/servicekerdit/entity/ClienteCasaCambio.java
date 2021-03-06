package com.servicekerdit.entity;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClienteCasaCambio {

    private int idClienteCasacambio;
    private String numDocu;
    private String razonSocial;
    private int idTipoDocumento;

}
