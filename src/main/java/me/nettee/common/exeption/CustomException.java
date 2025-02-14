package me.nettee.common.exeption;

public class CustomException extends RuntimeException {
    protected ErrorCode errorCode;

    public CustomException() {
        this.errorCode = null;
    }

    public CustomException(ErrorCode errorCode) {
        // ErrorCode의 기본 메시지를 RuntimeException에 전달하여 예외 메시지를 설정
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.message(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
