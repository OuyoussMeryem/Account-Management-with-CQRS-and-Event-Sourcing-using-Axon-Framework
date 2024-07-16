package com.example.testeaxonjava8.query.controllers;

import com.example.testeaxonjava8.commonapi.queries.GetAccountById;
import com.example.testeaxonjava8.commonapi.queries.GetAllAccountQuery;
import com.example.testeaxonjava8.query.entities.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/query/accounts")
@AllArgsConstructor
@Slf4j
public class AccountQueryController {
     private QueryGateway queryGateway;

    @GetMapping(path="/getAllAccounts")
    public List<Account> AllAccounts(){
        return queryGateway.query(new GetAllAccountQuery(), ResponseTypes.multipleInstancesOf(Account.class)).join();
    }

    @GetMapping(path="/getAccountById/{id}")
    public Account getAccountById(@PathVariable String id){
        return queryGateway.query(new GetAccountById(id),ResponseTypes.instanceOf(Account.class)).join();
    }
}
