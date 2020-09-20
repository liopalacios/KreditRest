package com.servicekerdit.service.impl;

import com.servicekerdit.entity.BoletaDetalleKredit;
import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.entity.TipoMovimientoKredit;
import com.servicekerdit.repository.BoletaRepository;
import com.servicekerdit.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BoletaServiceImpl implements BoletaService {

    private static final int ESTNE = 0;
    private static final String TIPOPAGO = "02";
    @Autowired
    BoletaRepository boletaRepository;

    @Override
    public BoletaKredit findBoletaKreditBySereid(int idboleta) {
        String coempresa="001";
        String coficina="001";
        String coprod="01";
        String coserie="001";
        String cotipo="01";
        return boletaRepository.findByEmpresaAndOficinaAndProductoAndSerieidAndComprobanteidAndTipopago(coempresa,coficina,coprod,coserie,idboleta,TipoMovimientoKredit.builder().codigo("02").build());
    }

    @Override
    public List<BoletaKredit> findBoletaKreditByClienteid(String idcliente, int page, int porpage) {

        Pageable pageable = new PageRequest(page-1, porpage);
        List<BoletaKredit> boletaKredits = boletaRepository.findByClienteid_ClienteidOrderByFechaimpDesc(idcliente,pageable);
        return boletaKredits;

    }
    @Override
    public List<BoletaKredit> findBoletaKreditByClienteiddni(String clienteid, int page, int porpage) {
        Pageable pageable = new PageRequest(page-1, porpage);
        List<BoletaKredit> boletaKredits = boletaRepository.findByClienteid_DocumentoOrderByFechaimpDesc(clienteid,pageable);
        return boletaKredits;
    }

    @Override
    public List<BoletaKredit> findBoletaKreditByProductoidAndContratoidAndMonedaid(String productoid, String contratoid, String monedaid, int pagenumber, int perpage) {
        Pageable pageable = new PageRequest(pagenumber-1, perpage);
        List<BoletaKredit> boletaKredits = boletaRepository.findByProductoAndNucontratoAndComonedaOrderByFechaimpDesc(productoid,contratoid,monedaid,pageable);
        return boletaKredits;
    }
// REVISAR LAS BOLETAS QUE ESTAN PENDIENTES DE CLIENTE INGRESADO
    @Override
    public List<BoletaKredit> revisaBoletaKredit(String clienteid) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        ClienteKredit clienteKredit = new ClienteKredit(clienteid);
        //List<BoletaKredit> boletaKredits = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "06-05-2020";
        Date date = sdf.parse(dateInString);
        Date date1 = new Date();
        System.out.println(date); //Tue Aug 31 10:20:56 SGT 1982
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        List<BoletaKredit> boletaKredits = boletaRepository.findByClienteidAndEstadoenvAndFechaimpAfterAndCotipopago(
                ClienteKredit.builder().clienteid(clienteid).build(),ESTNE,c.getTime(),TIPOPAGO);

        return boletaKredits;
    }

    @Override
    public List<BoletaKredit> revisaBoletaKreditDay() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "06-05-2020";
        Date date = sdf.parse(dateInString);
        Date date1 = new Date();
        System.out.println(date1); //Tue Aug 31 10:20:56 SGT 1982
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        List<BoletaKredit> boletaKredits = boletaRepository.findByCotipopagoAndEstadoenvAndFechaimpAfter(TIPOPAGO,
                ESTNE,c.getTime());

        return boletaKredits;
    }
   // REVISA BOLETAS PARA ENVIAR POR DNI
    @Override
    public List<BoletaKredit> revisaBoletaKreditdni(String clienteid) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        ClienteKredit clienteKredit = new ClienteKredit(clienteid);
        //List<BoletaKredit> boletaKredits = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "29-04-2020";
        Date date = sdf.parse(dateInString);
        Date date1 = new Date();
        System.out.println(date1); //Tue Aug 31 10:20:56 SGT 1982
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        List<BoletaKredit> boletaKredits = boletaRepository.findByClienteid_DocumentoAndEstadoenvAndFechaimpAfterAndCotipopago(
                clienteid,ESTNE,c.getTime(),TIPOPAGO);

        return boletaKredits;
    }

    @Override
    public long countByClienteid(String idcliente) {
        return boletaRepository.countByClienteid(ClienteKredit.builder().clienteid(idcliente).build());
    }
    @Override
    public long countByClientedni(String clienteid) {
        return boletaRepository.countByClienteid_Documento(clienteid);
    }
    @Override
    public long countByProductoidAndContratoidAndMonedaid(String productoid, String contratoid, String monedaid) {
        return boletaRepository.countByProductoAndNucontratoAndComoneda(productoid,contratoid,monedaid);
    }
    //BUSCAR BOLETAS PARA ENVIAR POR CONTRATO
    @Override
    public List<BoletaKredit> revisaBoletaKreditProductContratoMoneda(String productoid, String contratoId, String monedaId) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "06-05-2020";
        Date date = sdf.parse(dateInString);
        Date date1 = new Date();
        System.out.println(date1); //Tue Aug 31 10:20:56 SGT 1982
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        List<BoletaKredit> boletaKredits = boletaRepository.findByProductoAndNucontratoAndComonedaAndEstadoenvAndFechaimpAfterAndCotipopago(
                productoid,contratoId,monedaId,ESTNE,c.getTime(),TIPOPAGO);

        return boletaKredits;
    }

    @Override
    public void save(BoletaKredit boletaKredit) {
        System.out.print(boletaKredit);
        System.out.print(" INSRTENDO BOLETA ACTUALIZADA ");
        boletaRepository.saveboleta(boletaKredit.getSerieid(),boletaKredit.getComprobanteid(),boletaKredit.getLinkticket(),boletaKredit.getLinkpdf(),boletaKredit.getEmpresa(),boletaKredit.getOficina(),boletaKredit.getProducto(),boletaKredit.getTipopago().getCodigo());
    }

}
