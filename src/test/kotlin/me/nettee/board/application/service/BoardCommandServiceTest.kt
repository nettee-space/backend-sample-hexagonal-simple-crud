package me.nettee.board.application.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nettee.board.application.domain.Board
import me.nettee.board.application.port.BoardCommandPort

class BoardCommandServiceTest : FreeSpec({

    val boardCommandPort = mockk<BoardCommandPort>()
    val boardCommandService = BoardCommandService(boardCommandPort)

    "BoardCommandService" - {
        "create" {
            // given
            var board = Board()
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

        "delete" {
            // given
            val boardId = 1L
            every {
                boardCommandPort.delete(boardId)
            } returns Unit

            // when
            boardCommandService.deleteBoard(boardId)

            // then
            verify { boardCommandPort.delete(boardId) }
        }
    }
})
