package me.nettee.board.adapter.driven.persistence;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.port.BoardQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static me.nettee.board.adapter.driven.persistence.entity.QBoardEntity.boardEntity;

@Repository
public class BoardQueryAdapter extends QuerydslRepositorySupport implements BoardQueryPort {

    private final BoardEntityMapper boardEntityMapper;

    public BoardQueryAdapter(BoardEntityMapper boardEntityMapper) {
        super(BoardEntity.class);
        this.boardEntityMapper = boardEntityMapper;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        var result = getQuerydsl().createQuery()
                .select(boardEntity)
                .from(boardEntity)
                .where(
                        boardEntity.status.eq(BoardStatus.ACTIVE) // board status가 ACTIVE인 것만 조회
                ).fetch();

        var totalCount = getQuerydsl().createQuery()
                .select(boardEntity.count())
                .from(boardEntity)
                .where(
                        boardEntity.status.eq(BoardStatus.ACTIVE) // board status가 ACTIVE인 것만 조회
                );

        return PageableExecutionUtils.getPage(
                result.stream().map(boardEntityMapper::toDomain).toList(),
                pageable,
                totalCount::fetchOne
        );
    }

    @Override
    public Optional<Board> findById(Long id) {
        // 파라매터로 status 를 받을 수 도 있지 않을까?
        return boardEntityMapper.toOptionalDomain(
                getQuerydsl().createQuery()
                        .select(boardEntity)
                        .from(boardEntity)
                        .where(
                                boardEntity.id.eq(id),
                                boardEntity.status.eq(BoardStatus.ACTIVE) // board status가 ACTIVE인 것만 조회
                        ).fetchOne()
        );
    }
}
