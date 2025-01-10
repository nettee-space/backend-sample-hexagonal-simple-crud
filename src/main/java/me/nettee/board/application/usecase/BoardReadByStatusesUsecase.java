package me.nettee.board.application.usecase;

import java.util.List;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReadByStatusesUsecase {
    Page<Board> findByStatuses(Pageable pageable, List<BoardStatus> statuses);
}

