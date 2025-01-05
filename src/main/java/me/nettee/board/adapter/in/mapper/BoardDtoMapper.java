package me.nettee.board.adapter.in.mapper;

import me.nettee.board.adapter.in.web.dto.BoardCreateCommand;
import me.nettee.board.adapter.in.web.dto.BoardDto;
import me.nettee.board.adapter.in.web.dto.BoardUpdateCommand;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardDtoMapper {
    BoardDto toDto(Board board);

    Board toDomain(BoardCreateCommand command);

    Board toDomain(Long id, BoardUpdateCommand command);
}
