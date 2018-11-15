package com.servicekerdit.repository;

import com.servicekerdit.entity.MovimientoCasaCambio;

import java.util.Date;
import java.util.List;

public interface MovimientoCasaCambioRepository {
    List<MovimientoCasaCambio> findAll();

    List<MovimientoCasaCambio> findFecRegNoEnv(String date1,int page, int limite);

    int countCasacambioByDate(String numdni, String date1);

    List<MovimientoCasaCambio> findFecReg(String format, int page, int limite);

    List<MovimientoCasaCambio> findNoSend(String format);
}
