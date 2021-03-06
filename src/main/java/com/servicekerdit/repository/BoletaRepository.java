package com.servicekerdit.repository;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.entity.TipoMovimientoKredit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<BoletaKredit,String> {

    BoletaKredit findByComprobanteid(int idboleta);

    List<BoletaKredit> findByClienteid_ClienteidOrderByFechaimpDesc(String clienteId, Pageable pageRequest);

    List<BoletaKredit> findByClienteid_DocumentoOrderByFechaimpDesc(String clienteId, Pageable pageRequest);

    List<BoletaKredit> findByClienteidAndEstadoenv(ClienteKredit cliente, int estne);

    List<BoletaKredit> findByClienteidAndEstadoenvAndFechaimp(ClienteKredit build, int estne, Date format);

    long countByClienteid(ClienteKredit build);

    @Transactional
    @Modifying
    @Query("update BoletaKredit set estadomnsag=1, estadoenv=1, linkticket= :linkticket, linkpdf= :linkpdf where co_empresa= :coempresa and co_oficina= :coficna and co_producto= :coprod and co_serie= :serieid and nu_comprobante= :comprobanteid and co_tipo_doc_pago= :cotipo")
    void saveboleta(@Param("serieid") String serieid, @Param("comprobanteid") int comprobanteid,@Param("linkticket") String linkticket, @Param("linkpdf") String linkpdf, @Param("coempresa") String lincoempresakpdf, @Param("coficna") String coficna, @Param("coprod") String coprod, @Param("cotipo") String cotipo);

   // BoletaKredit findByComprobanteidcomposite(String coempresa, String coficina, String coprod, String coserie, int idboleta, String cotipo);

    BoletaKredit findByEmpresaAndOficinaAndProductoAndSerieidAndComprobanteidAndTipopago(String coempresa, String coficina, String coprod, String coserie, int idboleta, TipoMovimientoKredit cotipo);

    List<BoletaKredit> findByClienteid_DocumentoAndEstadoenvAndFechaimp(String build, int estne, Date date1);

    long countByClienteid_Documento(String build);

    List<BoletaKredit> findByProductoAndNucontratoAndComonedaAndEstadoenvAndFechaimp(String productoid, String contratoId, String monedaId, int estne, Date date1);

    List<BoletaKredit> findByProductoAndNucontratoAndComonedaOrderByFechaimpDesc(String productoid, String contratoid, String monedaid, Pageable pageable);

    long countByProductoAndNucontratoAndComoneda(String productoid, String contratoid, String monedaid);

    List<BoletaKredit> findByEstadoenvAndFechaimp(int estne, Date date1);

    List<BoletaKredit> findByCotipopagoAndEstadoenvAndFechaimpAfter(String tipopago, int estne, Date date1);

    List<BoletaKredit> findByClienteidAndEstadoenvAndFechaimpAfterAndCotipopago(ClienteKredit build, int estne, Date date1, String tipopago);

    List<BoletaKredit> findByClienteid_DocumentoAndEstadoenvAndFechaimpAfterAndCotipopago(String clienteid, int estne, Date date, String tipopago);

    List<BoletaKredit> findByProductoAndNucontratoAndComonedaAndEstadoenvAndFechaimpAfterAndCotipopago(String productoid, String contratoId, String monedaId, int estne, Date date, String tipopago);
}