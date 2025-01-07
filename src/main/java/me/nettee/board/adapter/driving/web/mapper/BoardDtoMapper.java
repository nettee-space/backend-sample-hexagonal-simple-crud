package me.nettee.board.adapter.driving.web.mapper;

import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Board toDomain(BoardCreateCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Board toDomain(Long id, BoardUpdateCommand command);

    BoardDetailResponse toDtoDetail(Board board);

    BoardSummaryResponse toDtoSummary(Board board);

}
