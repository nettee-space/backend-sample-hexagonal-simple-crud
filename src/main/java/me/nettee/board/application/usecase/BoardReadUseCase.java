package me.nettee.board.application.usecase;

import me.nettee.board.application.domain.Board;
import me.nettee.board.application.model.BoardReadDetailModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReadUseCase {

    BoardReadDetailModel getBoard(Long id);
}