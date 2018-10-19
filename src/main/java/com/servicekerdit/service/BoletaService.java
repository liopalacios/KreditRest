package com.servicekerdit.service;

import com.servicekerdit.entity.BoletaKredit;

public interface BoletaService {
    BoletaKredit findBoletaKreditBySereid(long idboleta);
}
