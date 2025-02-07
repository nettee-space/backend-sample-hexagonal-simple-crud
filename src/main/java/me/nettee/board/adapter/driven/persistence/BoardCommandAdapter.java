package me.nettee.board.adapter.driven.persistence;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.port.BoardCommandPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardCommandAdapter implements BoardCommandPort {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardEntityMapper boardEntityMapper;

    @Override
    public Optional<Board> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Board create(Board board) {
        var boardEntity = boardEntityMapper.toEntity(board);

        return boardEntityMapper.toDomain(boardJpaRepository.save(boardEntity));
    }

    @Override
    public Board update(Board board) {
        var existBoard = boardJpaRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("board not found"));

        existBoard.prepareUpdate()
                .title(board.getTitle())
                .content(board.getContent())
                .status(board.getStatus())
                .update();

        return boardEntityMapper.toDomain(boardJpaRepository.save(existBoard));
    }

    @Override
    public void delete(Long id) {
            if(!boardJpaRepository.existsById(id)) throw new IllegalArgumentException("board not found");

            boardJpaRepository.deleteById(id);
    }
}
