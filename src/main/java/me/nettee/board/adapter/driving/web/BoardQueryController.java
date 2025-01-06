package me.nettee.board.adapter.driving.web;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driving.web.dto.BoardDto.BoardSummaryDto;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSelectOneResponse;
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper;
import me.nettee.board.application.usecase.BoardReadUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/board")
@RequiredArgsConstructor
public class BoardQueryController {
    private final BoardReadUseCase boardReadUseCase;
    private final BoardDtoMapper boardDtoMapper;

    @GetMapping("/{boardId}")
    public BoardSelectOneResponse getBoard(@PathVariable("boardId") long boardId) {
        var board = boardReadUseCase.getBoard(boardId);
        return BoardSelectOneResponse.builder()
                .board(boardDtoMapper.toDtoDetailDto(board))
                .build();
    }

    @GetMapping
    public Page<BoardSummaryDto> getBoards(Pageable pageable) {
        var boardList = boardReadUseCase.findGeneralBy(pageable).stream()
                .map(boardDtoMapper::toDtoSummary)
                .toList();
        return new PageImpl<>(boardList, pageable, boardList.size());
    }
}
