package com.blibli.cart.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String msg) {
        super(msg);
    }
}
