package me.nettee.board.adapter.driven.mapper;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;

import java.util.Optional;
import me.nettee.board.application.model.BoardReadDetailModel;
import me.nettee.board.application.model.BoardReadSummaryModel;

// Entity <-> Domain 매핑 클래스
@Mapper(componentModel = "spring")
public interface BoardEntityMapper {
    Board toDomain(BoardEntity boardEntity);
    BoardReadDetailModel toBoardReadDetailModel(BoardEntity boardEntity);
    BoardReadSummaryModel toBoardReadSummaryModel(BoardEntity boardEntity);
    BoardEntity toEntity(Board board);

    default Optional<BoardReadDetailModel> toOptionalBoardReadDetailModel(BoardEntity boardEntity) {
        return Optional.ofNullable(toBoardReadDetailModel(boardEntity));
    }
}