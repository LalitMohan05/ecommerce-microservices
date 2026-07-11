package com.lalit.order.exceptions;

public class OrderIdNotFoundException extends RuntimeException {
    public OrderIdNotFoundException(String message) {
        super(message);
    }
}
