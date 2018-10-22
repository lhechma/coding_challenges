package com.n26.infra;

import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.BlockingMessageQueue;
import com.n26.models.queues.MessageQueue;
import com.n26.models.queues.WrapperMessageQueue;
import com.n26.models.transactions.Transaction;
import com.n26.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Comparator.comparing;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
public class InfraConfig {

    @Bean
    @Qualifier("inboundMessageQueue")
    public MessageQueue<TransactionCommand> inbound() {
        return new WrapperMessageQueue<>(new ConcurrentLinkedQueue<TransactionCommand>());
    }

    @Bean
    @Qualifier("delayQueue")
    public MessageQueue<TransactionCommand> delay() {
        return new BlockingMessageQueue<>(new DelayQueue());
    }

    @Bean
    @Qualifier("heap")
    public MessageQueue<TransactionCommand> heap() {
        return new BlockingMessageQueue<>(new DelayQueue());
    }

    @Bean
    public TimeUtils timeUtils(){
        return new TimeUtils();
    }
}
