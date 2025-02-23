package me.nettee.board.application.exception;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import me.nettee.common.exeption.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BoardQueryErrorCode implements ErrorCode {
    BOARD_NOT_FOUND("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BOARD_GONE("더 이상 존재하지 않는 게시물입니다.", HttpStatus.GONE),
    BOARD_FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN),
    DEFAULT("게시물 조작 오류", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    BoardQueryErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public BoardQueryException exception() {
        return new BoardQueryException(this);
    }

    @Override
    public BoardQueryException exception(Throwable cause) {
        return new BoardQueryException(this, cause);
    }

    @Override
    public RuntimeException exception(Runnable runnable) {
        return new BoardQueryException(this, runnable);
    }

    @Override
    public RuntimeException exception(Runnable runnable, Throwable cause) {
        return new BoardQueryException(this, runnable, cause);
    }

    @Override
    public RuntimeException exception(Supplier<Map<String, Object>> payload) {
        return new BoardQueryException(this, payload);
    }

    @Override
    public RuntimeException exception(Supplier<Map<String, Object>> payload, Throwable cause) {
        return new BoardQueryException(this, payload, cause);
    }
}
