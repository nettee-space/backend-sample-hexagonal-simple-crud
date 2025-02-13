package me.nettee.board.adapter.driven.persistence;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.exception.BoardCommandErrorCode;
import me.nettee.board.application.port.BoardCommandPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardCommandAdapter implements BoardCommandPort {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardEntityMapper boardEntityMapper;

    // 2025.02.13 수용님 주석 한번 확인해주세요.
    @Override
    public Optional<Board> findById(Long id) {
        // Command 가 Master DB 조회를 하기 위함
        // 추후 Command 와 Query 트랜잭션이 분리되어 Command 는 Master DB Query는 Slave db를 바라볼때
        // Master DB를 바라보는 조회 영역도 필요함
        var board = boardJpaRepository.findById(id)
                .orElseThrow(BoardCommandErrorCode.BOARD_NOT_FOUND::defaultException);

        return boardEntityMapper.toOptionalDomain(board);
    }

    @Override
    public Board create(Board board) {
        var boardEntity = boardEntityMapper.toEntity(board);

        // 키 값 중복은 DBMS에서 에러를 던지기도 하지만
        // CustomException RestControllerAdvice 로 Client 에게 던져야 한다면 하기와 같이 처리
        if(boardJpaRepository.existsById(boardEntity.getId())) {
            // 이미 존재하는 게시판 입니다. 이라는 구체적인 에러 코드 추가가 필요해보임
            throw BoardCommandErrorCode.DEFAULT.defaultException();
        }

        return boardEntityMapper.toDomain(boardJpaRepository.save(boardEntity));
    }

    @Override
    public Board update(Board board) {
        var existBoard = boardJpaRepository.findById(board.getId())
                .orElseThrow(BoardCommandErrorCode.BOARD_NOT_FOUND::defaultException);

        existBoard.prepareUpdate()
                .title(board.getTitle())
                .content(board.getContent())
                .status(board.getStatus())
                .update();

        return boardEntityMapper.toDomain(boardJpaRepository.save(existBoard));
    }

    // PORT 부분에서 소프트 딜리트에 대한 정책 결정이 필요
    @Override
    public void delete(Long id) {
        if(!boardJpaRepository.existsById(id))
            throw BoardCommandErrorCode.BOARD_NOT_FOUND.defaultException(); //id 존재여부 확인

        boardJpaRepository.deleteById(id);
    }
}
