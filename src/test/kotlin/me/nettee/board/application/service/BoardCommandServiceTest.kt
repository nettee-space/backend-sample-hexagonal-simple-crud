package me.nettee.board.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus.REMOVED
import me.nettee.board.application.exception.BoardCommandException
import me.nettee.board.application.port.BoardCommandPort
import java.util.Optional
import me.nettee.board.application.exception.BoardCommandErrorCode.BOARD_NOT_FOUND
import me.nettee.board.application.exception.BoardCommandErrorCode.DEFAULT


class BoardCommandServiceTest : FreeSpec({

    val boardCommandPort = mockk<BoardCommandPort>()
    val boardCommandService = BoardCommandService(boardCommandPort)

    beforeTest {
        clearMocks(boardCommandPort, answers = true, recordedCalls = true)
    }

    "BoardCommandService" - {
        "create" {
            // given
            val board = Board()
            every { boardCommandPort.create(board) } returns board

            // when
            val result = boardCommandService.createBoard(board)

            // then
            result shouldBe board
            verify(exactly = 1) { boardCommandPort.create(board) }
        }

        "update" {
            // given
            val board = Board()
            every { boardCommandPort.update(board) } returns board

            // when
            val result = boardCommandService.updateBoard(board)

            // then
            result shouldBe board
            verify(exactly = 1) { boardCommandPort.update(board) }
        }

        "delete" - {
            "Soft Delete - 정상적으로 삭제된 경우" {
                // given
                val boardId = 1L
                val board = Board()

                every { boardCommandPort.updateStatus(boardId, REMOVED) } returns Unit
                every { boardCommandPort.findById(boardId) } returns Optional.of(board)

                // when
                boardCommandService.deleteBoard(boardId)

                // then
                verify(exactly = 1) { boardCommandPort.updateStatus(boardId, REMOVED) }
                verify(exactly = 1) { boardCommandPort.findById(boardId) }
            }

            "Exception - 게시판을 찾지 못하면 예외 처리" {
                // given
                val boardId = 1L

                every { boardCommandPort.updateStatus(boardId, REMOVED) } returns Unit
                every { boardCommandPort.findById(boardId) } returns Optional.empty()

                // when & then
                val exception = shouldThrow<BoardCommandException> {
                    boardCommandService.deleteBoard(boardId)
                }
                exception.errorCode shouldBe BOARD_NOT_FOUND

                verify(exactly = 1) { boardCommandPort.updateStatus(boardId, REMOVED) }
                verify(exactly = 1) { boardCommandPort.findById(boardId) }
            }

            "Exception - 삭제 후 조회된 Board의 상태가 REMOVED가 아니면 예외 처리" {
                // given
                val boardId = 1L
                val board = Board()

                every { boardCommandPort.updateStatus(boardId, REMOVED) } returns Unit
                every { boardCommandPort.findById(boardId) } returns Optional.of(board)

                // when & then
                val exception = shouldThrow<BoardCommandException> {
                    boardCommandService.deleteBoard(boardId)
                }
                exception.errorCode shouldBe DEFAULT

                verify(exactly = 1) { boardCommandPort.updateStatus(boardId, REMOVED) }
                verify(exactly = 1) { boardCommandPort.findById(boardId) }
            }
        }
    }
})
