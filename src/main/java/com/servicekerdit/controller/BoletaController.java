package com.servicekerdit.controller;

import com.servicekerdit.entity.BoletaKredit;
import com.servicekerdit.model.BoletaSunat;
import com.servicekerdit.service.BoletaService;
import com.servicekerdit.service.impl.BoletaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BoletaController {

    @Autowired
    BoletaService boletaService;

    @GetMapping("/boleta")
    public String index(){
        BoletaKredit boletaKredit = boletaService.findBoletaKreditBySereid(44668);
        //System.out.print("SYSTEM CERTIKREDIT RUN "+boletaService.toString());
        BoletaSunat boletaSunat = new BoletaSunat();
        boletaSunat.setCliente_tipo_documento(Integer.toString(boletaKredit.getClienteid().getDocumentotipo()));
        boletaSunat.setCliente_numero_documento(boletaKredit.getClienteid().getDocumento());
        boletaSunat.setCliente_denominacion(boletaKredit.getClienteid().getApellidop()+" "+boletaKredit.getClienteid().getApellidom()+" "+boletaKredit.getClienteid().getNombre());
        System.out.print( boletaKredit.toString());
        return "Bienvenido al controlador" +  boletaKredit.toString();
    }
}
