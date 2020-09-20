package com.servicekerdit.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CampoAdicional
{
    private String nombre_campo;
    private String valor_campo;

}
