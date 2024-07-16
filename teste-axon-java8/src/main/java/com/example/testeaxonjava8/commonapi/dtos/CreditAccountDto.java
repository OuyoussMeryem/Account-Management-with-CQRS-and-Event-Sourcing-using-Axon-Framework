package com.example.testeaxonjava8.commonapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditAccountDto {
    private String accountId;
    private double amount;
    private String currency;
}
