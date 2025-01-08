package me.nettee.board.adapter.driving.web
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.usecase.BoardReadUseCase
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant


@ExtendWith(SpringExtension::class)
@WebMvcTest(BoardQueryController::class)
@AutoConfigureMockMvc
class BoardQueryControllerTest(
    @MockitoBean  private val boardReadUseCase : BoardReadUseCase,
    @MockitoBean private val boardDtoMapper : BoardDtoMapper,
    @Autowired private val mvc: MockMvc
) : FreeSpec({

    val objectMapper = ObjectMapper()
    objectMapper.registerModule(JavaTimeModule())
    objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)

    "게시판 상세 조회"-{
        "boardId로 게시판이 조회될때"{
            val boardId = 1L
            val board = Board(1,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
            val boardDetailResponse = BoardDetailResponse.builder()
                .id(board.id)
                .title(board.title)
                .content(board.content)
                .status(board.status)
                .createdAt(board.createdAt)
                .updatedAt(board.updatedAt)
                .build()

            `when` (boardReadUseCase.getBoard(boardId)).thenReturn(board)
            `when` (boardDtoMapper.toDtoDetail(board)).thenReturn(boardDetailResponse)

            val result = mvc.get("/api/v1/board/$boardId"){
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
            }.andReturn()

            val response = objectMapper.readValue(result.response.contentAsString, BoardDetailResponse::class.java)

            println(objectMapper.writeValueAsString(response))
            println("response: $response")
            println("boardDetailResponse: $boardDetailResponse")
            response shouldBe boardDetailResponse

        }
    }

    "게시판 목록 조회"{

        val board1 = Board(1,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
        val board2 = Board(2,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
        val board3 = Board(3,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
        val board4 = Board(4,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
        val board5 = Board(5,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
        val board6 = Board(6,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)
        val board7 = Board(7,"title","content", BoardStatus.ACTIVE, Instant.now(), null, null)

        val pageable: Pageable = PageRequest.of(0, 10)
        val boardList = listOf(board1, board2, board3, board4, board5, board6, board7)
        val boardPage = PageImpl(boardList, pageable, boardList.size.toLong())

        `when` (boardReadUseCase.findGeneralBy(pageable)).thenReturn(boardPage)
        boardList.forEach { board ->
            `when`(boardDtoMapper.toDtoSummary(board)).thenReturn(
                BoardSummaryResponse.builder()
                    .id(board.id)
                    .title(board.title)
                    .status(board.status)
                    .createdAt(board.createdAt)
                    .build()
            )
        }

        val result = mvc.get("/api/v1/board"){
            param("page", "0")
            param("size", "10")
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val jsonNode = objectMapper.readTree(result.response.contentAsString)
        val contentJson = jsonNode.get("content").toString()
        val response: List<BoardSummaryResponse> = objectMapper.readValue(
            contentJson,
            object : TypeReference<List<BoardSummaryResponse>>() {}
        )
        val jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response)

        println("json Output:")
        println(jsonOutput)

        println("Compact JSON Output:")
        println(objectMapper.writeValueAsString(response))

        println("Response Objects:")
        response.forEach { println(it) }

    }
})