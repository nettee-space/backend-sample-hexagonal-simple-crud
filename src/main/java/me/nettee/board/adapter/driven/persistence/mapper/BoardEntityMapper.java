package me.nettee.board.adapter.driven.persistence.mapper;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.model.BoardQueryModel.BoardSummary;
import me.nettee.board.application.model.BoardQueryModel.BoardDetail;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BoardEntityMapper {

    Board toDomain(BoardEntity boardEntity);

    BoardEntity toEntity(Board board);

    BoardDetail toBoardReadDetailModel(BoardEntity boardEntity);

    BoardSummary toBoardReadSummaryModel(BoardEntity boardEntity);

    default Optional<Board> toOptionalDomain(BoardEntity boardEntity) {
        return Optional.ofNullable(toDomain(boardEntity));
    }

    default Optional<BoardDetail> toOptionalBoardReadDetailModel(BoardEntity boardEntity) {
        return Optional.ofNullable(toBoardReadDetailModel(boardEntity));
    }
}
