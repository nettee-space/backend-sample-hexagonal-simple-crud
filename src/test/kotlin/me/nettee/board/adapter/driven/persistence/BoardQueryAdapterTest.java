//package me.nettee.board.adapter.driven.persistence;
//
//import me.nettee.board.adapter.driven.mapper.BoardEntityMapper;
//import me.nettee.board.adapter.driven.persistence.entity.BoardEntity;
//import me.nettee.board.application.domain.Board;
//import me.nettee.board.application.domain.type.BoardStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class BoardQueryAdapterTest {
//    @InjectMocks
//    private BoardQueryAdapter boardQueryAdapter;
//    @Mock
//    private BoardJpaRepository boardJpaRepository;
//    @Mock
//    private BoardEntityMapper boardEntityMapper;
//
//    private Board board;
//    private BoardEntity boardEntity;
//
//    @BeforeEach
//    void setUp() {
//        board = Board.builder()
//                .id(1L)
//                .title("제목")
//                .content("내용")
//                .status(BoardStatus.ACTIVE)
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        boardEntity = BoardEntity.builder()
//                .id(1L)
//                .title("제목")
//                .content("내용")
//                .status(BoardStatus.ACTIVE)
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//    }
//
//    @Test
//    void 단건조회_테스트_게시글존재_케이스() {
//        // Given
//        Long boardId = 1L;
//
//        // Mock 객체의 동작 설정
//        when(boardJpaRepository.findById(boardId)).thenReturn(Optional.of(boardEntity));
//        when(boardEntityMapper.toDomain(boardEntity)).thenReturn(board);
//
//        // When
//        Optional<Board> result = boardQueryAdapter.findById(boardId);
//
//        // Then
//        assertTrue(result.isPresent()); // 반환된 Optional 객체에 값이 존재하는지 확인
//        assertEquals(board, result.get()); // 반환된 값이 예상된 값(board)과 동일한지 확인
//    }
//
//    @Test
//    void 단건조회_테스트_게시글존재하지않는_케이스() {
//        // Given
//        Long boardId = 1L;
//
//        // Mock 객체의 동작 설정 -> findById 메서드가 반환한 값이 비어 있으면 toDomain은 호출되지 않음
//        when(boardJpaRepository.findById(boardId)).thenReturn(Optional.empty());
//
//        // When
//        Optional<Board> result = boardQueryAdapter.findById(boardId);
//
//        // Then
//        assertTrue(result.isEmpty()); // 결과가 비어 있는지 검증
//    }
//}