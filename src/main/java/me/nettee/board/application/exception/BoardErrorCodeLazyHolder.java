package me.nettee.board.application.exception;

import java.util.Map;
import java.util.function.Supplier;
import me.nettee.common.exeption.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * enum BoardErrorCode 문제점
 * - enum으로 선언하기 때문에, JVM이 실행될 때 모든 enum 상수를 메모리에 올려놓는다.
 * - 이는 상수가 많아질수록 메모리를 많이 차지하게 된다. 이를 해결하기 위해 Lazy-Holder 패턴을 사용한다.
 */
public class BoardErrorCodeLazyHolder implements ErrorCode {
    private final String name;
    private final String message;
    private final HttpStatus httpStatus;

    public BoardErrorCodeLazyHolder(String name, String message, HttpStatus httpStatus) {
        this.name = name;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    // static inner class로 선언하여 JVM이 클래스를 로딩할 때는 초기화를 하지 않고, getInstance() 메서드를 호출할 때 초기화를 진행한다.
    private static class LazyHolder {
        private static final BoardErrorCodeLazyHolder BOARD_NOT_FOUND = new BoardErrorCodeLazyHolder("BOARD_NOT_FOUND",
                "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        private static final BoardErrorCodeLazyHolder BOARD_NOT_AUTHORIZED = new BoardErrorCodeLazyHolder("BOARD_NOT_AUTHORIZED",
                "게시글에 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    public static BoardErrorCodeLazyHolder BOARD_NOT_FOUND() {
        return LazyHolder.BOARD_NOT_FOUND;
    }

    public static BoardErrorCodeLazyHolder BOARD_NOT_AUTHORIZED() {
        return LazyHolder.BOARD_NOT_AUTHORIZED;
    }

    @Override
    public String name() {
        return name;
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
    public RuntimeException exception() {
        return new BoardCommandException(this);
    }

    @Override
    public RuntimeException exception(Throwable cause) {
        return new BoardCommandException(this, cause);
    }

    @Override
    public RuntimeException exception(Runnable runnable) {
        return new BoardCommandException(this, runnable);
    }

    @Override
    public RuntimeException exception(Runnable runnable, Throwable cause) {
        return new BoardCommandException(this, runnable, cause);
    }

    @Override
    public RuntimeException exception(Supplier<Map<String, Object>> payload) {
        return new BoardCommandException(this, payload);
    }

    @Override
    public RuntimeException exception(Supplier<Map<String, Object>> payload, Throwable cause) {
        return new BoardCommandException(this, payload, cause);
    }
}
