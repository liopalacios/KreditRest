package com.servicekerdit.service.impl;

import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.repository.MovimientoCasaCambioRepository;
import com.servicekerdit.repository.con.Conexion;
import com.servicekerdit.service.MovimientoCasaCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoCasaCambioServiceImpl implements MovimientoCasaCambioService {
    Connection connection = null;
    Connection connectionb = null;
    @Autowired
    MovimientoCasaCambioRepository movimientoCasaCambioRepository;

    @Override
    public List<MovimientoCasaCambio> findAll() {
        return movimientoCasaCambioRepository.findAll();
    }

    @Override
    public List<MovimientoCasaCambio> findFecReg(int page, int limite, int userid) throws Exception {
        Date date1 = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        java.sql.Date sDate = new java.sql.Date(c.getTimeInMillis());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        page = page-1;
        page=limite*page;

        String dateInString = "2020-05-09";
        Date date = df.parse(dateInString);
        connection = Conexion.conectarPosgressGPS();
        return movimientoCasaCambioRepository.findFecReg(sDate,userid,page,limite,connection);
    }

    @Override
    public int countCasacambioidByDate(int userid) throws Exception {
        Date date1 = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        connectionb = Conexion.conectarPosgressGPS();
        return movimientoCasaCambioRepository.countCasacambioByDate(userid,df.format(date1),connectionb);
    }

    @Override
    public List<MovimientoCasaCambio> findNoSend() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2020-05-08";
        Date date = df.parse(dateInString);
        Date date1 = new Date();
        System.out.println(" CAMBIOS CASA ");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        return movimientoCasaCambioRepository.findNoSend(c.getTime());
    }

    @Override
    @Transactional
    public void save(MovimientoCasaCambio movimientoCasaCambio) {
        movimientoCasaCambioRepository.save(movimientoCasaCambio);
    }
}
