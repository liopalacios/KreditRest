package com.servicekerdit.service;

import com.servicekerdit.entity.BoletaDetalleKredit;

public interface BoletaDetalleService {

    BoletaDetalleKredit findBySerieidAndComprobanteid(String documento_serie, int documento_numero);
}
