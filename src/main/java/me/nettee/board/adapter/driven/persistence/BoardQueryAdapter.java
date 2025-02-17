package me.nettee.board.adapter.driven.persistence;

import static me.nettee.board.adapter.driven.persistence.entity.QBoardEntity.boardEntity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.adapter.driven.persistence.entity.type.BoardEntityStatus;
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardQueryModels.BoardDetail;
import me.nettee.board.application.model.BoardQueryModels.BoardSummary;
import me.nettee.board.application.port.BoardQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BoardQueryAdapter extends QuerydslRepositorySupport implements BoardQueryPort {

    private final BoardEntityMapper boardEntityMapper;

    public BoardQueryAdapter(
        BoardEntityMapper boardEntityMapper) {
        super(BoardEntity.class);
        this.boardEntityMapper = boardEntityMapper;
    }

    @Override
    public Optional<BoardDetail> findById(Long id) {
        return boardEntityMapper.toOptionalBoardReadDetailModel(
                getQuerydsl().createQuery()
                        .select(boardEntity)
                        .from(boardEntity)
                        .where(boardEntity.id.eq(id)
                        ).fetchOne()
        );
    }

    @Override
    public Page<BoardSummary> findAll(Pageable pageable) {
        // 기본 쿼리 생성
        var query = getQuerydsl().createQuery()
                .select(boardEntity)
                .from(boardEntity)
                .where();

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
                .where();

        return PageableExecutionUtils.getPage(
                result.stream().map(boardEntityMapper::toBoardSummary).toList(),
                pageable,
                totalCount::fetchOne
        );
    }

    @Override
    public Page<BoardSummary> findByStatusesList(Set<BoardStatus> statuses, Pageable pageable) {
        // 기본 쿼리 생성
        var boardEntityStatuses = statuses.stream().map(
            status -> BoardEntityStatus.valueOf(status)
        ).collect(Collectors.toSet());

        var query = getQuerydsl().createQuery()
                .select(boardEntity)
                .from(boardEntity)
                .where(boardEntity.status.in(boardEntityStatuses));

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
                .where(boardEntity.status.in(boardEntityStatuses));

        return PageableExecutionUtils.getPage(
                result.stream().map(boardEntityMapper::toBoardSummary).toList(),
                pageable,
                totalCount::fetchOne
        );
    }
}