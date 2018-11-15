package com.servicekerdit.service;

import com.servicekerdit.entity.MovimientoCasaCambio;

import java.util.List;

public interface MovimientoCasaCambioService {
    List<MovimientoCasaCambio> findAll();

    List<MovimientoCasaCambio> findFecReg(int page, int limite);

    int countCasacambioidByDate(String numdni);

    List<MovimientoCasaCambio> findNoSend();
}
