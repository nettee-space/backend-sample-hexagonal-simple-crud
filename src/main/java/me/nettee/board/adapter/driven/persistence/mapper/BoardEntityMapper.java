package me.nettee.board.adapter.driven.persistence.mapper;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.application.domain.Board;
//import me.nettee.board.application.model.BoardReadDetailModel;
//import me.nettee.board.application.model.BoardReadSummaryModel;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BoardEntityMapper {

    Board toDomain(BoardEntity boardEntity);

    BoardEntity toEntity(Board board);

//    BoardReadDetailModel toBoardReadDetailModel(BoardEntity boardEntity);

//    BoardReadSummaryModel toBoardReadSummaryModel(BoardEntity boardEntity);

    default Optional<Board> toOptionalDomain(BoardEntity boardEntity) {
        return Optional.ofNullable(toDomain(boardEntity));
    }

//    default Optional<BoardReadDetailModel> toOptionalBoardReadDetailModel(BoardEntity boardEntity) {
//        return Optional.ofNullable(toBoardReadDetailModel(boardEntity));
//    }
}
