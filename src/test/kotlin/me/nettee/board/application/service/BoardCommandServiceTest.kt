package me.nettee.board.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus.REMOVED
import me.nettee.board.application.exception.BoardCommandException
import me.nettee.board.application.port.BoardCommandPort
import java.util.Optional
import me.nettee.board.application.exception.BoardCommandErrorCode.BOARD_NOT_FOUND


class BoardCommandServiceTest : FreeSpec({

    val boardCommandPort = mockk<BoardCommandPort>()
    val boardCommandService = BoardCommandService(boardCommandPort)

    "BoardCommandService" - {
        "create" {
            // given
            val board = Board()
            every {
                boardCommandPort.create(board)
            } returns board

            // when
            val result = boardCommandService.createBoard(board)

            // then
            result shouldBe board
            verify { boardCommandPort.create(board) }
        }

        "update" {
            // given
            val board = Board()
            every {
                boardCommandPort.update(board)
            } returns board

            // when
            val result = boardCommandService.updateBoard(board)

            // then
            result shouldBe board
            verify { boardCommandPort.update(board) }
        }

        // TO-DO 테스트 코드 수정이 필요합니다.
        "delete" - {

            "Soft Delete - 게시판을 삭제" {
                // Mock 생성
                val boardCommandPort = mockk<BoardCommandPort>()
                val boardCommandService = BoardCommandService(boardCommandPort)

                // given
                val boardId = 1L
                val board = Board()

                every { boardCommandPort.findById(boardId) } returns Optional.of(board)
                every { boardCommandPort.updateStatus(boardId, REMOVED) } returns Unit

                // when
                boardCommandService.deleteBoard(boardId)

                // then
                verify(exactly = 1) { boardCommandPort.findById(boardId) }
                verify(exactly = 1) { boardCommandPort.updateStatus(boardId, REMOVED) }
            }

            "게시판을 찾지 못하면 예외를 발생" {
                // Mock 생성
                val boardCommandPort = mockk<BoardCommandPort>()
                val boardCommandService = BoardCommandService(boardCommandPort)

                // given
                val boardId = 999L
                every { boardCommandPort.findById(boardId) } returns Optional.empty()

                // when & then
                val exception = shouldThrow<BoardCommandException> {
                    boardCommandService.deleteBoard(boardId)
                }
                exception.errorCode shouldBe BOARD_NOT_FOUND

                // updateStatus가 호출되면 안 된다
                verify(exactly = 1) { boardCommandPort.findById(boardId) }
                verify(exactly = 0) { boardCommandPort.updateStatus(any(), any()) }
            }
        }
    }
})
