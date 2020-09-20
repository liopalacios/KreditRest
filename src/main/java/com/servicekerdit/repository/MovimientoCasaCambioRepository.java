package com.servicekerdit.repository;

import com.servicekerdit.entity.MovimientoCasaCambio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import java.util.List;

public interface MovimientoCasaCambioRepository {
    List<MovimientoCasaCambio> findAll();

    List<MovimientoCasaCambio> findFecRegNoEnv(String date1,int page, int limite);

    int countCasacambioByDate(int userid, String date1, Connection connectionb) throws SQLException;

    List<MovimientoCasaCambio> findFecReg(java.sql.Date format, int ncaja, int page, int limite, Connection connection) throws SQLException;

    List<MovimientoCasaCambio> findNoSend(Date format);

    void save(MovimientoCasaCambio movimientoCasaCambio);
}
