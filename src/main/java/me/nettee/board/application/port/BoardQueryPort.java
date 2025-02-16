package me.nettee.board.application.port;

import java.util.Optional;
import java.util.Set;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardQueryModel.BoardDetail;
import me.nettee.board.application.model.BoardQueryModel.BoardSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryPort {

    Optional<BoardDetail> findById(Long id);

    Page<BoardSummary> findAll(Pageable pageable);

    Page<BoardSummary> findByStatusesList(Set<BoardStatus> statuses, Pageable pageable);
}
    