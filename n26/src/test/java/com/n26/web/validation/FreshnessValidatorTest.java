package com.n26.web.validation;

import com.n26.configuration.TransactionProperties;
import com.n26.exceptions.TransactionTooEarlyException;
import com.n26.exceptions.TransactionTooLateException;
import com.n26.utils.TimeUtils;
import com.n26.web.DTO.TransactionDTO;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Freshness Validator")
public class FreshnessValidatorTest {

    @Mock
    private TransactionProperties properties;

    @InjectMocks
    private FreshnessValidator freshnessValidator;

    @Mock
    private ConstraintValidatorContext validatorContext;

    @Spy
    private TimeUtils timeUtils;

    private LocalDateTime now;

    @BeforeEach
    public void init() {
        now = LocalDateTime.now();
        when(timeUtils.now()).thenReturn(now);
    }


    @Nested
    @DisplayName("without transaction TTL and without drift")
    class WithoutTTLAndDrift {
        @Test
        @DisplayName("Transaction with timestamp matching exactly current time should pass validation")
        void testTransactionJustCreated() {
            when(timeUtils.durationToNow(now)).thenReturn(0L);
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now);
            freshnessValidator.isValid(transaction, validatorContext);
            assertTrue(true);
        }

        @Test
        @DisplayName("Transaction created in the future without a ttl should fail")
        void testTransactionCreatedInTheFuture() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(1, MILLIS));
            assertThrows(TransactionTooEarlyException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created in the future should have failed validation");
        }

        @Test
        @DisplayName("Transaction created in the past without a ttl should fail")
        void testTransactionCreatedInThePast() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-1, MILLIS));
            assertThrows(TransactionTooLateException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created in the past should have failed validation");
        }
    }

    @Nested
    @DisplayName("with TTL")
    class WithTTL {

        long ttl = 60;

        @BeforeEach
        public void init() {
            when(properties.getEventTtlMilliSeconds()).thenReturn(ttl);
        }

        @Test
        @DisplayName("Transaction with timestamp matching ttl should pass validation")
        void testTransactionInThepastAtLimitTTL() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-ttl, MILLIS));
            freshnessValidator.isValid(transaction, validatorContext);
            assertTrue(true);
        }

        @Test
        @DisplayName("Transaction with timestamp outside ttl should fail validation")
        void testTransactionInThepastOutsideTTL() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-ttl - 1, MILLIS));
            assertThrows(TransactionTooLateException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created outside ttl should have failed validation");

        }

        @Test
        @DisplayName("Transaction with timestamp in ttl in the future fails")
        void testTransactionInTheFutureTTL() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(ttl, MILLIS));
            assertThrows(TransactionTooEarlyException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction ttl has not impact on the future");
        }

        @Nested
        @DisplayName("and clock drift")
        class WithDrift {

            long drift = 2;

            @BeforeEach
            public void init() {
                when(properties.getTolerableClockDriftMillis()).thenReturn(drift);
            }

            @Test
            @DisplayName("Transaction with timestamp in ttl and drift in the past should pass validation")
            void testTransactionInThepastWithDrift() {
                TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-(drift+ttl), MILLIS));
                freshnessValidator.isValid(transaction, validatorContext);
                assertTrue(true);
            }

            @Test
            @DisplayName("Transaction with timestamp in ttl and drift in the future should fail validation")
            void testTransactionInTheFuturetWithDrift() {
                TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(drift+ttl, MILLIS));
                assertThrows(TransactionTooEarlyException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction ttl and drift in the future should fail");
            }

            @Test
            @DisplayName("Transaction with timestamp outside ttl and drift in the past should fail validation")
            void testTransactionInThepastOutsideTTL() {
                TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-(drift +ttl +1), MILLIS));
                assertThrows(TransactionTooLateException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created outside drift and ttl in the past should have failed validation");

            }

            @Test
            @DisplayName("Transaction with timestamp outside ttl and drift in the future should pass validation")
            void testTransactionInTheFutureTTL() {
                TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(drift+ttl + 1, MILLIS));
                assertThrows(TransactionTooEarlyException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created beyond drift and ttl in the future should fail");
            }

        }
    }

    @Nested
    @DisplayName("with clock drift")
    class WithDrift {

        long drift = 2;

        @BeforeEach
        public void init() {
            when(properties.getTolerableClockDriftMillis()).thenReturn(drift);
        }

        @Test
        @DisplayName("Transaction with timestamp in drift in the past should pass validation")
        void testTransactionInThepastWithDrift() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-drift, MILLIS));
            freshnessValidator.isValid(transaction, validatorContext);
            assertTrue(true);
        }

        @Test
        @DisplayName("Transaction with timestamp in drift in the future should pass validation")
        void testTransactionInTheFuturetWithDrift() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(drift, MILLIS));
            freshnessValidator.isValid(transaction, validatorContext);
            assertTrue(true);
        }

        @Test
        @DisplayName("Transaction with timestamp outside drift in the past should fail validation")
        void testTransactionInThepastOutsideTTL() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(-drift - 1, MILLIS));
            assertThrows(TransactionTooLateException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created outside drift should have failed validation");

        }

        @Test
        @DisplayName("Transaction with timestamp outside drift in the future should fail validation")
        void testTransactionInTheFutureTTL() {
            TransactionDTO transaction = new TransactionDTO(BigDecimal.ZERO, now.plus(drift + 1, MILLIS));
            assertThrows(TransactionTooEarlyException.class, () -> freshnessValidator.isValid(transaction, validatorContext), "transaction created beyond drift in the future should fail");
        }

    }

}
