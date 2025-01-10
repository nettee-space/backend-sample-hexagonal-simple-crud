package me.nettee.board.application.port;

import java.util.List;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardQueryPort {

    Optional<Board> findById(Long id);

    Page<Board> findAll(Pageable pageable);

    Page<Board> findByStatusesList(Pageable pageable, List<BoardStatus> statuses);

}
