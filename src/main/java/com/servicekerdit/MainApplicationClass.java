package com.servicekerdit;

import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.service.BoletaService;
import com.servicekerdit.service.MovimientoCasaCambioService;
import com.servicekerdit.service.impl.BoletaServiceImpl;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class MainApplicationClass {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {

        context = SpringApplication.run(MainApplicationClass.class, args);



    }
/*
    @Scheduled(cron = "0 17 18 * * *")
    public void scheduleTaskUsingCronExpression() {

        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);
        ApplicationArguments arguments = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(Application.class, arguments.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();

    }*/
}
