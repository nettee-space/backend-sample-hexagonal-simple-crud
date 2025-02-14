package me.nettee.board.application.usecase;

import me.nettee.board.application.model.BoardModel.BoardDetail;

public interface BoardReadUseCase {

   BoardDetail getBoard(Long id);
}