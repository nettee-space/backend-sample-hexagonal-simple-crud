package me.nettee.board.application.port;

import java.util.Optional;
import me.nettee.board.application.domain.Board;

public interface BoardCommandPort {

    Optional<Board> findById(Long id);

    Board create(Board board);

    Board update(Board board);

    void delete(Board id);
}
