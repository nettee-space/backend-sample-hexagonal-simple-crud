package me.nettee.board.application.exception;

import me.nettee.common.exeption.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BoardErrorCode implements ErrorCode {
    // ⬇️ 인증 쪽 에러코드 제공받아 쓸 것이냐
    // UNAUTHORIZED("로그인이 필요한 기능입니다.", HttpStatus.UNAUTHORIZED),
    // ⬇️ 조회 쪽 에러코드 제공받아 쓸 것이냐, 아니면 Master DB 핸들 시 사용할 용도로 여기 따로 둘 거냐.
    BOARD_NOT_FOUND("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BOARD_GONE("더 이상 존재하지 않는 게시물입니다.", HttpStatus.GONE),
    BOARD_FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN),
    DEFAULT("게시물 조작 오류", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    BoardErrorCode(String message, HttpStatus httpStatus) {
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
    public BoardException exception() {
        return new BoardException(this);
    }

    @Override
    public BoardException exception(Throwable cause) {
        return new BoardException(this, cause);
    }
}
