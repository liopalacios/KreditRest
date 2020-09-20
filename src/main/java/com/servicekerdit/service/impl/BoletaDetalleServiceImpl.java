package com.servicekerdit.service.impl;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.repository.BoletaDetalleRepository;
import com.servicekerdit.service.BoletaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoletaDetalleServiceImpl implements BoletaDetalleService {

    @Autowired
    BoletaDetalleRepository boletaDetalleRepository;

    @Override
    public BoletaDetalleKredit findBySerieidAndComprobanteid(String documento_serie, int documento_numero, String tipopago) {
        return boletaDetalleRepository.findBySerieidAndComprobanteidAndTipopago(documento_serie,documento_numero, tipopago);
    }

}
