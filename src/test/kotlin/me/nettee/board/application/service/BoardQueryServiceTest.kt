package me.nettee.board.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.exception.BoardCommandErrorCode.BOARD_NOT_FOUND
import me.nettee.board.application.exception.BoardCommandException
import me.nettee.board.application.model.BoardQueryModels.BoardSummary
import me.nettee.board.application.model.BoardQueryModels.BoardDetail
import me.nettee.board.application.port.BoardQueryPort
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Instant
import java.util.*

class BoardQueryServiceTest : FreeSpec({

    val boardQueryPort = mockk<BoardQueryPort>() // mocking
    val boardQueryService = BoardQueryService(boardQueryPort) // 주입

    "BoardQueryService" - {

        "getBoard" - {

            "board detail 조회 by board id" {
                // given
                val boardId = 1L
                val now = Instant.now()

                // 자바 record 이므로, 순서대로 인자 전달
                val expectedDetail = BoardDetail(
                        boardId,
                        "Test Title",
                        "Test Content",
                        BoardStatus.ACTIVE,
                        now,
                        now
                )

                every {
                    boardQueryPort.findById(boardId)
                } returns Optional.of(expectedDetail)

                // when
                val result = boardQueryService.getBoard(boardId)

                // then
                result shouldBe expectedDetail
                verify(exactly = 1) { boardQueryPort.findById(boardId) }
            }

            "board id에 해당하는 게시판이 없으면 예외 반환" {
                // given
                val boardId = 999L
                every {
                    boardQueryPort.findById(boardId)
                } returns Optional.empty()

                // when & then
                val exception = shouldThrow<BoardCommandException> {
                    boardQueryService.getBoard(boardId)
                }
                exception.errorCode shouldBe BOARD_NOT_FOUND

                verify(exactly = 1) { boardQueryPort.findById(boardId) }
            }
        }

        "findByStatuses" - {

            "BoardStatus로 조회" {
                // given
                val statuses = setOf(BoardStatus.ACTIVE, BoardStatus.SUSPENDED)
                val pageable = PageRequest.of(0, 10)
                val now = Instant.now()

                // 자바 record 이므로, 순서대로 인자 전달
                val summaries = listOf(
                        BoardSummary(
                                1L,
                                "Active Board",
                                BoardStatus.ACTIVE,
                                now,
                                now
                        ),
                        BoardSummary(
                                2L,
                                "Suspended Board",
                                BoardStatus.SUSPENDED,
                                now,
                                now
                        )
                )

                val expectedPage: Page<BoardSummary> =
                        PageImpl(summaries, pageable, summaries.size.toLong())

                every {
                    boardQueryPort.findByStatusesList(statuses, pageable)
                } returns expectedPage

                // when
                val result = boardQueryService.findByStatuses(statuses, pageable)

                // then
                result shouldBe expectedPage
                verify(exactly = 1) { boardQueryPort.findByStatusesList(statuses, pageable) }
            }

            "빈 상태 목록으로 조회하면 빈 페이지가 반환" {
                // given
                val statuses = emptySet<BoardStatus>()
                val pageable = PageRequest.of(0, 10)

                val emptySummaries = emptyList<BoardSummary>()
                val expectedPage: Page<BoardSummary> =
                        PageImpl(emptySummaries, pageable, 0)

                every {
                    boardQueryPort.findByStatusesList(statuses, pageable)
                } returns expectedPage

                // when
                val result = boardQueryService.findByStatuses(statuses, pageable)

                // then
                result shouldBe expectedPage
                verify(exactly = 1) { boardQueryPort.findByStatusesList(statuses, pageable) }
            }
        }
    }

})
