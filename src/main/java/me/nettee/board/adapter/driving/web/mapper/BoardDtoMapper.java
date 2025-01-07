package me.nettee.board.adapter.driving.web.mapper;

import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardDtoMapper {
    BoardDto toDto(Board board);

    Board toDomain(BoardCreateCommand command);

    Board toDomain(Long id, BoardUpdateCommand command);

    BoardDetailDto toDtoDetailDto(Board board);

    BoardSummaryDto toDtoSummary(Board board);

}
