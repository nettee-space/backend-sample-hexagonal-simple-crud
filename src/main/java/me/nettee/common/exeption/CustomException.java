package me.nettee.common.exeption;

import java.util.Collections;
import java.util.Map;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> payload;

    public CustomException() {
        this.errorCode = null;
        this.payload = Collections.emptyMap();
    }

    public CustomException(ErrorCode errorCode) {
        // ErrorCode의 기본 메시지를 RuntimeException에 전달하여 예외 메시지를 설정
        super(errorCode.message());
        this.errorCode = errorCode;
        this.payload = Collections.emptyMap();
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.message(), cause);
        this.errorCode = errorCode;
        this.payload = Collections.emptyMap();
    }

    public CustomException(ErrorCode errorCode, Map<String, Object> payload) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.payload = payload;
    }

    public CustomException(ErrorCode errorCode, Map<String, Object> payload, Throwable cause) {
        super(errorCode.message(), cause);
        this.errorCode = errorCode;
        this.payload = payload;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
