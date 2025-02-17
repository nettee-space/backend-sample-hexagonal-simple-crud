package me.nettee.board.application.service;

import lombok.RequiredArgsConstructor;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.exception.BoardCommandException;
import me.nettee.board.application.port.BoardCommandPort;
import me.nettee.board.application.usecase.BoardCreateUseCase;
import me.nettee.board.application.usecase.BoardDeleteUseCase;
import me.nettee.board.application.usecase.BoardUpdateUseCase;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static me.nettee.board.application.exception.BoardCommandErrorCode.BOARD_NOT_FOUND;
import static me.nettee.board.application.exception.BoardCommandErrorCode.DEFAULT;

@Service
@RequiredArgsConstructor
public class BoardCommandService implements BoardCreateUseCase, BoardUpdateUseCase, BoardDeleteUseCase {

    private final BoardCommandPort boardCommandPort;

    public Board createBoard(Board board) {
        return boardCommandPort.create(board);
    }

    public Board updateBoard(Board board) {
        return boardCommandPort.update(board);
    }

    public void deleteBoard(Long id) {
        // softDelete 명을 가진 메서드가 생기면 변경
        // 현재 updateStatus로 REMOVE 상태로 변경
        boardCommandPort.updateStatus(id, BoardStatus.REMOVED);

        // Hard Delete 됬는지 확인
        Board board = boardCommandPort.findById(id).orElseThrow(
                () -> new BoardCommandException(BOARD_NOT_FOUND));

        // REMOVED 상태 확인 (null-safe)
        if (!Objects.equals(board.getStatus(), BoardStatus.REMOVED)) {
            throw new BoardCommandException(DEFAULT);
        }
    }
}
