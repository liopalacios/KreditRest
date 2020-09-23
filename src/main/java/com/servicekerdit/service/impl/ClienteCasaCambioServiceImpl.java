package com.servicekerdit.service.impl;

import com.servicekerdit.entity.ClienteCasaCambio;
import com.servicekerdit.repository.ClienteCasaCambioRepository;
import com.servicekerdit.repository.con.Conexion;
import com.servicekerdit.service.ClienteCasaCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

@Service
public class ClienteCasaCambioServiceImpl implements ClienteCasaCambioService {

    Connection connection = null;

    @Autowired
    ClienteCasaCambioRepository clienteCasaCambioRepository;

    @Override
    public List<ClienteCasaCambio> findByNumdocu(String numdoc) throws Exception {
        connection = Conexion.conectarPosgressGPS();
        return clienteCasaCambioRepository.findByNumdoc(numdoc,connection);
    }
}
