package me.nettee.board.application.exception;

import java.util.Map;
import java.util.function.Supplier;
import me.nettee.common.exeption.CustomException;

public class BoardQueryException extends CustomException {
    public BoardQueryException(BoardQueryErrorCode errorCode) {
        super(errorCode);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Runnable runnable) {
        super(errorCode, runnable);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Runnable runnable, Throwable cause) {
        super(errorCode, runnable, cause);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Supplier<Map<String, Object>> payload) {
        super(errorCode, payload);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Supplier<Map<String, Object>> payload, Throwable cause) {
        super(errorCode, payload, cause);
    }
}
