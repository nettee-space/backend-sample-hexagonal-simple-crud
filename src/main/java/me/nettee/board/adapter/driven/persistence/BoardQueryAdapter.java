package me.nettee.board.adapter.driven.persistence;

import me.nettee.board.adapter.driven.mapper.BoardEntityMapper;
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.adapter.driven.persistence.entity.QBoardEntity;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.port.BoardQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardQueryAdapter extends QuerydslRepositorySupport implements BoardQueryPort {
    private final BoardEntityMapper boardEntityMapper;
    QBoardEntity boardEntity = QBoardEntity.boardEntity;

    public BoardQueryAdapter(BoardEntityMapper boardEntityMapper) {
        super(BoardEntity.class);
        this.boardEntityMapper = boardEntityMapper;
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardEntityMapper.toOptionalDomain(
                getQuerydsl().createQuery()
                        .select(boardEntity)
                        .from(boardEntity)
                        .where(
                                boardEntity.id.eq(id),
                                boardEntity.status.ne(BoardStatus.REMOVED)
                        ).fetchOne()
        );
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        // 기본 쿼리 생성
        var query = getQuerydsl().createQuery()
                .select(boardEntity)
                .from(boardEntity)
                .where(boardEntity.status.ne(BoardStatus.REMOVED));

        // pageable 정렬 조건 적용
        pageable.getSort().forEach(order -> {
            if (order.isAscending()) {
                query.orderBy(boardEntity.createdAt.asc()); // createdAt 필드를 기준으로 오름차순 정렬
            } else {
                query.orderBy(boardEntity.createdAt.desc()); // createdAt 필드를 기준으로 내림차순 정렬
            }
        });

        var result = query
                .offset(pageable.getOffset()) // 현재 페이지의 오프셋 설정
                .limit(pageable.getPageSize()) // 페이지 크기 설정
                .fetch(); // 쿼리 실행

        var totalCount  = getQuerydsl().createQuery()
                .select(boardEntity.count())
                .from(boardEntity)
                .where(boardEntity.status.ne(BoardStatus.REMOVED));

        return PageableExecutionUtils.getPage(
                result.stream().map(boardEntityMapper::toDomain).toList(),
                pageable,
                totalCount::fetchOne
        );
    }

    @Override
    public Page<Board> findByStatusesList(Pageable pageable, List<BoardStatus> statuses) {
        // 기본 쿼리 생성
        var query = getQuerydsl().createQuery()
                .select(boardEntity)
                .from(boardEntity)
                .where(boardEntity.status.in(statuses));

        // pageable 정렬 조건 적용
        pageable.getSort().forEach(order -> {
            if (order.isAscending()) {
                query.orderBy(boardEntity.createdAt.asc()); // createdAt 필드를 기준으로 오름차순 정렬
            } else {
                query.orderBy(boardEntity.createdAt.desc()); // createdAt 필드를 기준으로 내림차순 정렬
            }
        });

        var result = query
                .offset(pageable.getOffset()) // 현재 페이지의 오프셋 설정
                .limit(pageable.getPageSize()) // 페이지 크기 설정
                .fetch(); // 쿼리 실행

        var totalCount  = getQuerydsl().createQuery()
                .select(boardEntity.count())
                .from(boardEntity)
                .where(boardEntity.status.in(statuses));

        return PageableExecutionUtils.getPage(
                result.stream().map(boardEntityMapper::toDomain).toList(),
                pageable,
                totalCount::fetchOne
        );
    }
}