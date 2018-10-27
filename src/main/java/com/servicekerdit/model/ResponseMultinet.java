package com.servicekerdit.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseMultinet {
    private Boolean estado;
    private String mensaje;
    private String nombre;
    private String urlPdfTicket;
    private String urlPdfA4;
    private String urlXml;
}
