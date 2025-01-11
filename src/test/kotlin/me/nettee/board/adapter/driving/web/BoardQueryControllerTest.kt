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

    lateinit var board1: Board
    lateinit var board2: Board
    lateinit var board3: Board
    lateinit var board4: Board
    lateinit var board5: Board
    lateinit var board6: Board
    lateinit var board7: Board
    lateinit var boardList: List<Board>
    lateinit var pageable: Pageable
    lateinit var boardPage: Page<Board>

    beforeSpec {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)

        board1 = Board(1,"title1","content1", BoardStatus.ACTIVE, Instant.now(), null, null)
        board2 = Board(2,"title2","content2", BoardStatus.ACTIVE, Instant.now(), null, null)
        board3 = Board(3,"title3","content3", BoardStatus.ACTIVE, Instant.now(), null, null)
        board4 = Board(4,"title4","content4", BoardStatus.ACTIVE, Instant.now(), null, null)
        board5 = Board(5,"title5","content5", BoardStatus.ACTIVE, Instant.now(), null, null)
        board6 = Board(6,"title6","content6", BoardStatus.ACTIVE, Instant.now(), null, null)
        board7 = Board(7,"title7","content7", BoardStatus.ACTIVE, Instant.now(), null, null)

        boardList = listOf(board1, board2, board3, board4, board5, board6, board7)
        pageable = PageRequest.of(0, 10)
        boardPage = PageImpl(boardList, pageable, boardList.size.toLong())

        `when` (boardReadUseCase.getBoard(anyLong())).thenAnswer { v ->
            val boardId = v.arguments[0] as Long
            boardList.associateBy { it.id }[boardId]
                ?: throw IllegalArgumentException(" 조회할 수 없는 게시판입니다 id=$boardId")
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
        `when` (boardReadUseCase.findGeneralBy(pageable)).thenReturn(boardPage)
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


    "게시판 상세 조회"-{
        "boardId로 게시판이 조회될때"{

            val result = mvc.get("/api/v1/board/2"){
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
            }.andReturn()

            val response = objectMapper.readValue(result.response.contentAsString, BoardDetailResponse::class.java)

            println(objectMapper.writeValueAsString(response))
            println("response: $response")
        }
    }

    "게시판 목록 조회"{

        val result = mvc.get("/api/v1/board"){
            param("page", "0")
            param("size", "10")
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val jsonNode = objectMapper.readTree(result.response.contentAsString)
        val contentJson = jsonNode["content"]

        println("jsonNode :")
        println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode))

        println("contentJson :")
        println(objectMapper.writeValueAsString(contentJson))
    }
})