package me.nettee.board.adapter.driving.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCommandResponse;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand;
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper;
import me.nettee.board.application.usecase.BoardCreateUseCase;
import me.nettee.board.application.usecase.BoardDeleteUseCase;
import me.nettee.board.application.usecase.BoardUpdateUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardCommandController {

    private final BoardCreateUseCase boardCreateUseCase;
    private final BoardUpdateUseCase boardUpdateUseCase;
    private final BoardDeleteUseCase boardDeleteUseCase;
    private final BoardDtoMapper boardDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardCommandResponse createBoard(@RequestBody @Valid BoardCreateCommand boardCreateCommand) {
        // Map to Domain
        var board = boardDtoMapper.toDomain(boardCreateCommand);

        return BoardCommandResponse.builder()
                .board(boardCreateUseCase.createBoard(board))
                .build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardCommandResponse updateBoard(@PathVariable("id") Long id,
                                            @Valid @RequestBody BoardUpdateCommand boardUpdateCommand) {
        // Map to Domain
        var board = boardDtoMapper.toDomain(id, boardUpdateCommand);

        return BoardCommandResponse.builder()
                .board(boardUpdateUseCase.updateBoard(board))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable("id") Long id) {
        boardDeleteUseCase.deleteBoard(id);
    }
}
