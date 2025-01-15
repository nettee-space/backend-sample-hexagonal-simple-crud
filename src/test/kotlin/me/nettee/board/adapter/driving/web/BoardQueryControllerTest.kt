package me.nettee.board.adapter.driving.web
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.FreeSpec
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardDetailResponse
import me.nettee.board.adapter.driving.web.dto.BoardQueryDto.BoardSummaryResponse
import me.nettee.board.adapter.driving.web.mapper.BoardDtoMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import me.nettee.board.application.usecase.BoardReadByStatusesUseCase
import me.nettee.board.application.usecase.BoardReadUseCase
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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
    @MockitoBean private val boardReadByStatusesUseCase : BoardReadByStatusesUseCase,
    @MockitoBean private val boardDtoMapper : BoardDtoMapper,
    @Autowired private val mvc: MockMvc
) : FreeSpec({

    val objectMapper = ObjectMapper()
    lateinit var boardList: List<Board>

    beforeSpec {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)

        // list 반복문 완성 코드 ***
        boardList = (1..14).flatMap {
            listOf(
                Board(it.toLong(), "title$it", "content$it", BoardStatus.ACTIVE, Instant.now(), null, null),
                Board(it.toLong(), "title$it", "content$it", BoardStatus.SUSPENDED, Instant.now(), null, null)
            )
        }

        `when` (boardReadUseCase.getBoard(anyLong())).thenAnswer { findBoardById(boardList, it.arguments[0] as Long) }
        `when`(boardDtoMapper.toDtoDetail(any(Board::class.java))).thenAnswer { mapToBoardDetailResponse(it.getArgument(0) as Board) }
        `when`(boardDtoMapper.toDtoSummary(any(Board::class.java))).thenAnswer { mapToBoardSummaryResponse(it.getArgument(0) as Board) }
        `when`(boardReadByStatusesUseCase.findByStatuses(any<PageRequest>(), any<List<BoardStatus>>())).thenAnswer {
            createPageFromBoardListByStatusList(boardList, it.getArgument<PageRequest>(0), it.getArgument<List<BoardStatus>>(1))
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

    "statuses 사용 게시판 목록 조회" - {
        val mvcGet = fun(page: Int, statuses: List<BoardStatus>): ResultActionsDsl{
            return mvc.get("/api/v1/board/statuses") {
                queryParam("page", page.toString())
                queryParam("size", "10")
                queryParam("statuses", statuses.joinToString(",") { it.name })
                contentType = MediaType.APPLICATION_JSON
            }
        }

        "정상 요청으로 인한 2xx 상태 반환" {
            val statuses = listOf(BoardStatus.ACTIVE, BoardStatus.REMOVED)
            val mvcResult = mvcGet(1, statuses)
                .andExpect { status { is2xxSuccessful() } }
                .andReturn()

            mvcResult.response.contentAsString
                .let { objectMapper.readTree(it) as ObjectNode }
                .let { println(it) }
        }

        "정상 요청으로 인한 4xx 상태 반환" {
            val statuses = listOf( BoardStatus.REMOVED)
            val mvcResult = mvcGet(2, statuses)
                .andExpect { status { is4xxClientError() } }
                .andReturn()

            println("status: ${mvcResult.response.status}")
        }
    }
})


fun mapToBoardSummaryResponse(board: Board): BoardSummaryResponse {
    return BoardSummaryResponse.builder()
        .id(board.id)
        .title(board.title)
        .status(board.status)
        .createdAt(board.createdAt)
        .build()
}

fun mapToBoardDetailResponse(board: Board): BoardDetailResponse {
    return BoardDetailResponse.builder()
        .id(board.id)
        .title(board.title)
        .content(board.content)
        .status(board.status)
        .createdAt(board.createdAt)
        .updatedAt(board.updatedAt)
        .build()
}

fun findBoardById(boardList: List<Board>, boardId: Long): Board {
    return boardList.associateBy { it.id }[boardId]
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid board ID: $boardId")
}

fun createPageFromBoardListByStatusList(
    boardList: List<Board>,
    pageable: PageRequest,
    boardStatusList: List<BoardStatus>
): PageImpl<Board> {
   val filteredBoards = boardList.filter { board ->
        board.status in boardStatusList
    }.takeIf { it.isNotEmpty() }
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid board status: $boardStatusList")

    val pageContent = filteredBoards.drop(pageable.pageNumber * pageable.pageSize)
        .take(pageable.pageSize)
        .takeIf { it.isNotEmpty() }
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid page size: ${pageable.pageSize}")

    return PageImpl(pageContent, pageable, filteredBoards.size.toLong())
}