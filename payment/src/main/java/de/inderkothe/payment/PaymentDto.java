package de.inderkothe.payment;

public record PaymentDto (
    Double amount,
    String src,
    String dest
){}
