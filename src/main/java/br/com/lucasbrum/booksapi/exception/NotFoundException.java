package br.com.lucasbrum.booksapi.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
