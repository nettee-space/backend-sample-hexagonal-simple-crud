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

        exception.execute();

        var responseBody = ApiErrorResponse.builder()
                .status(errorCode.httpStatus().value())
                .code(errorCode.name())
                .message(exception.getMessage()) // same to errorCode.message
                .payload(exception.getPayload())
                .build();
        
        return ResponseEntity
                .status(errorCode.httpStatus())
                .body(responseBody);
    }
}
