package me.nettee.board.adapter.driving.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.every
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCommandResponse
import me.nettee.board.adapter.driving.web.dto.BoardCommandDto.BoardCreateCommand
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.usecase.BoardCreateUseCase
import me.nettee.board.application.usecase.BoardDeleteUseCase
import me.nettee.board.application.usecase.BoardUpdateUseCase
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ExtendWith(SpringExtension::class)
@WebMvcTest(BoardCommandController::class)
@AutoConfigureMockMvc
class BoardCommandControllerTest(
    @Autowired private val mvc: MockMvc,
    @MockitoBean private val boardCreateUseCase : BoardCreateUseCase,
    @MockitoBean private val boardUpdateUseCase : BoardUpdateUseCase,
    @MockitoBean private val boardDeleteUseCase : BoardDeleteUseCase,
    @MockitoBean private val boardDtoMapper : BoardDtoMapper
) : FreeSpec({

    val objectMapper = ObjectMapper()

    "게시판 생성"-{
        "게시판 NotNull을 충족하여 생성한 경우"{
            // given
            val boardCreateCommand = BoardCreateCommand("게시판 테스트","게시판 테스트 내용")
            var newBoard = Board(1,"게시판 테스트","게시판 테스트 내용",BoardStatus.ACTIVE, null, null, null)
            val boardCommandResponse = BoardCommandResponse.builder()
                .board(newBoard)
                .build()

            `when` (boardDtoMapper.toDomain(any())).thenReturn(newBoard)
            `when` (boardCreateUseCase.createBoard(any())).thenReturn(newBoard)

            // when
            val result = mvc.post("/api/v1/board"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(boardCreateCommand)
            }.andExpect {
                status { isCreated() }
            }.andReturn()

            // then
            val responseResult = objectMapper.readValue(result.response.contentAsString, BoardCommandResponse::class.java)
            println(responseResult.board.equals(boardCommandResponse.board))

            println(objectMapper.writeValueAsString(responseResult))
            println("responseResult: $responseResult")
            println("boardCommandResponse: $boardCommandResponse")

            responseResult.board.id shouldBeEqual boardCommandResponse.board.id
            // responseResult.board shouldBeEqual boardCommandResponse.board
        }
    }
})