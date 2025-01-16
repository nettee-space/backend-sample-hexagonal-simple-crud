package me.nettee.board.application.exception;

import me.nettee.common.exeption.CustomException;
import me.nettee.common.exeption.ErrorCode;

public class BoardQueryException extends CustomException {
    public BoardQueryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardQueryException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
