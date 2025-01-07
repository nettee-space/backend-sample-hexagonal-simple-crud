package me.nettee.board.adapter.driven.persistence;

import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driven.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.board.application.port.BoardQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardQueryAdapter implements BoardQueryPort {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardEntityMapper boardEntityMapper;

    @Override
    public Optional<Board> findById(Long id) {
        return boardJpaRepository.findById(id)
                .map(boardEntityMapper::toDomain);
    }

    @Override
    public Page<Board> findBoardListByStatus(BoardStatus status, int pageNumber, int size) {
        // Sort 객체를 생성하여 정렬 기준을 설정합니다.
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        // 페이지 번호와 페이지 크기를 사용하여 PageRequest 객체를 생성합니다.
        PageRequest pageRequest = PageRequest.of(pageNumber, size, sort);

        return boardJpaRepository.findBoardByStatus(status, pageRequest)
                .map(boardEntityMapper::toDomain);
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardJpaRepository.findAll(pageable)
                .map(boardEntityMapper::toDomain);
    }


}