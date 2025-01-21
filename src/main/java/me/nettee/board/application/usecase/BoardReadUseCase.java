package me.nettee.board.application.usecase;

import me.nettee.board.application.domain.Board;

public interface BoardReadUseCase {

    Board getBoard(Long id);

}