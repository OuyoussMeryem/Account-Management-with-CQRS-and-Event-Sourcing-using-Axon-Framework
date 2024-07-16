package com.example.testeaxonjava8.commands.controllers;

import com.example.testeaxonjava8.commonapi.commands.CreateAccountCommand;
import com.example.testeaxonjava8.commonapi.commands.CreditAccountCommand;
import com.example.testeaxonjava8.commonapi.commands.DebitAccountCommand;
import com.example.testeaxonjava8.commonapi.dtos.CreateAccountRequestDto;
import com.example.testeaxonjava8.commonapi.dtos.CreditAccountDto;
import com.example.testeaxonjava8.commonapi.dtos.DebitAccountDto;
import com.example.testeaxonjava8.commonapi.events.AccountCreditedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


@RestController
@RequestMapping(path="/commands/account")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;
    @PostMapping(path="/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDto request){
        String uuid = UUID.randomUUID().toString();
        System.out.println("Generated UUID: " + uuid);
        CompletableFuture<String> responseCommand = commandGateway.send(new CreateAccountCommand(
                uuid,
                request.getInitialBalance(),
                request.getCurrency()
        ));
        return responseCommand;
    }
    @GetMapping(path="/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }

    @PutMapping(path="/credite")
    public CompletableFuture<String> crediteAccount(@RequestBody CreditAccountDto creditAccountDto){
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                creditAccountDto.getAccountId(),
                creditAccountDto.getAmount(),
                creditAccountDto.getCurrency()
        ));
        return response;
    }

    @PutMapping(path="/debite")
    public CompletableFuture<String> debiteAccount(@RequestBody DebitAccountDto debitAccountDto){
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                debitAccountDto.getAccountId(),
                debitAccountDto.getAmount(),
                debitAccountDto.getCurrency()
        ));
        return response;
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
