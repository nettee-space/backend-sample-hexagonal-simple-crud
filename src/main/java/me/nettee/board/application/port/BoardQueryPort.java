package me.nettee.board.application.port;


import java.util.Optional;
import java.util.Set;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardReadDetailModel;
import me.nettee.board.application.model.BoardReadSummaryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryPort {

    Optional<BoardReadDetailModel> findById(Long id);
    Page<BoardReadDetailModel> findAll(Pageable pageable);
    Page<BoardReadSummaryModel> findByStatusesList(Pageable pageable, Set<BoardStatus> statuses);

}
