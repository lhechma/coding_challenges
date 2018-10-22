package com.n26.models.workers.consumers;

import com.n26.configuration.TransactionProperties;
import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.models.events.TransactionCommandFactory;
import com.n26.models.queues.MessageQueue;
import com.n26.models.transactions.Transaction;
import com.n26.utils.TimeUtils;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.AtLeast;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(MockitoExtension.class)
public class FifoMessagingOrchestratorTest {


    FifoMessagingOrchestrator orchestrator;
    @InjectMocks
    TransactionCommandFactory transactionCommandFactory;
    @Mock
    TransactionProperties transactionProperties;
    @Mock
    private com.n26.models.queues.MessageQueue<TransactionCommand> fifo;
    @Mock
    private MessageQueue<TransactionCommand> delay;
    @Mock
    private StatisticsCalculator calculator;
    @Captor
    ArgumentCaptor<Transaction> transactioCaptor;

    private ExecutorService executorService;

    @BeforeEach
    public void init() {
        executorService = Executors.newFixedThreadPool(1);
        orchestrator = new FifoMessagingOrchestrator(fifo,delay,calculator);
    }

    @Test
    @DisplayName("Purge Command workflow")
    public void testWorkflowForPurge() throws InterruptedException {
        Mockito.when(fifo.pull()).thenReturn(transactionCommandFactory.createPurgeCommand());
        executorService.submit(()-> orchestrator.start());
        Thread.sleep(100);
        Mockito.verify(delay, new AtLeast(1)).clear();
        Mockito.verify(calculator, new AtLeast(1)).update(transactioCaptor.capture());
        Transaction transaction = transactioCaptor.getValue();
        Assert.assertFalse(transaction.getAmount().isPresent());
    }

    @AfterEach
    public void stop(){
        executorService.shutdown();
    }

}
