package com.servicekerdit.repository;

import com.servicekerdit.entity.ClienteCasaCambio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ClienteCasaCambioRepository {
    List<ClienteCasaCambio> findByNumdoc(String numdoc, Connection connection) throws SQLException;
}
