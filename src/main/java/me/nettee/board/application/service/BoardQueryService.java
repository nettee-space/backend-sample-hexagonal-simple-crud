package me.nettee.board.application.service;

import lombok.RequiredArgsConstructor;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.exception.BoardQueryErrorCode;
import me.nettee.board.application.port.BoardQueryPort;
import me.nettee.board.application.usecase.BoardReadUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardQueryService implements BoardReadUseCase {

    private final BoardQueryPort boardQueryPort;
    @Override
    @Transactional(readOnly = true)
    public Board getBoard(Long id) {
        return boardQueryPort.findBoardById(id)
                .orElseThrow(BoardQueryErrorCode.BOARD_NOT_FOUND::defaultException);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Board> getBoards(int pageNumber, int size) {
        return boardQueryPort.findBoards(pageNumber, size);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Board> getBoardsByStatus(BoardStatus status, int pageNumber, int size) {
        return boardQueryPort.findBoardsByStatus(status, pageNumber, size);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Board> getActiveAndSuspendedBoards(int pageNumber, int size) {
        return boardQueryPort.findActiveAndSuspendedBoards(pageNumber, size);
    }

    @Override
    public Page<Board> findGeneralBy(Pageable pageable) {
        return null;
    }
}
