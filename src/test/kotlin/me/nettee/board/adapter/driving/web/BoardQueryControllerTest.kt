package me.nettee.board.adapter.driving.web
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.usecase.BoardReadUseCase
import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.web.server.ResponseStatusException
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

    lateinit var boardList: List<Board>

    beforeSpec {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)

        // list 반복문 완성 코드 ***
        boardList = (1..14).map {
            Board(it.toLong(),"title$it","content$it", BoardStatus.ACTIVE, Instant.now(), null, null)
        }
        `when` (boardReadUseCase.getBoard(anyLong())).thenAnswer { v ->
            val boardId = v.arguments[0] as Long
            boardList.associateBy { it.id }[boardId]
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid board ID: $boardId")
        }
        `when` (boardDtoMapper.toDtoDetail(any(Board::class.java))).thenAnswer { v ->
            val board = v.getArgument(0) as Board
            BoardDetailResponse.builder()
                .id(board.id)
                .title(board.title)
                .content(board.content)
                .status(board.status)
                .createdAt(board.createdAt)
                .updatedAt(board.updatedAt)
                .build()
        }
        `when` (boardReadUseCase.findGeneralBy(any<PageRequest>())).thenAnswer { v ->
            val pageable = v.getArgument<PageRequest>(0)
            val pageContent = boardList.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)
                .takeIf { it.isNotEmpty() }
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page size: ${pageable.pageSize}")

            PageImpl(pageContent, pageable, boardList.size.toLong())
        }
        `when` (boardDtoMapper.toDtoSummary(any(Board::class.java))).thenAnswer { v ->
            val board = v.getArgument(0) as Board
            BoardSummaryResponse.builder()
                .id(board.id)
                .title(board.title)
                .status(board.status)
                .createdAt(board.createdAt)
                .build()
        }
    }

    "게시판 상세 조회" - {

        val mvcGet = fun(boardId: Long): ResultActionsDsl{
            return mvc.get("/api/v1/board/$boardId") {
                contentType = MediaType.APPLICATION_JSON
            }
        }

        "정상 요청으로 인한 2xx 상태 반환" {
            val mvcResult = mvcGet(1)
                .andExpect { status { is2xxSuccessful() } }
                .andReturn()

            mvcResult.response.contentAsString
                .let { objectMapper.readValue(it, BoardDetailResponse::class.java) }
                .let { objectMapper.writeValueAsString(it) }
                .let { println(it) }

        }

        "비정상 요청으로 인한 4xx 상태 반환" {
            val mvcResult =mvcGet(200)
                .andExpect { status{is4xxClientError()}}
                .andReturn()

            println("status: ${mvcResult.response.status}")
        }

    }

    "게시판 목록 조회" - {

        val mvcGet = fun(page: Int): ResultActionsDsl{
            return mvc.get("/api/v1/board") {
                queryParam("page", page.toString())
                queryParam("size", "10")
                contentType = MediaType.APPLICATION_JSON
            }
        }

        "정상 요청으로 인한 2xx 상태 반환" {
            val mvcResult = mvcGet(1)
                .andExpect { status { is2xxSuccessful() } }
                .andReturn()

            mvcResult.response.contentAsString
            .let { objectMapper.readTree(it) as ObjectNode }
            .let { println(it) }
        }

        "정상 요청으로 인한 4xx 상태 반환" {
            val mvcResult = mvcGet(2)
                .andExpect { status { is4xxClientError() } }
                .andReturn()

            println("status: ${mvcResult.response.status}")
        }
    }
})