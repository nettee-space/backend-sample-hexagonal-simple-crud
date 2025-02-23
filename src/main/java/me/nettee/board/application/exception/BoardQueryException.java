package me.nettee.board.application.exception;

import java.util.Map;
import me.nettee.common.exeption.CustomException;

public class BoardQueryException extends CustomException {
    public BoardQueryException(BoardQueryErrorCode errorCode) {
        super(errorCode);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Map<String, Object> payload, Throwable cause) {
        super(errorCode, payload, cause);
    }

    public BoardQueryException(BoardQueryErrorCode errorCode, Map<String, Object> payload) {
        super(errorCode, payload);
    }
}
