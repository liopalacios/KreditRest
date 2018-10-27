package com.servicekerdit.service.impl;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.repository.BoletaRepository;
import com.servicekerdit.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BoletaServiceImpl implements BoletaService {

    private static final int ESTNE = 0;
    @Autowired
    BoletaRepository boletaRepository;

    @Override
    public BoletaKredit findBoletaKreditBySereid(int idboleta) {
        return boletaRepository.findByComprobanteid(idboleta);
    }

    @Override
    public List<BoletaKredit> findBoletaKreditByClienteid(String dnicliente) {

        return boletaRepository.findByClienteidOrderByFechaimpDesc(ClienteKredit.builder().clienteidp(dnicliente).build());

    }

    @Override
    public List<BoletaKredit> revisaBoletaKredit(String clienteid) throws ParseException {
        String sDate1="28-08-2018";
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
        List<BoletaKredit> boletaKredits = boletaRepository.findByClienteidAndEstadoenvAndFechaimp(ClienteKredit.builder().clienteidp(clienteid).build(),ESTNE,date1);

        for(BoletaKredit boletaKredit: boletaKredits){
            if(boletaKredit.getEstadoenv()==0){

            }
            System.out.print(boletaKredit.getSerieid()+" "+boletaKredit.getSubtotal()+"\n");
        }





        return boletaKredits;
    }
}
