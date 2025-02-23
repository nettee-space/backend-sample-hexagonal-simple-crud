package me.nettee.board.adapter.driving.web;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse;
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardQueryModels.BoardSummary;
import me.nettee.board.application.usecase.BoardReadByStatusesUseCase;
import me.nettee.board.application.usecase.BoardReadUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardQueryApi {

    private final BoardReadUseCase boardReadUseCase;
    private final BoardReadByStatusesUseCase boardReadByStatusesUseCase;

    private final BoardDtoMapper mapper;

    @GetMapping("/{boardId}")
    public BoardDetailResponse getBoard(@PathVariable("boardId") long boardId) {
        var board = boardReadUseCase.getBoard(boardId);

        return mapper.toDtoDetail(board);
    }

    @GetMapping
    public Page<BoardSummary> getBoardsByStatuses(@RequestParam(defaultValue = "ACTIVE,SUSPENDED") Set<BoardStatus> statuses, Pageable pageable) {
        return boardReadByStatusesUseCase.findByStatuses(statuses, pageable);
    }
}
