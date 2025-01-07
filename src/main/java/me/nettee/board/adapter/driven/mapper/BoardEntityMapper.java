package me.nettee.board.adapter.driven.mapper;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.application.domain.Board;
import org.mapstruct.Mapper;

import java.util.Optional;

// Entity <-> Domain 매핑 클래스
@Mapper(componentModel = "spring")
public interface BoardEntityMapper {
    Board toDomain(BoardEntity boardEntity);

    BoardEntity toEntity(Board board);

    default Optional<Board> toOptionalDomain(BoardEntity boardEntity) {
        return Optional.ofNullable(toDomain(boardEntity));
    }
}