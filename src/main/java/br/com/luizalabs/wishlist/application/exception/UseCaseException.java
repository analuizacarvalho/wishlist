package br.com.luizalabs.wishlist.application.exception;

public class UseCaseException extends RuntimeException {
    public UseCaseException(String message) {
        super(message);
    }
}
