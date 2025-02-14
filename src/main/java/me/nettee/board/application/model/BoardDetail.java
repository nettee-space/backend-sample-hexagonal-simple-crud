package me.nettee.board.application.model;

import java.time.Instant;
import me.nettee.board.application.domain.type.BoardStatus;

public record BoardDetail(
    Long id,
    String title,
    String content,
    BoardStatus status,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {}