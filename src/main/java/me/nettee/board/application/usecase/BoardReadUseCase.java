package me.nettee.board.application.usecase;

import me.nettee.board.application.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReadUseCase {

    Board getBoard(Long id);
}