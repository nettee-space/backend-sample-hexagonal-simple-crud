package me.nettee.board.adapter.driving.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardUpdateCommand(
        @NotBlank(message = "제목을 입력하십시오.")
        @Size(max = 50, message = "제목은 최대 50글자입니다.")
        String title,

        @NotBlank(message = "내용을 입력하십시오.")
        @Size(min = 3, message = "내용은 세 글자 이상 입력하세요.")
        String content
) {
}
