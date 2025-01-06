package me.nettee.board.adapter.driving.web.dto;

import lombok.Builder;
import me.nettee.board.application.domain.type.BoardStatus;

import java.time.Instant;

public class BoardDto {

    @Builder
    public record BoardBasicDto(
            Long id,
            String title,
            String content
    ) {}

    @Builder
    public record BoardSummaryDto(
            Long id,
            String title,
            BoardStatus status,
            Instant createdAt
    ){}

    @Builder
    public record BoardDetailDto(
            Long id,
            String title,
            String content,
            BoardStatus status,
            Instant createdAt,
            Instant updatedAt
    ){}
}
