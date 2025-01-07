package me.nettee.board.adapter.driving.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import me.nettee.board.application.domain.type.BoardStatus;

import java.time.Instant;

public final class BoardQueryDto {
    private BoardQueryDto() {}


    @Builder
    public record BoardSummaryResponse(
            Long id,
            String title,
            BoardStatus status,
            Instant createdAt
    ){}

    @JsonRootName(value = "board")
    @Builder
    public record BoardDetailResponse(
            Long id,
            String title,
            String content,
            BoardStatus status,
            Instant createdAt,
            Instant updatedAt
    ){}
}
