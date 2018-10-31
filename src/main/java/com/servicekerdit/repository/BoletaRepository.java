package com.servicekerdit.repository;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<BoletaKredit,String> {


    BoletaKredit findByComprobanteid(int idboleta);

    List<BoletaKredit> findByClienteid_ClienteidOrderByFechaimpDesc(String clienteId, Pageable pageRequest);

    List<BoletaKredit> findByClienteidAndEstadoenv(ClienteKredit cliente, int estne);

    List<BoletaKredit> findByClienteidAndEstadoenvAndFechaimp(ClienteKredit build, int estne, Date format);

    long countByClienteid(ClienteKredit build);
}
