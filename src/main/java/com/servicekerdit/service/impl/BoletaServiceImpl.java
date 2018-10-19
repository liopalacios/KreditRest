package com.servicekerdit.service.impl;

import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.repository.BoletaRepository;
import com.servicekerdit.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoletaServiceImpl implements BoletaService {

    @Autowired
    BoletaRepository boletaRepository;

    @Override
    public BoletaKredit findBoletaKreditBySereid(long idboleta) {
        return boletaRepository.findByComprobanteid(idboleta);
    }
}
