package org.angel.test.springboot.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.angel.test.springboot.app.Data;
import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.TransferDto;
import org.angel.test.springboot.app.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private AccountService accountService;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    /**
     * Unit test for the GET endpoint `/api/accounts/{id}` using MockMvc.
     *
     * Purpose:
     * This test ensures that the endpoint correctly retrieves and returns
     * the details of an account when a valid ID is provided.
     *
     * Test flow:
     * 1. **Given**
     *    - The service layer (`accountService.findAccountById(1L)`) is mocked
     *      to return a predefined account object (`Account 001` with person "Angel"
     *      and balance 1000).
     *
     * 2. **When**
     *    - A GET request is performed to `/api/accounts/1`
     *      with `Content-Type` set to `application/json`.
     *
     * 3. **Then**
     *    - The response should return HTTP 200 (OK).
     *    - The response content type should be JSON.
     *    - The `person` field in the JSON response should be `"Angel"`.
     *    - The `balance` field in the JSON response should be `1000`.
     *    - The service method `findAccountById(1L)` must be invoked exactly once.
     */
    @Test
    void testDetail() throws Exception {
        // Given
        when(accountService.findAccountById(1L)).thenReturn(Data.createAccount001().orElseThrow());
        // When
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
        // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Angel"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(accountService).findAccountById(1L);
    }

    /**
     * Test for the POST /api/accounts/transfer endpoint.
     *
     * Scenario:
     *  - A TransferDto object is sent as JSON in the request body.
     *  - The controller should process the transfer and return:
     *      - HTTP Status 200 (OK)
     *      - Content-Type application/json
     *      - A JSON response containing:
     *          - the current date,
     *          - a success message,
     *          - and the same transfer data sent in the request.
     */
    @Test
    void testTransfer() throws Exception {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountSourceId(1L);
        transferDto.setAccountDestinationId(2L);
        transferDto.setAmount(new BigDecimal("100"));
        transferDto.setBankId(1L);

        // ------------
        System.out.println(mapper.writeValueAsString(transferDto));
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", OK);
        response.put("message", "Transfer successful");
        response.put("transfer", transferDto);

        System.out.println(mapper.writeValueAsString(response));
        // ------------

        mvc.perform(MockMvcRequestBuilders.post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transferDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Transfer successful"))
                .andExpect(jsonPath("$.transfer.accountSourceId").value(1L))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void testFindAllAccounts() throws Exception {
        List<Account> accounts = Arrays.asList(Data.createAccount001().orElseThrow(), Data.createAccount002().orElseThrow());
        when(accountService.findAll()).thenReturn(accounts);

        mvc.perform(MockMvcRequestBuilders.get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Angel"))
                .andExpect(jsonPath("$[1].person").value("Estrella"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(mapper.writeValueAsString(accounts)));

        verify(accountService).findAll();
    }

    /**
     * Test case for the POST /api/accounts endpoint.
     *
     * This test simulates creating a new account by sending a JSON payload.
     * The AccountService.save method is mocked to automatically assign an ID (3L)
     * to the account when it is "saved".
     *
     * Steps:
     * 1. Prepare an Account object with no ID, name "Antonio", and balance 3000.
     * 2. Mock the accountService.save method to set the ID to 3 when called.
     * 3. Perform a POST request to /api/accounts with the Account JSON as body.
     * 4. Validate the response:
     *    - HTTP status is 201 Created
     *    - Response content type is application/json
     *    - JSON contains the expected ID, person, and balance
     * 5. Verify that accountService.save was called exactly once.
     */
    @Test
    void testSaveAccount() throws Exception {
        Account account = new Account(null, "Antonio", new BigDecimal("3000"));
        when(accountService.save(any())).then(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(3L);
            return a;
        });

        mvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.person", is("Antonio")))
                .andExpect(jsonPath("$.balance", is(3000)));

        verify(accountService).save(any());
    }
}















