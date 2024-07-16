package com.example.testeaxonjava8.commonapi.commands;

import lombok.Getter;

import java.math.BigDecimal;

public class DebitAccountCommand extends BaseCommand<String>{
    @Getter
    private double amount;
    @Getter
    private String currenty;
    public DebitAccountCommand(String id,double amount,String currenty) {
        super(id);
        this.amount=amount;
        this.currenty=currenty;
    }
}
