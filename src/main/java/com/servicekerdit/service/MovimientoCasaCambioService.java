package com.servicekerdit.service;

import com.servicekerdit.entity.MovimientoCasaCambio;

import java.text.ParseException;
import java.util.List;

public interface MovimientoCasaCambioService {
    List<MovimientoCasaCambio> findAll();

    List<MovimientoCasaCambio> findFecReg(int page, int limite, int userid) throws Exception;

    int countCasacambioidByDate(int userid) throws Exception;

    List<MovimientoCasaCambio> findNoSend() throws ParseException;

    void save(MovimientoCasaCambio movimientoCasaCambio);
}
