package com.n26.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.helper.TimeHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TimeHelper timeHelper;

    @Mock
    private TransactionService transactionService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .build();
    }

    @Test
    public void given_a_valid_request_when_posting_it_then_is_created_returned() throws Exception {
        TransactionRequest validTransactionRequest = new TransactionRequest("44.0", "2018-07-17T09:59:51.312Z");
        Transaction validTransaction = validTransactionRequest.validateAndMap(timeHelper).getValue1();

        when(timeHelper.getCurrentMilliseconds()).thenReturn(1531821593000L);
        when(transactionService.add(validTransaction)).thenReturn(true);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validTransactionRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void given_a_invalid_field_request_when_posting_it_then_is_created_returned() throws Exception {
        TransactionRequest validTransactionRequest = new TransactionRequest("44.0", "2018-07-17T09:59:51.3121Z");

        when(timeHelper.getCurrentMilliseconds()).thenReturn(1531814392L);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validTransactionRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void given_a_old_request_when_posting_it_then_is_created_returned() throws Exception {
        TransactionRequest validTransactionRequest = new TransactionRequest("44.0", "2018-06-17T09:59:51.312Z");

        when(timeHelper.getCurrentMilliseconds()).thenReturn(1531814392000L);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validTransactionRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void given_a_invalid_json_request_when_posting_it_then_is_created_returned() throws Exception {
        when(timeHelper.getCurrentMilliseconds()).thenReturn(1531814392L);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("Test")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_deleting_transactions_then_is_no_content_returned() throws Exception {
        mockMvc.perform(delete("/transactions"))
                .andExpect(status().isNoContent());
    }
}
