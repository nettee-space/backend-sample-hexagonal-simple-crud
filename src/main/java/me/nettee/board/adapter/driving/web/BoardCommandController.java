package me.nettee.board.adapter.driving.web;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driving.web.dto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardUpdateCommand;
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
    public Board createBoard(@RequestBody BoardCreateCommand boardCreateCommand) {
        return boardCreateUseCase.createBoard(boardDtoMapper.toDomain(boardCreateCommand));
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
