package me.nettee.board.application.port;

import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardQueryPort {
    Optional<Board> findBoardById(Long id);
    Page<Board> findBoards(int pageNumber, int size);
    Page<Board> findBoardsByStatus(BoardStatus status, int pageNumber, int size);
    Page<Board> findActiveAndSuspendedBoards(int pageNumber, int size);
}
