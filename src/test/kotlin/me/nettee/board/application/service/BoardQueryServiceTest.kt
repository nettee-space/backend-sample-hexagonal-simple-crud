package me.nettee.board.application.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.port.BoardQueryPort
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class BoardQueryServiceTest : FreeSpec({

    val boardQueryPort = mockk<BoardQueryPort>() // mocking
    val boardQueryService = BoardQueryService(boardQueryPort) // 주입

    "BoardQueryService" - {
        "findById" - {
            "존재하는 ID로 조회한다." {
                // given
                val boardId = 1L
                val expectedBoard = Board.builder().id(boardId).title("test title").build()
                every {
                    boardQueryPort.findById(boardId)
                } returns Optional.of(expectedBoard)

                // when
                val result = boardQueryService.getBoard(boardId)

                // then
                result shouldBe expectedBoard
                verify { boardQueryPort.findById(boardId) }
            }

            "존재하지 않는 ID로 예외를 발생시킨다." {
                // given
                val boardId = 1L
                every {
                    boardQueryPort.findById(boardId)
                } returns Optional.empty()

                // when
                val result = runCatching {
                    boardQueryService.getBoard(boardId) // Optional이 비어있을 때, orElseThrow()에서 예외가 발생한다.
                }

                // then
                result.isFailure shouldBe true
                verify { boardQueryPort.findById(boardId) }
            }
        }

        "findAll" - {
            "모든 게시글 목록을 페이징 조회한다." {
                // given
                val pageable = PageRequest.of(0, 10)
                val boards = listOf(
                    Board.builder().id(1L).title("First Board").status(BoardStatus.ACTIVE).build(),
                    Board.builder().id(2L).title("Second Board").status(BoardStatus.SUSPENDED).build()
                )
                val expectedPage = PageImpl(boards, pageable, boards.size.toLong())
                every {
                    boardQueryPort.findAll(pageable)
                } returns expectedPage

                // when
                val result = boardQueryService.findGeneralBy(pageable)

                // then
                result shouldBe expectedPage
                verify { boardQueryPort.findAll(pageable) }

            }
        }

        "findByStatuses" - {
            "상태 목록으로 조회 시, 페이징 조회한다." {
                // given
                val statuses = listOf(BoardStatus.ACTIVE, BoardStatus.SUSPENDED)
                val pageable = PageRequest.of(0, 10)
                val boards = listOf(
                    Board.builder().id(1L).title("Active Board").status(BoardStatus.ACTIVE).build(),
                    Board.builder().id(2L).title("Suspended Board").status(BoardStatus.SUSPENDED).build()
                )
                val expectedPage = PageImpl(boards, pageable, boards.size.toLong())
                every {
                    boardQueryPort.findByStatusesList(pageable, statuses)
                } returns expectedPage

                // when
                val result = boardQueryService.findByStatuses(pageable, statuses)

                // then
                result shouldBe expectedPage
                verify { boardQueryPort.findByStatusesList(pageable, statuses) }
            }

            "빈 상태 목록으로 조회 시, 빈 페이지를 조회한다." {
                // given
                val statuses = emptyList<BoardStatus>()
                val pageable = PageRequest.of(0, 10)
                val boards: List<Board> = emptyList()
                val expectedPage = PageImpl(boards, pageable, 0)
                every {
                    boardQueryPort.findByStatusesList(pageable, statuses)
                } returns expectedPage

                // when
                val result = boardQueryService.findByStatuses(pageable, statuses)

                // then
                result shouldBe expectedPage
                verify { boardQueryPort.findByStatusesList(pageable, statuses) }
            }
        }
    }
})
