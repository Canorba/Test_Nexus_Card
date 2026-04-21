package com.bankinc.card_system.config;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String CARD_NOT_FOUND = "Card not found";
    public static final String CARD_BLOCKED = "Card is blocked";
    public static final String CARD_INACTIVE = "Card is not active";
    public static final String CARD_EXPIRED = "Card expired";
    public static final String INVALID_AMOUNT = "Invalid amount";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";

    public static final String TX_NOT_FOUND = "Transaction not found";
    public static final String TX_NOT_BELONG = "Transaction does not belong to this card";
    public static final String TX_ALREADY_REVERSED = "Transaction already reversed";
    public static final String TX_TOO_OLD = "Transaction older than 24 hours cannot be reversed";
}
