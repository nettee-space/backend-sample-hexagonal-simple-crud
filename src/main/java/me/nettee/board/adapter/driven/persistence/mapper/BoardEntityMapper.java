package me.nettee.board.adapter.driven.persistence.mapper;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntityLong;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.model.BoardReadDetailModel;
import me.nettee.board.application.model.BoardReadSummaryModel;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BoardEntityMapper {

    Board toDomain(BoardEntityLong boardEntity);

    BoardEntityLong toEntity(Board board);

    BoardReadDetailModel toBoardReadDetailModel(BoardEntityLong boardEntity);

    BoardReadSummaryModel toBoardReadSummaryModel(BoardEntityLong boardEntity);

    default Optional<Board> toOptionalDomain(BoardEntityLong boardEntity) {
        return Optional.ofNullable(toDomain(boardEntity));
    }

    default Optional<BoardReadDetailModel> toOptionalBoardReadDetailModel(BoardEntityLong boardEntity) {
        return Optional.ofNullable(toBoardReadDetailModel(boardEntity));
    }
}
