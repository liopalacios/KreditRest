package com.servicekerdit;

import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.service.BoletaService;
import com.servicekerdit.service.MovimientoCasaCambioService;
import com.servicekerdit.service.impl.BoletaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MainApplicationClass {



    public static void main(String[] args) {

        SpringApplication.run(MainApplicationClass.class, args);



    }
}
