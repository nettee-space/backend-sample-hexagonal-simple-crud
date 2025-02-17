package me.nettee.board.adapter.driven.persistence.mapper;

import java.util.Optional;
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.adapter.driven.persistence.entity.type.BoardEntityStatus;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardQueryModels.BoardDetail;
import me.nettee.board.application.model.BoardQueryModels.BoardSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BoardEntityMapper {

    @Mapping(source="status", target="status", qualifiedByName="toBoardStatus")
    Board toDomain(BoardEntity boardEntity);

    @Mapping(source="status", target="status", qualifiedByName="toBoardEntityStatus")
    BoardEntity toEntity(Board board);

    @Mapping(source="status", target="status", qualifiedByName="toBoardStatus")
    BoardDetail toBoardDetail(BoardEntity boardEntity);

    @Mapping(source="status", target="status", qualifiedByName="toBoardStatus")
    BoardSummary toBoardSummary(BoardEntity boardEntity);

    default Optional<Board> toOptionalDomain(BoardEntity boardEntity) {
        return Optional.ofNullable(toDomain(boardEntity));
    }

    default Optional<BoardDetail> toOptionalBoardDetail(BoardEntity boardEntity) {
        return Optional.ofNullable(toBoardDetail(boardEntity));
    }

    @Named("toBoardStatus")
    static BoardStatus toBoardStatus(BoardEntityStatus status) {
        return switch (status) {
            case REMOVED -> BoardStatus.REMOVED;
            case PENDING -> BoardStatus.PENDING;
            case ACTIVE -> BoardStatus.ACTIVE;
            case SUSPENDED -> BoardStatus.SUSPENDED;
        };
    }

    @Named("toBoardEntityStatus")
    static BoardEntityStatus toBoardEntityStatus(BoardStatus status) {
        return switch (status) {
            case REMOVED -> BoardEntityStatus.REMOVED;
            case PENDING -> BoardEntityStatus.PENDING;
            case ACTIVE -> BoardEntityStatus.ACTIVE;
            case SUSPENDED -> BoardEntityStatus.SUSPENDED;
        };
    }
}
