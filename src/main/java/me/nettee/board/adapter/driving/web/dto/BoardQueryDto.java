package me.nettee.board.adapter.driving.web.dto;

import lombok.Builder;
import me.nettee.board.application.model.BoardReadDetailModel;

public final class BoardQueryDto {
    private BoardQueryDto() {}

    @Builder
    public record BoardDetailResponse(
            BoardReadDetailModel board
    ){}
}
