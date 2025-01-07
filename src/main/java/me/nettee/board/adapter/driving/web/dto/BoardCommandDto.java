package me.nettee.board.adapter.driving.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import me.nettee.board.application.domain.Board;

public final class BoardCommandDto {

    private BoardCommandDto(){}

    public record BoardCreateCommand(Long id, String title, String content) {

    }

    public record BoardUpdateCommand(Long id, String title, String content) {

    }

    @Builder
    @JsonRootName("board")
    public record BoardCommandResponse(Board board){
    }
}
