package me.nettee.board.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.port.BoardQueryPort;
import me.nettee.board.application.usecase.BoardReadByStatusesUsecase;
import me.nettee.board.application.usecase.BoardReadUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardQueryService implements BoardReadUseCase, BoardReadByStatusesUsecase {

    private final BoardQueryPort boardQueryPort;

    @Override
    public Board getBoard(Long id) {
        return boardQueryPort.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    @Override
    public Page<Board> findGeneralBy(Pageable pageable) {
        return boardQueryPort.findAll(pageable);
    }

    @Override
    public Page<Board> findByStatuses(Pageable pageable, List<BoardStatus> statuses) {
        return boardQueryPort.findByStatusesList(pageable, statuses);
    }
}
