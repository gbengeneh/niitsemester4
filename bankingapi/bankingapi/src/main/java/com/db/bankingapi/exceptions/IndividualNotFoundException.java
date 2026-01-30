package com.db.bankingapi.exceptions;

public class IndividualNotFoundException extends RuntimeException{
    public IndividualNotFoundException(String message){
        super(message);
    }
}
