package me.nettee.board.application.port;


import me.nettee.board.application.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardQueryPort {

    Page<Board> findAll(Pageable pageable);

    Optional<Board> findById(Long id);

}
