package com.n26.models.config;

import com.n26.configuration.TransactionProperties;
import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.MessageQueue;
import com.n26.models.workers.cleanup.StaleTransactionEventScavenger;
import com.n26.models.workers.consumers.TimeOrderingSensitiveMessagingOrchestrator;
import com.n26.models.workers.consumers.FifoMessagingOrchestrator;
import com.n26.models.workers.consumers.MessagingOrchestrator;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BusinessConfig {

    @Bean
    StatisticsCalculator calculator() {
        return new StatisticsCalculator();
    }

    @Bean
    StaleTransactionEventScavenger staleTransactionEventScavenger(@Qualifier("delay") MessageQueue<TransactionCommand> delay, StatisticsCalculator calculator) {
        return new StaleTransactionEventScavenger(delay, calculator);
    }


    @Bean
    @ConditionalOnProperty(prefix = "runtime.profile", value = "fifo", havingValue = "true")
    MessagingOrchestrator fifoMessagingOrchestrator(@Qualifier("inboundMessageQueue") MessageQueue<TransactionCommand> inboundMessageQueue, @Qualifier("delay") MessageQueue<TransactionCommand> delay, StatisticsCalculator calculator) {
        return new FifoMessagingOrchestrator(inboundMessageQueue, delay, calculator);
    }


    @Bean
    @ConditionalOnBean(name = "fifoMessagingOrchestrator")
    @ConditionalOnMissingBean
    MessagingOrchestrator timeOrderingMessagingOrchestrator(@Qualifier("inboundMessageQueue") MessageQueue<TransactionCommand> inboundMessageQueue, @Qualifier("heap") MessageQueue<TransactionCommand> heap, @Qualifier("delay") MessageQueue<TransactionCommand> delay, StatisticsCalculator calculator) {
        return new TimeOrderingSensitiveMessagingOrchestrator(inboundMessageQueue, heap, delay, calculator);
    }

}
