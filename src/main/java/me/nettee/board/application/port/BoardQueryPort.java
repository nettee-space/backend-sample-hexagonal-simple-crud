package me.nettee.board.application.port;

import java.util.Optional;
import java.util.Set;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryPort {

    Optional<Board> findById(Long id);

    Page<Board> findByStatusesList(Pageable pageable, Set<BoardStatus> statuses);

}
