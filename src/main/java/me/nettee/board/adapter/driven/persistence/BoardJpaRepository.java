package me.nettee.board.adapter.driven.persistence;

import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {
}
