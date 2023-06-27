package io.endeavour.stocks;

public class StocksException extends RuntimeException {
    public StocksException(String message) {
        super(message);
    }
    public StocksException(String message, Throwable cause) {
        super(message, cause);
    }
}
