package com.n26.web.controllers;

import com.n26.models.Statistics;
import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommandFactory;
import com.n26.models.events.TransactionCommand;
import com.n26.models.queues.MessageQueue;
import com.n26.web.DTO.StatisticsDTO;
import com.n26.web.DTO.TransactionDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("/transactions")
public class TransactionController {

    @Setter(onMethod_={@Autowired,@Qualifier("inboundMessageQueue")})
    private MessageQueue<TransactionCommand> messageQueue;
    @Setter(onMethod_={@Autowired})
    private StatisticsCalculator calculator;
    @Setter(onMethod_={@Autowired})
    private TransactionCommandFactory eventFactory;


    @GetMapping
    public StatisticsDTO getStatistic(){
        Statistics statistics = calculator.getStatistics();
        return new StatisticsDTO(statistics.getSum(),statistics.getMin(),statistics.getMax(),statistics.getAverage(),statistics.getCount());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submitTransaction(@Valid @RequestBody TransactionDTO transaction){
        messageQueue.push(eventFactory.createDepositCommand(transaction.getAmount(),transaction.getTimestamp()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransactions(){
        messageQueue.push(eventFactory.createPurgeCommand());
    }

}
