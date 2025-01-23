package me.nettee.board.adapter.driven.persistence

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
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
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import java.util.Optional



// QueryDsl 관련해서 뭔가 문제가 있음 ㅎㅎ! -> 리서치 필요
// 경우님 코멘트 -> 엔티티 매니저를 아마 넣어야할듯 하다.
@ComponentScan(basePackageClasses = [BoardEntityMapper::class])
@DataJpaTest // 데이터베이스와의 상호작용을 테스트할 수 있도록 서포트
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
class BoardQueryAdapterSpringBootTest (
        @Autowired private val boardJpaRepository: BoardJpaRepository,
        @Autowired private val boardEntityMapper: BoardEntityMapper,
        @Autowired private val entityManager: EntityManager,
) : FreeSpec({
    // 생성자
    val boardQueryAdapter = BoardQueryAdapter(boardEntityMapper);

    // 써치 해보기! 힌트 ㅎㅎ
    boardQueryAdapter.setEntityManager(entityManager)

    val boardEntity = BoardEntity.builder()
            .title("제목")
            .content("내용")
            .status(BoardStatus.ACTIVE)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build()

    beforeSpec {
        // Given: 게시글을 데이터베이스에 저장
        boardJpaRepository.save(boardEntity);
    }

    beforeEach {
        boardJpaRepository.deleteAll()
    }

    "[Read] 게시글 단건 조회" - { // RootNode
        "[정상] 게시글이 존재할 때" - {
            // When: 게시글을 조회
            val fetchedBoard = boardQueryAdapter.findById(1L)

            "[검증1] 조회된 게시글이 존재하는지 검증" {
                fetchedBoard.isPresent() shouldBe true
            }

            "[검증2] 불러온 board가 저장한 boardEntity와 동일한지 검증" {
                val board = fetchedBoard?.get()!!
                board.title shouldBe boardEntity.title
                board.content shouldBe boardEntity.content
                board.status shouldBe boardEntity.status
            }
        }
    }

    "[Read] 게시글 다수건 조회" - { // RootNode
        // Given: 여러 게시물들을 저장
        boardJpaRepository.saveAll(
                listOf(
                        BoardEntity.builder()
                                .title("제목1")
                                .content("내용1")
                                .status(BoardStatus.ACTIVE)
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build(),
                        BoardEntity.builder()
                                .title("제목2")
                                .content("내용2")
                                .status(BoardStatus.ACTIVE)
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build()
                )
        )
        "[정상] 게시글이 존재할 때" - {
            // When: 게시글 전수 조회
            val pageable: Pageable = PageRequest.of(0, 10)
            val fetchedBoards = boardQueryAdapter.findAll(pageable)

            "[검증1] 게시글들이 존재하는지 검증" {
                fetchedBoards.hasContent() shouldBe true
            }

            "[검증2] 조회된 게시글 수를 검증" {
                fetchedBoards.content.size shouldBe 2
            }
        }
    }

    "[Read] 특정 상태로 필터링하기" - {
        // Given: 특정 상태에 해당하는 게시글 저장
        boardJpaRepository.saveAll(
            listOf(
                BoardEntity.builder()
                        .title("게시글 1")
                        .content("내용 1")
                        .status(BoardStatus.ACTIVE)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build(),
                BoardEntity.builder()
                        .title("게시글 2")
                        .content("내용 2")
                        .status(BoardStatus.PENDING)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build(),
                BoardEntity.builder()
                        .title("게시글 3")
                        .content("내용 3")
                        .status(BoardStatus.ACTIVE)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
            )
        )

        // When: 특정 상태 목록으로 게시글을 조회
        val statuses = listOf(BoardStatus.ACTIVE, BoardStatus.PENDING)
        val pageable = PageRequest.of(0, 10)
        val page = boardQueryAdapter.findByStatusesList(pageable, statuses)

        "[검증1] 필터링된 게시글 총 개수를 검증" {
            page.totalElements shouldBe 3
        }

        "[검증2] 필터링된 게시글의 상태를 검증" {
            page.content.forEach {
                it.status shouldBeIn statuses
            }
        }
    }

})
