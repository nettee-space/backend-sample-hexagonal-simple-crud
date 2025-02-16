package me.nettee.board.adapter.driven.persistence.mapper;

import java.util.Optional;
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.model.BoardQueryModel.BoardDetail;
import me.nettee.board.application.model.BoardQueryModel.BoardSummary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardEntityMapper {

    Board toDomain(BoardEntity boardEntity);

    BoardEntity toEntity(Board board);

    BoardDetail toBoardDetail(BoardEntity boardEntity);

    BoardSummary toBoardSummary(BoardEntity boardEntity);

    default Optional<Board> toOptionalDomain(BoardEntity boardEntity) {
        return Optional.ofNullable(toDomain(boardEntity));
    }

    default Optional<BoardDetail> toOptionalBoardReadDetailModel(BoardEntity boardEntity) {
        return Optional.ofNullable(toBoardDetail(boardEntity));
    }
}
