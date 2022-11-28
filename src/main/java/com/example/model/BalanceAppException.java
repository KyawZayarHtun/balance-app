package com.example.model;

import java.io.Serial;

public class BalanceAppException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = 1L;

    public BalanceAppException(String message) {
        super(message);
    }
}
