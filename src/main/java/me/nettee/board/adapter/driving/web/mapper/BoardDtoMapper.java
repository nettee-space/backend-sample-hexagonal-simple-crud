package me.nettee.board.adapter.driving.web.mapper;

import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardDtoMapper {

    Board toDomain(BoardCreateCommand command);

    Board toDomain(Long id, BoardUpdateCommand command);

    BoardDetailResponse toDtoDetail(Board board);

    BoardSummaryResponse toDtoSummary(Board board);

}
