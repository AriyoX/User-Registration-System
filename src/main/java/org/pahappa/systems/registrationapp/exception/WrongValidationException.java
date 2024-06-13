package org.pahappa.systems.registrationapp.exception;


public class WrongValidationException extends RuntimeException{
    public WrongValidationException(String message) {
        super(message);
    }
}
