package me.nettee.board.adapter.driven.persistence

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import me.nettee.board.adapter.driven.mapper.BoardEntityMapper
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import java.time.Instant
import java.util.Optional

@DataJpaTest
@ComponentScan(basePackageClasses = [BoardQueryAdapter::class, BoardEntityMapper::class])
@Transactional
@ActiveProfiles("test")
class BoardQueryAdapterSpringBootTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var boardQueryAdapter: BoardQueryAdapter

    private lateinit var insertedBoards: List<BoardEntity>

    @BeforeEach
    fun setup() {
        // 테스트 데이터 삽입
        insertedBoards = (1..5).map { idx ->
            val board = BoardEntity.builder()
                    .title("제목 $idx")
                    .content("내용 $idx")
                    .status(BoardStatus.ACTIVE)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build()
            entityManager.persist(board)
            board
        }
        entityManager.flush() // DB에 반영
        entityManager.clear() // 영속성 컨텍스트 초기화
    }

    @Test
    fun `findById - ID로 BoardEntity를 조회할 수 있다`() {
        // Given
        val idToFind = insertedBoards[0].id

        // When
        val result: Optional<Board> = boardQueryAdapter.findById(idToFind)

        // Then
        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(idToFind)
    }

    @Test
    fun `findAll - 모든 BoardEntity를 페이지네이션 없이 조회할 수 있다`() {
        // Given
        val pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending())

        // When
        val page = boardQueryAdapter.findAll(pageable)

        // Then
        assertThat(page.totalElements).isEqualTo(5)
        assertThat(page.content.size).isEqualTo(5)
    }

    @Test
    fun `findByStatusesList - 특정 상태 목록으로 필터링할 수 있다`() {
        // Given
        val statuses = listOf(BoardStatus.ACTIVE, BoardStatus.PENDING)
        val pageable = PageRequest.of(0, 10)

        // When
        val page = boardQueryAdapter.findByStatusesList(pageable, statuses)

        // Then
        assertThat(page.totalElements).isEqualTo(5)
        page.content.forEach {
            assertThat(it.status).isIn(BoardStatus.ACTIVE, BoardStatus.PENDING)
        }
    }
}
