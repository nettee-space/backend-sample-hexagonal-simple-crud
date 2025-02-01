package me.nettee.board.adapter.driven.persistence

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import me.nettee.board.adapter.driven.mapper.BoardEntityMapper
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity
import me.nettee.board.application.domain.type.BoardStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.Instant

@ComponentScan(basePackageClasses = [BoardEntityMapper::class])
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
class BoardQueryAdapterSpringBootTest (
        @Autowired private val boardJpaRepository : BoardJpaRepository,
        @Autowired private val boardEntityMapper : BoardEntityMapper,
        @Autowired private val entityManager : EntityManager,
) : FreeSpec({
    val boardQueryAdapter = BoardQueryAdapter(boardEntityMapper)

    boardQueryAdapter.setEntityManager(entityManager)

    val boardEntity = BoardEntity.builder()
            .title("제목")
            .content("내용")
            .status(BoardStatus.ACTIVE)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build()

    beforeSpec {
        boardJpaRepository.save(boardEntity)
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

    "[Read] 게시글 목록 조회" - { // RootNode
        // Given: 여러 게시물들을 저장
        val boardEnties =
                (1..5).flatMap {
                listOf(
                        BoardEntity.builder()
                                .title("title$it")
                                .content("content$it")
                                .status(BoardStatus.ACTIVE)
                                .build()
                    )
                }
        boardJpaRepository.saveAll(
            boardEnties
        )
        "[정상] 게시글이 존재할 때" - {
            // When: 게시글 목록 조회
            val pageable: Pageable = PageRequest.of(0, 10)
            val fetchedBoards = boardQueryAdapter.findAll(pageable)
            val expectedSize = boardEnties.size

            "[검증1] 게시글들이 존재하는지 검증" {
                fetchedBoards.hasContent() shouldBe true
            }

            "[검증2] 조회된 게시글 수를 검증" {
                fetchedBoards.content.size shouldBe expectedSize
            }
        }
    }

    "[Read] 특정 상태 목록으로 게시글 목록을 조회" - {
        // Given: 특정 상태에 해당하는 게시글 저장
        val boardEntities = listOf(
                BoardEntity.builder()
                        .title("게시글 1")
                        .content("내용 1")
                        .status(BoardStatus.ACTIVE)
                        .build(),
                BoardEntity.builder()
                        .title("게시글 2")
                        .content("내용 2")
                        .status(BoardStatus.PENDING)
                        .build(),
                BoardEntity.builder()
                        .title("게시글 3")
                        .content("내용 3")
                        .status(BoardStatus.ACTIVE)
                        .build()
        )
        boardJpaRepository.saveAll(
            boardEntities
        )

        // When: 특정 상태 목록으로 게시글을 조회
        val statuses = listOf(BoardStatus.ACTIVE, BoardStatus.PENDING)
        val pageable = PageRequest.of(0, 10)
        val page = boardQueryAdapter.findByStatusesList(pageable, statuses)
        val expectedSize = boardEntities.size

        "[검증1] 필터링된 게시글 총 개수를 검증" {
            page.totalElements shouldBe expectedSize
        }

        "[검증2] 필터링된 게시글의 상태를 검증" {
            page.content.forEach {
                it.status shouldBeIn statuses
            }
        }
    }

})
