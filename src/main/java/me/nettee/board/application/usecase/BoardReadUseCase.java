package me.nettee.board.application.usecase;

import me.nettee.board.application.model.BoardDetail;

public interface BoardReadUseCase {

   BoardDetail getBoard(Long id);
}