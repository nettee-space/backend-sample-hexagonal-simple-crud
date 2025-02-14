package me.nettee.board.application.model;

import me.nettee.board.application.domain.type.BoardStatus;

import java.time.Instant;

public final class BoardQueryModel {

    private BoardQueryModel() {
    }

    public record BoardDetail(
            Long id,
            String title,
            String content,
            BoardStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record BoardSummary(
            Long id,
            String title,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
