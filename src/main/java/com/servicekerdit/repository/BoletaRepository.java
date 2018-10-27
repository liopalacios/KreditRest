package com.servicekerdit.repository;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;

import java.util.List;

public interface BoletaRepository extends JpaRepository<BoletaKredit,Long> {


    BoletaKredit findByComprobanteid(int idboleta);

    List<BoletaKredit> findByClienteidOrderByFechaimpDesc(ClienteKredit cliente);

    List<BoletaKredit> findByClienteidAndEstadoenv(ClienteKredit build, int estne);

    List<BoletaKredit> findByClienteidAndEstadoenvAndFechaimp(ClienteKredit build, int estne, Date format);
}
