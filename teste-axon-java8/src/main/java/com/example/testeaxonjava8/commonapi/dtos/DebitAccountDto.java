package com.example.testeaxonjava8.commonapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitAccountDto {
    private String accountId;
    private double amount;
    private String currency;
}
