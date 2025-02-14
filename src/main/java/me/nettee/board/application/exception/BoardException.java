package me.nettee.board.application.exception;

import java.util.Map;
import me.nettee.common.exeption.CustomException;
import me.nettee.common.exeption.ErrorCode;

public class BoardException extends CustomException {
    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }


    public BoardException(BoardErrorCode boardErrorCode, Map<String, Object> payload, Throwable cause) {
        super(boardErrorCode, payload, cause);
    }

    public BoardException(BoardErrorCode errorCode, Map<String, Object> payload) {
        super(errorCode, payload);
    }
}
