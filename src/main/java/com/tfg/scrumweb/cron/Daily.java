package com.tfg.scrumweb.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tfg.scrumweb.service.ProyectoService;

@Component
public class Daily {
    @Autowired
    private ProyectoService proyectoService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetColumnValue() {
        proyectoService.setAllDailyTo0();
    }
}