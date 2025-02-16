package me.nettee.board.adapter.driven.persistence;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driven.persistence.entity.type.BoardEntityStatus;
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.port.BoardCommandPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static me.nettee.board.application.exception.BoardCommandErrorCode.BOARD_NOT_FOUND;
import static me.nettee.board.application.exception.BoardCommandErrorCode.DEFAULT;

@Repository
@RequiredArgsConstructor
public class BoardCommandAdapter implements BoardCommandPort {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardEntityMapper boardEntityMapper;

    @Override
    public Optional<Board> findById(Long id) {
        var board = boardJpaRepository.findById(id)
                .orElseThrow(BOARD_NOT_FOUND::defaultException);

        return boardEntityMapper.toOptionalDomain(board);
    }

    @Override
    public Board create(Board board) {
        var boardEntity = boardEntityMapper.toEntity(board);

        if(boardJpaRepository.existsById(boardEntity.getId())) {
            // 이미 존재하는 게시판 입니다. 이라는 구체적인 에러 코드 추가가 필요해보임
            throw DEFAULT.defaultException();
        }

        return boardEntityMapper.toDomain(boardJpaRepository.save(boardEntity));
    }

    @Override
    public Board update(Board board) {
        var existBoard = boardJpaRepository.findById(board.getId())
                .orElseThrow(BOARD_NOT_FOUND::defaultException);

        existBoard.prepareUpdate()
                .title(board.getTitle())
                .content(board.getContent())
                .status(BoardEntityStatus.valueOf(board.getStatus()))
                .update();

        return boardEntityMapper.toDomain(boardJpaRepository.save(existBoard));
    }

    @Override
    public void updateStatus(Long id, BoardStatus status) {
        var board = boardJpaRepository.findById(id)
                .orElseThrow(BOARD_NOT_FOUND::defaultException);

        board.prepareUpdate()
                .status(BoardEntityStatus.valueOf(status))
                .update();

        boardJpaRepository.save(board);
    }
}
