package com.n26.web.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.n26.web.validation.Fresh;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
//Hack to avoid creating an empty constructor, required=true does not work at the field level so we have to use our constructor with required=true on it
@RequiredArgsConstructor
@ToString
@Fresh
public class TransactionDTO {

    @Getter
    @EqualsAndHashCode.Exclude
    BigDecimal amount;

    @Getter
    @NotNull
    final LocalDateTime timestamp;

    @JsonCreator
    public TransactionDTO(@JsonProperty(value = "amount", required = true) BigDecimal amount, @JsonProperty(value = "timestamp", required = true) LocalDateTime timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }


}
