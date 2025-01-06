package me.nettee.board.adapter.driving.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import me.nettee.board.adapter.driving.web.dto.BoardDto.BoardDetailDto;
import me.nettee.board.adapter.driving.web.dto.BoardDto.BoardSummaryDto;
import java.util.List;

public final class BoardQueryDto {
    private BoardQueryDto() {}

    @Builder
    public record BoardSelectOneResponse(
         BoardDetailDto board
    ){}


}
