package me.nettee.board.adapter.driving.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.model.BoardReadDetailModel;
import me.nettee.board.application.model.BoardReadSummaryModel;

import java.time.Instant;

public final class BoardQueryDto {
    private BoardQueryDto() {}

    @Builder
    public record BoardDetailResponse(
            BoardReadDetailModel board
    ){}
}
