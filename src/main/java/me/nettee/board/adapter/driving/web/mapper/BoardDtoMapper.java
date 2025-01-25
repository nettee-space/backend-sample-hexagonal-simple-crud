package me.nettee.board.adapter.driving.web.mapper;

import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardUpdateCommand;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse;
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.model.BoardReadDetailModel;
import me.nettee.board.application.model.BoardReadSummaryModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardDtoMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    Board toDomain(BoardCreateCommand command);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "command.title")
    @Mapping(target = "content", source = "command.content")
    Board toDomain(Long id, BoardUpdateCommand command);

    BoardDetailResponse toDtoDetail(BoardReadDetailModel board);

    BoardSummaryResponse toDtoSummary(BoardReadSummaryModel board);

}
