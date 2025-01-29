package me.nettee.board.application.usecase;

import me.nettee.board.application.model.BoardReadDetailModel;

public interface BoardReadUseCase {

   BoardReadDetailModel getBoard(Long id);

}