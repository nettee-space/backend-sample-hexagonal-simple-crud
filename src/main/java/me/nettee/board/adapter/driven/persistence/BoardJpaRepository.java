package me.nettee.board.adapter.driven.persistence;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import me.nettee.board.application.domain.type.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByStatusIn(Set<BoardStatus> statuses, Pageable pageable);
    Page<BoardEntity> findByStatus(BoardStatus status, PageRequest pageRequest);
}