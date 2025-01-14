package me.nettee.board.application.usecase;

import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardReadUseCase {
    Board getBoard (Long id);
    Page<Board> getBoards(Pageable pageable);
    Page<Board> getBoardsByStatus(Pageable pageable, List<BoardStatus> statuses);
}