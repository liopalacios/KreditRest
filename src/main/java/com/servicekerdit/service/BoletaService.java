package com.servicekerdit.service;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface BoletaService {
    BoletaKredit findBoletaKreditBySereid(int idboleta);

    List<BoletaKredit> findBoletaKreditByClienteid(String dnicliente,int page, int porpage);

    List<BoletaKredit> revisaBoletaKredit(String clienteid) throws ParseException;

    List<BoletaKredit> revisaBoletaKreditDay() throws ParseException;

    List<BoletaKredit> revisaBoletaKreditdni(String clienteid) throws ParseException;

    long countByClienteid(String idcliente);

    void save(BoletaKredit boletaKredit);

    List<BoletaKredit> findBoletaKreditByClienteiddni(String clienteid, int pagenumber, int perpage);

    long countByClientedni(String clienteid);

    List<BoletaKredit> revisaBoletaKreditProductContratoMoneda(String productoid, String contratoId, String monedaId) throws ParseException;

    List<BoletaKredit> findBoletaKreditByProductoidAndContratoidAndMonedaid(String productoid, String contratoid, String monedaid, int pagenumber, int perpage);

    long countByProductoidAndContratoidAndMonedaid(String productoid, String contratoid, String monedaid);


}
