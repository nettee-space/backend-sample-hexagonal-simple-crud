import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import me.nettee.board.adapter.driven.mapper.BoardEntityMapper
import me.nettee.board.adapter.driven.persistence.BoardJpaRepository
import me.nettee.board.adapter.driven.persistence.BoardQueryAdapter
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Instant


class BoardQueryAdapterTest {

    @InjectMockKs
    private lateinit var boardQueryAdapter: BoardQueryAdapter

    @MockK
    private lateinit var boardJpaRepository: BoardJpaRepository

    @MockK
    private lateinit var boardEntityMapper: BoardEntityMapper

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
//        boardQueryAdapter = BoardQueryAdapter(boardEntityMapper, boardJpaRepository)
    }

    @Test
    @DisplayName("findAll: Repository 호출 시 올바른 Pageable 전달")
    fun `findAll should call repository with correct pageable`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        every { boardJpaRepository.findAll(pageable) } returns PageImpl(emptyList())

        // When
        boardQueryAdapter.findAll(pageable)

        // Then
        verify { boardJpaRepository.findAll(pageable) }
    }

    @Test
    @DisplayName("findAll: Mapper를 사용하여 BoardEntity를 Board로 변환")
    fun `findAll should map entities to domain objects`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val entity1 = BoardEntity.builder().title("title1").content("content1").build()
        val entity2 = BoardEntity.builder().title("title2").content("content2").build()

        every { boardJpaRepository.findAll(pageable) } returns PageImpl(listOf(entity1, entity2), pageable, 2)
        every { boardEntityMapper.toDomain(entity1) } returns Board.builder().title("title1").build()
        every { boardEntityMapper.toDomain(entity2) } returns Board.builder().title("title2").build()

        // When
        boardQueryAdapter.findAll(pageable)

        // Then
        verify { boardEntityMapper.toDomain(entity1) }
        verify { boardEntityMapper.toDomain(entity2) }
    }

    @Test
    @DisplayName("findAll: 변환된 Board 목록 반환")
    fun `findAll should return correctly mapped boards`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val entity1 = BoardEntity.builder().title("title1").content("content1").build()
        val entity2 = BoardEntity.builder().title("title2").content("content2").build()
        val domain1 = Board.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .status(BoardStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build()
        val domain2 = Board.builder()
                .id(2L)
                .title("title2")
                .content("content2")
                .status(BoardStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build()

        every { boardJpaRepository.findAll(pageable) } returns PageImpl(listOf(entity1, entity2), pageable, 2)
        every { boardEntityMapper.toDomain(entity1) } returns domain1
        every { boardEntityMapper.toDomain(entity2) } returns domain2

        // When
        val result: Page<Board> = boardQueryAdapter.findAll(pageable)

        // Then
        val resultList = result.content
        assertThat(resultList[0]).usingRecursiveComparison().isEqualTo(domain1)
        assertThat(resultList[1]).usingRecursiveComparison().isEqualTo(domain2)
        assertThat(result.totalElements).isEqualTo(2)
    }

}
