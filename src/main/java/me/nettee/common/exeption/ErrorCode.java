package me.nettee.common.exeption;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name(); // enum default method
    String message();
    HttpStatus httpStatus();
    RuntimeException exception();
    RuntimeException exception(Throwable cause);
}
