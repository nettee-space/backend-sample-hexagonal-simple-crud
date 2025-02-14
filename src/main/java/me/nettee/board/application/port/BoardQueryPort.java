package me.nettee.board.application.port;

import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardQueryModel.BoardDetail;
import me.nettee.board.application.model.BoardQueryModel.BoardSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface BoardQueryPort {

    Optional<BoardDetail> findById(Long id);

    Page<BoardSummary> findAll(Pageable pageable);

    Page<BoardSummary> findByStatusesList(Pageable pageable, Set<BoardStatus> statuses);
}
