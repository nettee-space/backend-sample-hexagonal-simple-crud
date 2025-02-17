package me.nettee.board.application.exception;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import me.nettee.common.exeption.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BoardCommandErrorCode implements ErrorCode {
    // ⬇️ 인증 쪽 에러코드 제공받아 쓸 것이냐
    // UNAUTHORIZED("로그인이 필요한 기능입니다.", HttpStatus.UNAUTHORIZED),
    // ⬇️ 조회 쪽 에러코드 제공받아 쓸 것이냐, 아니면 Master DB 핸들 시 사용할 용도로 여기 따로 둘 거냐.
    BOARD_NOT_FOUND("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BOARD_GONE("더 이상 존재하지 않는 게시물입니다.", HttpStatus.GONE),
    BOARD_FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN),
    DEFAULT("게시물 조작 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    BOARD_ALREADY_EXIST("게시물이 이미 존재합니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;

    BoardCommandErrorCode(String message, HttpStatus httpStatus) {
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
    public BoardCommandException exception() {
        return new BoardCommandException(this);
    }

    @Override
    public BoardCommandException exception(Throwable cause) {
        return new BoardCommandException(this, cause);
    }

    @Override
    public RuntimeException exception(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
        return new BoardCommandException(this);
    }

    @Override
    public RuntimeException exception(Runnable runnable, Throwable cause) {
        if (runnable != null) {
            runnable.run();
        }
        return new BoardCommandException(this, cause);
    }

    @Override
    public RuntimeException exception(Supplier<Map<String, Object>> appendPayload) {
        Map<String, Object> payload = (appendPayload != null) ? appendPayload.get() : Collections.emptyMap();
        return new BoardCommandException(this, payload);
    }

    @Override
    public RuntimeException exception(Supplier<Map<String, Object>> appendPayload, Throwable cause) {
        Map<String, Object> payload = (appendPayload != null) ? appendPayload.get() : Collections.emptyMap();
        return new BoardCommandException(this, payload, cause);
    }
}
