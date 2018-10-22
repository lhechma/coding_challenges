package com.n26.web.configuration;

import com.n26.models.workers.cleanup.StaleTransactionEventScavenger;
import com.n26.models.workers.consumers.MessagingOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Component
public class InfrastructureInitializer implements ServletContextInitializer {

    private StaleTransactionEventScavenger scavenger;
    private MessagingOrchestrator messagingOrchestrator;

    @Autowired
    public InfrastructureInitializer(StaleTransactionEventScavenger scavenger, MessagingOrchestrator messagingOrchestrator) {
        this.scavenger = scavenger;
        this.messagingOrchestrator = messagingOrchestrator;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        scavenger.scavenge();
        messagingOrchestrator.start();

    }
}
