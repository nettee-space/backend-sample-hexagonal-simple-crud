package me.nettee.adapter.driven.persistence

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.nettee.board.adapter.driven.persistence.BoardCommandAdapter
import me.nettee.board.adapter.driven.persistence.BoardJpaRepository
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.core.jpa.JpaTransactionalFreeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import java.time.temporal.ChronoUnit
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
                // 실질적으로는 Board,BoardEntity 를 처리하는 로직을 담아야하는게 아닌가?
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

        val savedBoardEntity = repository.save(mapper.toEntity(board))

        val editedBoard = Board.builder()
            .id(savedBoardEntity.id)
            .title(editedTitle)
            .content(editedContent)
            .status(editedStatus)
            .build()

        "savedBoard -> updatedBoard" - {

            val result = adapter.update(editedBoard)

            "id 값 유지" {
                result.id shouldBeEqual savedBoardEntity.id
            }

            "title, content, status 값 변경" {
                result.title shouldBeEqual editedTitle
                result.content shouldBeEqual editedContent
                result.status shouldBeEqual editedStatus
            }

            "createdAt 값 유지" {
                result.createdAt.truncatedTo(ChronoUnit.MILLIS) shouldBeEqual savedBoardEntity.createdAt.truncatedTo(ChronoUnit.MILLIS)
            }

            "updatedAt 값 변경" {
                result.updatedAt shouldNotBe  savedBoardEntity.updatedAt
            }
        }
    }

    "[Pass] Delete(id)" - {
        val board = Board.builder()
            .title(testTitle)
            .content(testContent)
            .status(BoardStatus.ACTIVE)
            .build()

        val savedBoardEntity = repository.save(mapper.toEntity(board))

        "id -> void" - {
            adapter.delete(savedBoardEntity.id)

            // NOTE: 실습용으로 2가지 사용 - 의미는 동일
            "data 존재 확인" {
                repository.existsById(savedBoardEntity.id) shouldBe false
                repository.findById(savedBoardEntity.id).isEmpty shouldBe true
            }
        }
    }
})
//{
//    override fun extensions() =listOf(SpringExtension)
//}