package com.servicekerdit.service;

import com.servicekerdit.entity.ClienteCasaCambio;

import java.util.List;

public interface ClienteCasaCambioService {

    List<ClienteCasaCambio> findByNumdocu(String numdoc) throws Exception;
}
