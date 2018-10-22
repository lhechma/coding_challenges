package com.n26.web.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.configuration.TransactionProperties;
import com.n26.web.DTO.TransactionDTO;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Test TransactionDTO Business Rules Validation")
@ExtendWith(MockitoExtension.class)
public class TransactionValidationBusinessRulesIT {


    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    long NANOS_PER_MS = 1000000;
    @Autowired
    private GenericWebApplicationContext webApplicationContext;
    @MockBean
    private TransactionProperties properties;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.when(properties.getEventTtlMilliSeconds()).thenReturn(60000L);
    }

    @Test
    @DisplayName("transaction payload with a positive numeric amount and current timestamp passes")
    public void passWithApositiveAMountAndConfiguredFormatForTimestamp() throws Exception {
        TransactionDTO transaction = new TransactionDTO(BigDecimal.valueOf(10), zuluTime());
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writer().writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }

    private LocalDateTime zuluTime() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
    }

    @Nested
    @DisplayName("with timestamp freshness")
    class timeStampFreshess {

        @BeforeEach
        public void before() {
            Mockito.when(properties.getTolerableClockDriftMillis()).
                    thenReturn(0L);
        }

        @Test
        @DisplayName("transaction older than configured ttl is not valid")
        void staleTransactionRejected() throws Exception {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.valueOf(10), zuluTime().plus(-properties.getEventTtlMilliSeconds()-1,ChronoUnit.MILLIS ));
            mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writer().writeValueAsString(transaction)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("transaction fresher than current time is not valid")
        void futureTransactionRejected() throws Exception {
            LocalDateTime now = zuluTime();
            TransactionDTO transaction = new TransactionDTO(BigDecimal.valueOf(10), now.plus(3, ChronoUnit.MILLIS));
            mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writer().writeValueAsString(transaction)))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    @DisplayName("with clock drift")
    class WithClockDrift {


        @BeforeEach
        public void before() {
            Mockito.when(properties.getTolerableClockDriftMillis()).
                    thenReturn(2000L);
        }

        @Test
        @DisplayName("transaction older than configured ttl is valid if within drift")
        void staleTransactionAcceptedIfWithinDrift() throws Exception {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.valueOf(10), zuluTime().plus(-properties.getEventTtlMilliSeconds() - 100, ChronoUnit.MILLIS));
            mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writer().writeValueAsString(transaction)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("transaction fresher than current time is valid if within drift")
        void freshTransactionAcceptedIfWithinDrift() throws Exception {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.valueOf(10), zuluTime().plus(1000,ChronoUnit.MILLIS));
            mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writer().writeValueAsString(transaction)))
                    .andExpect(status().isCreated());
        }


    }
}
