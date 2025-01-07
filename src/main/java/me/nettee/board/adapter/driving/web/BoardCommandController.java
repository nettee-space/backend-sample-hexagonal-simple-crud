package me.nettee.board.adapter.driving.web;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCommandResponse;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand;
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.usecase.BoardCreateUseCase;
import me.nettee.board.application.usecase.BoardDeleteUseCase;
import me.nettee.board.application.usecase.BoardUpdateUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/board")
@RequiredArgsConstructor
public class BoardCommandController {

    private BoardCreateUseCase boardCreateUseCase;
    private BoardUpdateUseCase boardUpdateUseCase;
    private BoardDeleteUseCase boardDeleteUseCase;
    private BoardDtoMapper boardDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardCommandResponse createBoard(@RequestBody BoardCreateCommand boardCreateCommand) {
        return BoardCommandResponse.builder()
                .board(boardCreateUseCase.createBoard(boardDtoMapper.toDomain(boardCreateCommand)))
                .build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Board updateBoard(@PathVariable("id") Long id,
                             @RequestBody BoardUpdateCommand boardUpdateCommand) {
        return boardUpdateUseCase.updateBoard(boardDtoMapper.toDomain(id, boardUpdateCommand));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBoard(@PathVariable("id") Long id) {
        boardDeleteUseCase.deleteBoard(id);
    }
}
