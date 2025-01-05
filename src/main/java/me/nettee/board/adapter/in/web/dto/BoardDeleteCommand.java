package me.nettee.board.adapter.in.web.dto;

import org.jetbrains.annotations.NotNull;

public record BoardDeleteCommand(
    @NotNull Long id
) {
}
