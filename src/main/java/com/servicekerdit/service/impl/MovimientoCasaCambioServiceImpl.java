package com.servicekerdit.service.impl;

import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.repository.MovimientoCasaCambioRepository;
import com.servicekerdit.service.MovimientoCasaCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoCasaCambioServiceImpl implements MovimientoCasaCambioService {

    @Autowired
    MovimientoCasaCambioRepository movimientoCasaCambioRepository;

    @Override
    public List<MovimientoCasaCambio> findAll() {
        return movimientoCasaCambioRepository.findAll();
    }

    @Override
    public List<MovimientoCasaCambio> findFecReg(int page, int limite) {
        Date date1 = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        page = page-1;
        page=limite*page;
        return movimientoCasaCambioRepository.findFecReg(df.format(date1),page,limite);
    }

    @Override
    public int countCasacambioidByDate(String numdni) {
        Date date1 = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return movimientoCasaCambioRepository.countCasacambioByDate(numdni,df.format(date1));
    }

    @Override
    public List<MovimientoCasaCambio> findNoSend() {
        Date date1 = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return movimientoCasaCambioRepository.findNoSend(df.format(date1));
    }
}
