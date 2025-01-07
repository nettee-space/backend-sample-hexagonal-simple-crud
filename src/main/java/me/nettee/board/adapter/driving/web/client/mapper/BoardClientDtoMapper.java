package me.nettee.board.adapter.driving.web.client.mapper;

import me.nettee.board.adapter.driving.web.dto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardDto;
import me.nettee.board.adapter.driving.web.dto.BoardDto.BoardDetailDto;
import me.nettee.board.adapter.driving.web.dto.BoardDto.BoardSummaryDto;
import me.nettee.board.adapter.driving.web.dto.BoardUpdateCommand;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardClientDtoMapper {
    BoardDto toDto(Board board);

    Board toDomain(BoardCreateCommand command);

    Board toDomain(Long id, BoardUpdateCommand command);

    BoardDetailDto toDtoDetailDto(Board board);

    BoardSummaryDto toDtoSummary(Board board);

}
