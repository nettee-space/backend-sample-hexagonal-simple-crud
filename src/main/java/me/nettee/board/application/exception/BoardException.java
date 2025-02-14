package me.nettee.board.application.exception;

import me.nettee.common.exeption.CustomException;
import me.nettee.common.exeption.ErrorCode;

public class BoardException extends CustomException {
    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
