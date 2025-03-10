package me.nettee.adapter.driven.persistence

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.nettee.board.adapter.driven.persistence.BoardCommandAdapter
import me.nettee.board.adapter.driven.persistence.BoardJpaRepository
import me.nettee.board.adapter.driven.persistence.entity.type.BoardEntityStatus
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.core.jpa.JpaTransactionalFreeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import java.util.*

@DataJpaTest
@ComponentScan(basePackageClasses = [BoardEntityMapper::class])
class BoardCommandAdapterTest(
    @Autowired private val repository: BoardJpaRepository,
    @Autowired private val mapper : BoardEntityMapper
) : JpaTransactionalFreeSpec({
    val adapter = BoardCommandAdapter(repository, mapper)
    val testTitle = "Test Title"
    val testContent = "Test Content"

    "[Pass] findById" - {
        val savedTitle = "search Title"
        val savedContent = "search Content"
        val savedStatus = BoardStatus.PENDING
        val board = Board.builder()
                .title(savedTitle)
                .content(savedContent)
                .status(savedStatus)
                .build()

        val savedId = repository.save(mapper.toEntity(board)).id
        val originBoardEntity = repository.findById(savedId).get()

        "조회할 Board -> 조회된 Board" - {
            val result = adapter.findById(savedId).get()

            "id 값 유지" {
                result.id shouldBeEqual savedId
            }

            "title, content, status 값 유지" {
                result.title shouldBeEqual savedTitle
                result.content shouldBeEqual savedContent
                result.status shouldBeEqual savedStatus
            }

            "createdAt 값 유지" {
                result.createdAt shouldBeEqual originBoardEntity.createdAt
            }

            "updatedAt 값 유지" {
                result.updatedAt shouldBeEqual  originBoardEntity.updatedAt
            }
        }
    }

    "[Pass] Create(boardDomain)" - {
        val board = Board.builder()
            .title(testTitle)
            .content(testContent)
            .status(BoardStatus.ACTIVE)
            .build()

        "save: boardDomain -> boardDomain" - {
            val result = adapter.create(board)

            "id 생성" {
                result.id shouldNotBe null
            }

            "title, content 입력 일치 확인" {
                result.title shouldBeEqual testTitle
                result.content shouldBeEqual testContent
            }

            "status 값 변경: null -> BoardStatus.ACTIVE" {
                result.status shouldBeEqual BoardStatus.ACTIVE
            }

            "createdAt, updatedAt 시간 생성 확인" {
                result.createdAt shouldNotBe null
                result.updatedAt shouldNotBe null
            }

            // NOTE: createdAt, updatedAt을 동일하게 만들자는 요건 시
            "createdAt과 updatedAt 일치 확인" {
                result.createdAt shouldBeEqual result.updatedAt
            }
        }
    }

    "[Exception] Create(noTitleDomain)" - {
        val noTitleBoard = Board.builder()
            .content(testContent)
            .build()

        "noTitleDomain -> Exception(title cannot be null)" - {
            val result = shouldThrow<NullPointerException> {
                Objects.requireNonNull(noTitleBoard.title, "Board not found")
            }

            "title 값이 없으므로 Null 에러 발생" {
                result.message shouldBe "Board not found"
            }
        }
    }

    "[Exception] Create(noContentDomain)" - {
        val noContentBoard = Board.builder()
            .title(testTitle)
            .build()

        "noContentDomain -> Exception(content cannot be null)" - {
            val result = shouldThrow<NullPointerException> {
                Objects.requireNonNull(noContentBoard.content, "Board not found")
            }

            "content 값이 없으므로 Null 에러 발생" {
                result.message shouldBe "Board not found"
            }
        }
    }

    "[Pass] Update(boardDomain)" - {
        val editedTitle = "Update Title"
        val editedContent = "Update Content"
        val editedStatus = BoardStatus.PENDING
        val board = Board.builder()
            .title(testTitle)
            .content(testContent)
            .status(BoardStatus.ACTIVE)
            .build()

        val savedId = repository.save(mapper.toEntity(board)).id
        val originBoardEntity = repository.findById(savedId).get()

        val editedBoard = Board.builder()
            .id(savedId)
            .title(editedTitle)
            .content(editedContent)
            .status(editedStatus)
            .build()

        "수정할 Board -> 수정된 Board" - {
            val result = adapter.update(editedBoard)

            "id 값 유지" {
                result.id shouldBeEqual savedId
            }

            "title, content, status 값 변경" {
                result.title shouldBeEqual editedTitle
                result.content shouldBeEqual editedContent
                result.status shouldBeEqual editedStatus
            }

            "createdAt 값 유지" {
                result.createdAt shouldBeEqual originBoardEntity.createdAt
            }

            "updatedAt 값 변경" {
                result.updatedAt shouldNotBe  originBoardEntity.updatedAt
            }
        }
    }

    "[Pass] updateStatus" - {
        val board = Board.builder()
            .title(testTitle)
            .content(testContent)
            .status(BoardStatus.ACTIVE)
            .build()

        val savedBoardEntity = repository.save(mapper.toEntity(board))

        "ACTIVE -> REMOVED로 상태 변경시" - {
            adapter.updateStatus(savedBoardEntity.id, BoardStatus.REMOVED)

            "[성공 처리]REMOVED 확인" {
                val deletedBoard = repository.findById(savedBoardEntity.id)
                deletedBoard.isPresent shouldBe true
                deletedBoard.get().status shouldBe BoardEntityStatus.REMOVED
            }
        }
    }
})