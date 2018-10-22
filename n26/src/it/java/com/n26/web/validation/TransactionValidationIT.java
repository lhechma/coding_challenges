package com.n26.web.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.models.StatisticsCalculator;
import com.n26.models.events.TransactionCommand;
import com.n26.web.controllers.ControllerExceptionAdvice;
import com.n26.web.controllers.TransactionController;
import com.n26.web.exception.ExceptionResolver;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionDTO Unmarhsalling Validation Test")
public class TransactionValidationIT {


    static MockMvc mockMvc;

    @InjectMocks
    TransactionController transactionController;

    @Autowired
    ObjectMapper mapper;

    @Mock
    Queue<TransactionCommand> messageQueue;
    @Mock
    StatisticsCalculator calculator;

    @Autowired
    Environment environment;


    @BeforeAll
    public static void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(TransactionController.class).setControllerAdvice(new ControllerExceptionAdvice(new ExceptionResolver())).build();
    }

    @BeforeEach
    public void before(){
    }

    @Test
    @DisplayName("transaction payload whithout an an amount field fails with status code 422")
    public void transactionMustContainAnAmountField() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload without a non numerical amount field fails with status code 422")
    public void transactionCannotContainANonNumericalAmountField() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "value");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload with a zero numeric amount field fails with status code 422")
    public void transactionPayloadMustContainANonZeroAmountField() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "0");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload with a negative numeric amount field fails with status code 422")
    public void transactionPayloadMustContainANonNegativeAmountField() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "-10");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload with a positive numeric amount field and whithout a timestamp fails with status code 422")
    public void failWithApositiveAMountAndWithoutTimestamp() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "0.0001");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload with a positive numeric amount and incompatible type for timestamp fails with status code 422")
    public void failWithApositiveAMountAndIncompatibleTimestamp() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "0.0001");
        map.put("timetamp", "0.0001");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload with a positive numeric amount and bad time format for timestamp type fails with status code 422")
    public void failWithApositiveAMountAndUnsupportedFormatForTimestamp() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "0.0001");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        map.put("timetamp", LocalDateTime.now().format(formatter));
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("transaction payload with only text input fails with statusCode 400")
    public void failWithATextInputAsPayloadForTimestamp() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        mockMvc.perform(post("/transactions").contentType(APPLICATION_JSON).content("SOME INPUT"))
                .andExpect(status().isBadRequest());
    }

}
