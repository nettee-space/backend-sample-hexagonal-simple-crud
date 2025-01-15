package me.nettee.board.adapter.driving.web;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse;
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.usecase.BoardReadByStatusesUseCase;
import me.nettee.board.application.usecase.BoardReadUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardQueryController {
    private final BoardReadUseCase boardReadUseCase;
    private final BoardDtoMapper boardDtoMapper;
    private final BoardReadByStatusesUseCase boardReadByStatusesUseCase;

    @GetMapping("/{boardId}")
    public BoardDetailResponse getBoard(@PathVariable("boardId") long boardId) {
        var board = boardReadUseCase.getBoard(boardId);
        return boardDtoMapper.toDtoDetail(board);
    }

    @GetMapping("/statuses")
    public Page<BoardSummaryResponse> getBoardsByStatuses(Pageable pageable,@RequestParam List<BoardStatus> statuses) {
        var boardByList = boardReadByStatusesUseCase.findByStatuses(pageable, statuses).stream()
                .map(boardDtoMapper::toDtoSummary)
                .toList();
        return  new PageImpl<>(boardByList, pageable, boardByList.size());
    }
}
