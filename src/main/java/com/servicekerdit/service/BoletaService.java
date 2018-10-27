package com.servicekerdit.service;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface BoletaService {
    BoletaKredit findBoletaKreditBySereid(int idboleta);

    List<BoletaKredit> findBoletaKreditByClienteid(String dnicliente);

    List<BoletaKredit> revisaBoletaKredit(String clienteid) throws ParseException;
}
