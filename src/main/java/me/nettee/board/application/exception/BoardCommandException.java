package me.nettee.board.application.exception;

import me.nettee.common.exeption.CustomException;
import me.nettee.common.exeption.ErrorCode;

public class BoardCommandException extends CustomException {
    // (intellij) Ctrl + O
    // (eclipse or sts) Alt Shift S => generate constructors

    public BoardCommandException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardCommandException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
