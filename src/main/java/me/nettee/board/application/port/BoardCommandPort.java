package me.nettee.board.application.port;

import java.util.Optional;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;

public interface BoardCommandPort {

    Optional<Board> findById(Long id);

    Board create(Board board);

    Board update(Board board);

    void updateStatus(Long id, BoardStatus status);
}
