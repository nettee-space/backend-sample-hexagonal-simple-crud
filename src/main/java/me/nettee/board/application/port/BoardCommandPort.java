package me.nettee.board.application.port;

import me.nettee.board.application.domain.Board;

public interface BoardCommandPort {

    Board create(Board board);

    Board update(Board board);

    void delete(Long id);

}
