package com.example.testeaxonjava8.query.services;

import com.example.testeaxonjava8.commonapi.enums.OperationType;
import com.example.testeaxonjava8.commonapi.events.AccountActivatedEvent;
import com.example.testeaxonjava8.commonapi.events.AccountCreatedEvent;
import com.example.testeaxonjava8.commonapi.events.AccountCreditedEvent;
import com.example.testeaxonjava8.commonapi.events.AccountDebitEvent;
import com.example.testeaxonjava8.commonapi.queries.GetAccountById;
import com.example.testeaxonjava8.commonapi.queries.GetAllAccountQuery;
import com.example.testeaxonjava8.query.entities.Account;
import com.example.testeaxonjava8.query.entities.Operation;
import com.example.testeaxonjava8.query.repositories.AccountRepository;
import com.example.testeaxonjava8.query.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceHandler {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent accountCreatedEvent){
        log.info("******************************");
        log.info("AccountCreatedEvent event");
        Account account=new Account();
        account.setId(accountCreatedEvent.getId());
        account.setBalance(accountCreatedEvent.getInitialBalance());
        account.setCurrency(accountCreatedEvent.getCurrency());
        account.setStatus(accountCreatedEvent.getStatus());
        accountRepository.save(account);

    }

    @EventHandler
    public void on(AccountActivatedEvent accountActivatedEvent){
        log.info("******************************");
        log.info("AccountActivatedEvent event");
        Account account=accountRepository.findById(accountActivatedEvent.getId()).get();
        account.setStatus(accountActivatedEvent.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent accountCreditedEvent){
        log.info("******************************");
        log.info("AccountCreditedEvent event");
        Account account=accountRepository.findById(accountCreditedEvent.getId()).get();

        Operation operation=new Operation();
        operation.setDate(new Date());
        operation.setAmount(accountCreditedEvent.getAmount());
        operation.setAccount(account);
        operation.setType(OperationType.CREDIT);
        operationRepository.save(operation);

        double balance=account.getBalance()+accountCreditedEvent.getAmount();
        account.setBalance(balance);
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountDebitEvent accountDebitEvent){
        log.info("******************************");
        log.info("AccountDebitEvent event");
        Account account=accountRepository.findById(accountDebitEvent.getId()).get();

        Operation operation=new Operation();
        operation.setDate(new Date());
        operation.setAmount(accountDebitEvent.getAmount());
        operation.setAccount(account);
        operation.setType(OperationType.DEBIT);
        operationRepository.save(operation);

        double balance=account.getBalance()-accountDebitEvent.getAmount();
        account.setBalance(balance);
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountQuery query){
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountById query){
        return accountRepository.findById(query.getId()).get();
    }
}
