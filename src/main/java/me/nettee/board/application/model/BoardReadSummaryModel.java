package me.nettee.board.application.model;

import java.time.Instant;

public record BoardReadSummaryModel(
    Long id,
    String title,
    String content,
    Instant createdAt,
    Instant updatedAt
){}