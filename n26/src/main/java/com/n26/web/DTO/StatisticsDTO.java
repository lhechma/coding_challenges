package com.n26.web.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.web.configuration.serialization.AmountSerializer;
import lombok.Getter;

import java.math.BigDecimal;

public class StatisticsDTO {
    @Getter
    @JsonSerialize(using = AmountSerializer.class)
    private  BigDecimal sum;
    @Getter
    @JsonSerialize(using = AmountSerializer.class)
    private  BigDecimal avg;
    @Getter
    @JsonSerialize(using = AmountSerializer.class)
    private  BigDecimal max;
    @Getter
    @JsonSerialize(using = AmountSerializer.class)
    private  BigDecimal min;
    @Getter
    private  long count;

    public StatisticsDTO(BigDecimal sum,  BigDecimal min, BigDecimal max,BigDecimal average, long count ){
        this.sum = sum;
        this.count = count;
        this.max = max;
        this.min = min;
        this.avg = average;
    }
}
