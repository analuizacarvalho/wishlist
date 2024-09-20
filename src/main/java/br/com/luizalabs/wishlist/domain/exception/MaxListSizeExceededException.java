package br.com.luizalabs.wishlist.domain.exception;

public class MaxListSizeExceededException extends RuntimeException {
    public MaxListSizeExceededException(String message) {
        super(message);
    }
}
