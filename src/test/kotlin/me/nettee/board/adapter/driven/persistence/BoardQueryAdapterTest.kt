package me.nettee.board.adapter.driven.persistence

import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.core.jpa.JpaTransactionalFreeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@ComponentScan(basePackageClasses = [BoardEntityMapper::class])
@DataJpaTest
class BoardQueryAdapterTest (
        @Autowired private val boardJpaRepository: BoardJpaRepository,
        @Autowired private val boardEntityMapper: BoardEntityMapper,
        @Autowired private val entityManager: EntityManager,
) : JpaTransactionalFreeSpec({
    val boardQueryAdapter = BoardQueryAdapter(boardEntityMapper)

    boardQueryAdapter.setEntityManager(entityManager)

    val boardEntity = BoardEntity.builder()
            .title("제목")
            .content("내용")
            .status(BoardStatus.ACTIVE)
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
        val boardEntities = (1..5).flatMap {
            listOf(
                BoardEntity.builder()
                    .title("title$it")
                    .content("content$it")
                    .status(BoardStatus.ACTIVE)
                    .build()
            )
        }

        boardJpaRepository.saveAll(boardEntities)

        "[정상] 게시글이 존재할 때" - {
            // When: 게시글 목록 조회
            val pageable: Pageable = PageRequest.of(0, 10)
            val fetchedBoards = boardQueryAdapter.findAll(pageable)
            val expectedSize = boardEntities.size

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
        val boardEntities = (1..3).flatMap {
            listOf(
                BoardEntity.builder()
                    .title("게시글 $it")
                    .content("내용 $it")
                    .status(if (it % 2 == 0) { BoardStatus.ACTIVE } else { BoardStatus.PENDING })
                    .build()
            )
        }

        boardJpaRepository.saveAll(boardEntities)

        // When: 특정 상태 목록으로 게시글을 조회
        val statuses = setOf(BoardStatus.ACTIVE, BoardStatus.PENDING)
        val pageable = PageRequest.of(0, 10)
        val page = boardQueryAdapter.findByStatusesList(pageable, statuses)
        val expectedSize = boardEntities.size

        "[검증1] 필터링된 게시글 총 개수를 검증" {
            page.totalElements shouldBe expectedSize
        }
    }
})