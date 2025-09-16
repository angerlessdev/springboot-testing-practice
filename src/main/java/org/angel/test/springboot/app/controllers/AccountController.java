package org.angel.test.springboot.app.controllers;

import org.angel.test.springboot.app.models.*;
import org.angel.test.springboot.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<Object> detail(@PathVariable(name="id") Long id) {
        Account account = null;
        try {
            account = accountService.findAccountById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Account> findAllAccounts() {
        return accountService.findAll();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Account createAccount(@RequestBody Account account) {
        return accountService.save(account);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable(name="id") Long id) {
        accountService.deleteById(id);
    }
}
