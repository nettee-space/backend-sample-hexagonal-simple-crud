package me.nettee.board.application.usecase;

import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardQueryModels.BoardSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BoardReadByStatusesUseCase {

    Page<BoardSummary> findByStatuses(Set<BoardStatus> statuses, Pageable pageable);
}

