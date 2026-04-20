package com.dbo.OrderManagement.exceptions;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String message){
        super(message);
    }

}
