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
        Board board = boardCommandPort.findById(id).orElseThrow(
                () -> new BoardCommandException(BOARD_NOT_FOUND));

        // id와 board의 id가 다르면 예외 발생 - 굳이 필요할까? 고민
        // 필요하다면 에러코드 추가 필요
        if(!Objects.equals(board.getId(), id)) {
            throw new BoardCommandException(DEFAULT);
        }

        // softDelete 명을 가진 메서드가 생기면 변경
        // 현재 updateStatus로 REMOVE 상태로 변경
        boardCommandPort.updateStatus(id, BoardStatus.REMOVED);

        // 2025.02.16
        // 굳이 updateStatus 전에 board를 조회하는 이유가 있을까요?
        // 명목상 updateStatus 이후 hard delete가 된건 아닌지
        // soft Delete각 내가 의도한 BoardStatus.Removed에 맞게 됐는지?
        // 소스 흐름 상 그렇게 바꿔보면 좋을 것 같습니다.
    }
}
