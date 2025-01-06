package me.nettee.board.application.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nettee.board.application.domain.Board
import me.nettee.board.application.port.BoardQueryPort
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class BoardQueryServiceTest : FreeSpec({

    val boardQueryPort = mockk<BoardQueryPort>() // mocking
    val boardQueryService = BoardQueryService(boardQueryPort) // 주입

    "BoardQueryService" - {
        "findAll" {
            // given
            val pageable = PageRequest.of(0, 10)
            val boardPage = PageImpl(emptyList<Board>())
            every {
                boardQueryPort.findAll(pageable)
            } returns boardPage

            // when
            val result = boardQueryService.findGeneralBy(pageable)

            // then
            result shouldBe boardPage
            verify { boardQueryPort.findAll(pageable) }
        }


        "findById" {
            // given
            val boardId = 1L
            val board = Board()
            every {
                boardQueryPort.findById(boardId)
            } returns Optional.of(board)

            // when
            val result = boardQueryService.getBoard(boardId)

            // then
            result shouldBe board
            verify { boardQueryPort.findById(boardId) }
        }
    }
})
