package me.nettee.board.adapter.driving.web.dto;

import lombok.Builder;

public final class BoardQueryDto {
    private BoardQueryDto() {}

    @Builder
    public record BoardSelectOneResponse(
         BoardDetailDto board
    ){}


}
