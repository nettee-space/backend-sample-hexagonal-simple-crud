package me.nettee.board.application.model;

import me.nettee.board.application.domain.type.BoardStatus;

import java.time.Instant;

public record BoardReadDetailModel(
        Long id,
        String title,
        String content,
        BoardStatus status,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
