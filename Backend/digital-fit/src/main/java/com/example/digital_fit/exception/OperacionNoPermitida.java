package com.example.digital_fit.exception;

public class OperacionNoPermitida extends RuntimeException {
    public OperacionNoPermitida(String mensaje) {
        super(mensaje);
    }

}
