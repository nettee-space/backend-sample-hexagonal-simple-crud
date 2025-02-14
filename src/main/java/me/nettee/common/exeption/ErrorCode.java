package me.nettee.common.exeption;

import java.util.Map;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name(); // enum default method
    String message();
    HttpStatus httpStatus();
    RuntimeException exception();
    RuntimeException exception(Throwable cause);

    // 예외 발생 시, 후속 작업 수행 가능
    RuntimeException exception(Runnable runnable);
    RuntimeException exception(Runnable runnable, Throwable cause);

    // 예외 발생 시, 추가적인 데이터 동적 제공 가능
    RuntimeException exception(Supplier<Map<String, Object>> appendPayload);
    RuntimeException exception(Supplier<Map<String, Object>> appendPayload, Throwable cause);
}
