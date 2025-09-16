package org.angel.test.springboot.app.controllers;

import org.angel.test.springboot.app.models.*;
import org.angel.test.springboot.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Account detail(@PathVariable(name="id") Long id) {
        return accountService.findAccountById(id);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferDto transferDto) {
        accountService.transfer(
                transferDto.getAccountSourceId(),
                transferDto.getAccountDestinationId(),
                transferDto.getAmount(),
                transferDto.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", OK);
        response.put("message", "Transfer successful");
        response.put("transfer", transferDto);

        return ResponseEntity.ok(response);
    }
}
