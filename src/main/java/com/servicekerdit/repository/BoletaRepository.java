package com.servicekerdit.repository;

import com.servicekerdit.entity.BoletaKredit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<BoletaKredit,Long> {


    BoletaKredit findByComprobanteid(long idboleta);
}
