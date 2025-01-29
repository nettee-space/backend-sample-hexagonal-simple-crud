package me.nettee.board.application.usecase;

import java.util.Set;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardReadSummaryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReadByStatusesUseCase {

    Page<BoardReadSummaryModel> findByStatuses(Set<BoardStatus> statuses, Pageable pageable);

}

