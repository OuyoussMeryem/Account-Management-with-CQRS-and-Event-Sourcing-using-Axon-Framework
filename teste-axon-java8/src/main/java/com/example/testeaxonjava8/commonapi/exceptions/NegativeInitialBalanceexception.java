package com.example.testeaxonjava8.commonapi.exceptions;

public class NegativeInitialBalanceexception extends RuntimeException {
    public NegativeInitialBalanceexception(String message) throws RuntimeException {
        super(message);
    }
}
