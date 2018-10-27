package com.servicekerdit.repository;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaDetalleRepository extends JpaRepository<BoletaDetalleKredit,Long> {

    BoletaDetalleKredit findBySerieidAndComprobanteid(String serieid, int idboleta);
}
