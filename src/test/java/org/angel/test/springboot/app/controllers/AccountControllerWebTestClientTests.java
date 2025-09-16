package org.angel.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.angel.test.springboot.app.models.TransferDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebTestClientTests {

    @Autowired
    private WebTestClient client;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testTransfer() throws JsonProcessingException {

        TransferDto transferDto = new TransferDto();
        transferDto.setAccountSourceId(1L);
        transferDto.setAccountDestinationId(2L);
        transferDto.setAmount(new BigDecimal("100"));
        transferDto.setBankId(1L);

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", OK);
        response.put("message", "Transfer successful");
        response.put("transfer", transferDto);

        client.post().uri("http://localhost:8080/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transferDto)
                // Ok, I've set everything up, now fire the request and give me the response.
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Transfer successful")
                .jsonPath("$.status").isEqualTo("OK")
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.transfer.accountSourceId").isEqualTo(transferDto.getAccountSourceId())
                .json(mapper.writeValueAsString(response));
    }
}