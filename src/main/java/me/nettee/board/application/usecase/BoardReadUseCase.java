package me.nettee.board.application.usecase;

import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReadUseCase {
    Board getBoard (Long id);
    Page<Board> getBoards(int pageNumber, int size);
    Page<Board> getBoardsByStatus(BoardStatus status, int pageNumber, int size);
    Page<Board> getActiveAndSuspendedBoards(int pageNumber, int size);
    Page<Board> findGeneralBy(Pageable pageable);
}