package me.nettee.board.adapter.driving.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import me.nettee.board.application.domain.Board;

public final class BoardCommandDto {

    private BoardCommandDto(){}

    public record BoardCreateCommand(
            @NotBlank(message = "제목을 입력하십시오.")
            String title,
            @NotBlank(message = "본문을 입력하십시오")
            String content)
    {}

    public record BoardUpdateCommand(
            @NotBlank(message = "제목을 입력하십시오.")
            String title,
            @NotBlank(message = "본문을 입력하십시오")
            String content)
    {}

    @Builder
    @JsonRootName("board")
    public record BoardCommandResponse(
            Board board
    )
    {}
}
