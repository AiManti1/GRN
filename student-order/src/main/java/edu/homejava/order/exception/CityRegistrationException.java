package edu.homejava.order.exception;

// Опиание проблем при обращении к реестру ГРН.
public class CityRegistrationException extends Exception {

    private String code;

    public CityRegistrationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CityRegistrationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() { return code; }
}
