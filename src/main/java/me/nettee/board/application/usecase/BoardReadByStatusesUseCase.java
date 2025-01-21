package me.nettee.board.application.usecase;

import java.util.Set;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReadByStatusesUseCase {
    Page<Board> findByStatuses(Pageable pageable, Set<BoardStatus> statuses);
}

