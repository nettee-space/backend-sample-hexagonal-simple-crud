package me.nettee.board.adapter.driving.web.dto;

import lombok.Builder;
import me.nettee.board.application.model.BoardQueryModels.BoardDetail;

public final class BoardQueryDto {
    private BoardQueryDto() {}

    @Builder
    public record BoardDetailResponse(
            BoardDetail board
    ){}
}
