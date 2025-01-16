package me.nettee.common.exeption;

import me.nettee.common.exeption.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 내장된 AOP (다른 라이브러리 없이 사용 가능한 AOP)
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class) // 모든 커스텀 익셉션
    public ResponseEntity<?> handleCustomException(CustomException exception) {

        ErrorCode errorCode = exception.getErrorCode();

        var responseBody = ApiErrorResponse.builder()
                .status(errorCode.defaultHttpStatus().value())
                .code(errorCode.name())
                .message(exception.getMessage()) // same to errorCode.defaultMessage
                .build();

        return ResponseEntity
                .status(errorCode.defaultHttpStatus())
                .body(responseBody);
    }
}
