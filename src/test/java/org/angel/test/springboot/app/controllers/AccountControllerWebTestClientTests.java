package org.angel.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.TransferDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
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

        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transferDto)
                // Ok, I've set everything up, now fire the request and give me the response.
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody() // Byte default
                .consumeWith(res -> {
                    try {
                        JsonNode jsonNode = mapper.readTree(res.getResponseBody());
                        assertEquals("Transfer successful", jsonNode.get("message").asText());
                        assertEquals(1L, jsonNode.path("transfer").path("accountSourceId").asLong());
                        assertEquals("100", jsonNode.path("transfer").path("amount").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.message").isEqualTo("Transfer successful")
                .jsonPath("$.status").isEqualTo("OK")
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.transfer.accountSourceId").isEqualTo(transferDto.getAccountSourceId())
                .json(mapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void testDetailAccount() throws JsonProcessingException {

        Account account = new Account(1L, "Angel", new BigDecimal("900"));

        client.get().uri("/api/accounts/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").isEqualTo("Angel")
                .jsonPath("$.balance").isEqualTo(900)
                .json(mapper.writeValueAsString(account));
    }
    @Test
    @Order(3)
    void testDetailAccount2() {
        client.get().uri("/api/accounts/2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    Account account = response.getResponseBody();
                    assert account != null;
                    assertEquals("Estrella", account.getPerson());
                    assertEquals("2100.00", account.getBalance().toPlainString());
                });
        //.jsonPath("$.person").isEqualTo("Angel")
        //.jsonPath("$.balance").isEqualTo(1000);
    }
}